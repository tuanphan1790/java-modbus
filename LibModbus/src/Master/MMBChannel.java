package Master;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

import javax.activity.InvalidActivityException;
import javax.management.openmbean.InvalidOpenTypeException;

import Common.ModbusTypes;
import Connection.ConnectionSettings;
import Connection.TcpClientSettings;
import Master.Request.Request;

public class MMBChannel {
	public MMBChannel() {
			
		listRequest = new Vector<Request>();
		countSendRequest = 0;
		
		channelStared = false;
		
		childsDevices = new Vector<MMBDevice>();
	}

	private Object lock = new Object();

	private ModbusTypes modbusTypes;

	private int threadBufferSize = 256;

	private final int DefaultReconnectInterval = 5000;

	private int timeout;

	private Timer timerReconnect;
	private TimerTask task;

	private AsynchronousSocketChannel tcpClient;

	Vector<Request> listRequest;
	Request currentRequest;
	int countSendRequest;
	Boolean newRequest;

	StreamWorker streamWorker;
	Thread thread;

	private ConnectionSettings connectionSettings;

	private Vector<MMBDevice> childsDevices;

	private Boolean channelStared;

	class TimerHelper extends TimerTask {
		@Override
		public void run() {
			synchronized (lock) {
				if (!channelStared) {
					return;
				}

				Connect();
			}
		}
	}

	public ModbusTypes getModbusTypes() {
		return modbusTypes;
	}

	public void setModbusTypes(ModbusTypes modbusTypes) {
		this.modbusTypes = modbusTypes;
	}

	public MMBDevice AddDevice(MMBDevice device) {
		synchronized (lock) {
			for (MMBDevice de : childsDevices) {
				if (device.getIdDevice() == de.getIdDevice())
					throw new NullPointerException("Device is existed");
			}
			
			childsDevices.add(device);

			return device;
		}
	}

	public void RemoveDevice(byte deviceId) {
		synchronized (lock) {
			for (MMBDevice device : childsDevices) {
				if (device.getIdDevice() == deviceId) {
					childsDevices.remove(device);
				}
			}
		}
	}

	public void Start(ConnectionSettings connSettings, ModbusTypes modType) {
		synchronized (lock) {
			if (channelStared)
				throw new RuntimeException();

			channelStared = true;

			this.connectionSettings = connSettings;
			this.modbusTypes = modType;

			this.Reconnect();
		}
	}

	public void Reconnect() {
		timerReconnect = new Timer();	
		task = new TimerHelper();
		this.timerReconnect.schedule(task, DefaultReconnectInterval);
	}

	public void Stop() {
		synchronized (lock) {
			if (!channelStared)
				return;

			channelStared = false;
		}
	}

	private void Connect() {
		switch (modbusTypes) {
		case RTU:
			ConnectSerial();
			break;

		case TCP:
			ConnectTcpClient();
			break;

		default:
			break;
		}
	}

	private void ConnectTcpClient() {
		try {
			tcpClient = AsynchronousSocketChannel.open();

			TcpClientSettings settings = connectionSettings.getClientSettings();
			InetSocketAddress add = new InetSocketAddress(settings.getAddress(), settings.getPort());
			tcpClient.connect(add, this, new CallbackAsynConnect());
		} catch (Exception ex) {
			this.Reconnect();
		}
	}

	class CallbackAsynConnect implements CompletionHandler<Void, MMBChannel> {
		@Override
		public void completed(Void result, MMBChannel attachment) {

			timerReconnect.cancel();
			listener.OnChannelConnectedChange(true);

			listRequest.clear();

			streamWorker = new StreamWorker();
			thread = new Thread(streamWorker);
			thread.start();
			
			for(MMBDevice device : childsDevices)
			{
				device.Start();
			}

			for (MMBDevice device : childsDevices) {
				listRequest = device.GetRequests();
			}

			if (listRequest.size() > 0) {
				SendRequestIndex0();
			}
			else
			{
				ExecutorService pool = Executors.newSingleThreadExecutor();
				PoolGetRequest task = new PoolGetRequest();
				pool.submit(task);
			}
		}	

		@Override
		public void failed(Throwable exc, MMBChannel attachment) {
			attachment.Reconnect();
		}
	}
	
	class PoolGetRequest implements Runnable{

