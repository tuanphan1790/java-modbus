package Master.Request;

import java.util.Timer;
import java.util.TimerTask;

import Common.ModbusTypes;
import Master.MMBChannel;
import Master.MMBDevice;

public abstract class Request {
	public MMBChannel channel;
	public MMBDevice device;
	
	public int timeout;	
	private Timer timerTimeOut;
	private TimerTask task;
	
	protected short transactionId;
	
	public ModbusTypes modbusTypes;
	
	public Boolean isCommand;
	
	private static short transactCounter = 0;

    protected static byte[] GetNextTransactId()
    {
    	byte[] ret = new byte[2];
    	transactCounter = (short) (transactCounter + 1);
    	
    	ret[0] = (byte)(transactCounter & 0xff);
    	ret[1] = (byte)((transactCounter >> 8) & 0xff);
    	
        return ret;
    }
	
	public int ExpectResponseExceptionLength;
	public int getExpectResponseExceptionLength() {
		return ExpectResponseExceptionLength;
	}

	public void setExpectResponseExceptionLength(int expectResponseExceptionLength) {
		ExpectResponseExceptionLength = expectResponseExceptionLength;
	}

	public int getExpectResponseFrameLength() {
		return ExpectResponseFrameLength;
	}

	public void setExpectResponseFrameLength(int expectResponseFrameLength) {
		ExpectResponseFrameLength = expectResponseFrameLength;
	}


	public int ExpectResponseFrameLength;
	
	protected byte[] sendBuffer;
	
	protected byte[] transacId;
	
	public ResponseCodes responseResult;
	public byte exceptionCode;	

	
	public Request(MMBChannel channel, MMBDevice device, int timeout, Boolean isCommand) {
		this.channel = channel;
		this.device = device;
		this.timeout = timeout;
		this.isCommand = isCommand;
		
		
	}
	
	private class TimeTaskTimeOut extends TimerTask{

		@Override
		public void run() {
			channel.RequestTimeout(Request.this);
		}	
	}
	
	public void StartWait() {
		timerTimeOut = new Timer();
		task = new TimeTaskTimeOut();
		timerTimeOut.schedule(task, timeout);
	}
	
	public void StopWait() {
		timerTimeOut.cancel();
	}
	
	public void ChannelDisconnect()
	{
		task.cancel();
		timerTimeOut.cancel();
	}
	
	protected abstract String GetRequestInfo_RTU();

    protected abstract String GetRequestInfo_TCP();
	
	public abstract byte[] GetRequest(ModbusTypes modbusType);
	
	public abstract void ProcessResponse(byte[] data, int length);
	
	 public void FireEvent()
     {
         switch (this.responseResult)
         {
             case Good:
                 this.FireEvent_Good();
                 break;

             case ExceptionReturn:
                 this.FireEvent_Exception();
                 break;
         }
     }
	
	protected abstract void FireEvent_Exception();

    protected abstract void FireEvent_Good();
	
    public void RequestFail()
    {
    	
    }
}