		@Override
		public void run() {
			while(true)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (MMBDevice device : childsDevices) {
					listRequest = device.GetRequests();
				}

				if (listRequest.size() > 0) {
					SendRequestIndex0();
					break;
				}
			}
		}
		
	}

	private void SendRequestIndex0() {
		synchronized (lock) {
			if (currentRequest != null)
				return;
			
			if(listRequest.size() ==0 )
				return;

			newRequest = true;
			currentRequest = listRequest.firstElement();
			listRequest.remove(0);

			byte[] byteWrite = currentRequest.GetRequest(modbusTypes);
			ByteBuffer buffer = ByteBuffer.wrap(byteWrite);

			try {
				currentRequest.StartWait();
				tcpClient.write(buffer);
			} catch (Exception ex) {
				this.Disconnect(ex);
			}
		}
	}

	private void ResendCurrentRequest() {
		if (currentRequest == null)
			return;

		countSendRequest = countSendRequest + 1;
		byte[] byteWrite = currentRequest.GetRequest(modbusTypes);
		ByteBuffer buffer = ByteBuffer.wrap(byteWrite);

		try {
			currentRequest.StartWait();
			tcpClient.write(buffer);
		} catch (Exception ex) {
			this.Disconnect(ex);
		}
	}

	public void RequestTimeout(Request request) {
		if (!this.channelStared)
			return;

		if (request.isCommand) {

		} else {
			if (countSendRequest == 3) {
				currentRequest.RequestFail();
				currentRequest = null;

				if (listRequest.size() > 0) {
					SendRequestIndex0();
				}
				else
				{
					ExecutorService pool = Executors.newSingleThreadExecutor();
					PoolGetRequest task = new PoolGetRequest();
					pool.submit(task);
				}
				
			} else {
				
				this.ResendCurrentRequest();
			}
		}
	}

	private class StreamWorker implements Runnable {
		@Override
		public void run() {

			ByteBuffer receives;
			int offset = 0;
			Request request = null;

			while (true) {
				
				receives = ByteBuffer.allocate(threadBufferSize);

				try {
					Future<Integer> readResult = tcpClient.read(receives);
					int numByteReads = readResult.get();
	
					synchronized (lock) {
						if (currentRequest == null) {
							offset = 0;
							continue;
						}

						if (newRequest) {
							offset = numByteReads;

							newRequest = false;
						} else {
							offset = offset + numByteReads;
						}
					}
				} catch (Exception ex) {
					Disconnect(ex);
					break;
				}

				if (modbusTypes == ModbusTypes.RTU) {
//					byte[] buffer = receives.array();
//					while(true)
//					{
//						if(offset > 0)
//						{
//							if(buffer[0] == currentRequest.device.getIdDevice())
//							{
//								break;
//							}
//							else
//							{
//								offset--;
//								buffer = Arrays.copyOfRange(buffer, 1, buffer.length);
//								continue;
//							}
//						}					
//					}
				} 
				
				if(offset == currentRequest.ExpectResponseExceptionLength)
				{
					currentRequest.ProcessResponse(receives.array(), offset);
					switch(currentRequest.responseResult)
					{
						case None:
							break;
						case ExceptionReturn:
						{
							offset = 0;							
							currentRequest.StopWait();
							
							request = currentRequest;
							currentRequest = null;
							if (listRequest.size() > 0) {
								SendRequestIndex0();
							}
							else
							{
								ExecutorService pool = Executors.newSingleThreadExecutor();
								PoolGetRequest task = new PoolGetRequest();
								pool.submit(task);
							}
							
							break;
						}
						default:
						{
							offset = 0;
							currentRequest.StopWait();
							
							if(currentRequest.isCommand | countSendRequest == 3)
							{
								request = currentRequest;
								currentRequest = null;
								if (listRequest.size() > 0) {
									SendRequestIndex0();
								}
								else
								{
									ExecutorService pool = Executors.newSingleThreadExecutor();
									PoolGetRequest task = new PoolGetRequest();
									pool.submit(task);
								}
							}
							else
							{
								ResendCurrentRequest();
							}
							break;
						}
					}
					continue;
				}
				
				if(offset >= currentRequest.ExpectResponseFrameLength)
				{
					currentRequest.ProcessResponse(receives.array(), offset);
					offset = 0;
					currentRequest.StopWait();
					
					switch(currentRequest.responseResult)
					{
						case Good:
						{
							offset = 0;							
							currentRequest.StopWait();
							
							request = currentRequest;
							currentRequest = null;
							if (listRequest.size() > 0) {
								SendRequestIndex0();
							}
							else
							{
								ExecutorService pool = Executors.newSingleThreadExecutor();
								PoolGetRequest task = new PoolGetRequest();
								pool.submit(task);
							}
												
							break;
						}
						default:
						{
							offset = 0;
							currentRequest.StopWait();
							
							if(currentRequest.isCommand | countSendRequest == 3)
							{
								request = currentRequest;
								currentRequest = null;
								if (listRequest.size() > 0) {
									SendRequestIndex0();
								}
								else
								{
									ExecutorService pool = Executors.newSingleThreadExecutor();
									PoolGetRequest task = new PoolGetRequest();
									pool.submit(task);
								}
							}
							else
							{
								ResendCurrentRequest();
							}
							break;
						}
					}
				}
				
				if(request != null)
				{
					switch(request.responseResult)
					{
					case Good:
					case ExceptionReturn:
						request.FireEvent();
					default:
						request.RequestFail();
					}
				}
			}
		}
	}

	private void ConnectSerial() {

	}

	private void Disconnect(Exception ex) {
		
		switch (modbusTypes) {
		case RTU:
			DisconnectSerial();
			break;

		case TCP:
			DisconnectTcpClient();
			break;
		}
	}

	private void DisconnectTcpClient() {
		tcpClient = null;
		currentRequest = null;
		
		this.listener.OnChannelConnectedChange(false);
		
		for(MMBDevice device : childsDevices)
		{
			device.Stop();
		}
		
		for(Request req : listRequest)
		{
			req.ChannelDisconnect();
		}
		listRequest.clear();	
		
		this.Reconnect();
	}

	private void DisconnectSerial() {

	}

	OnChannelConnectedListener listener;

	public void SetOnChannelConnectedListener(OnChannelConnectedListener listener) {
		this.listener = listener;
	}

}
