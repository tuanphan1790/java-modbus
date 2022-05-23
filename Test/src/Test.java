import java.io.IOException;

import Common.ModbusTypes;
import Connection.ConnectionSettings;
import Connection.TcpClientSettings;
import Master.MMBChannel;
import Master.MMBDevice;
import Master.OnChannelConnectedListener;
import Master.OnCoilsChangeListener;
import Master.Point.MMBCoil;

public class Test {

	public static void main(String[] args) throws IOException {
		
		MMBChannel channel = new MMBChannel();
		channel.SetOnChannelConnectedListener(new OnChannelConnectedListener() {
			
			@Override
			public void OnChannelConnectedChange(Boolean connected) {
				System.out.print("\nChannel connected change: " + connected);		
			}
		});
		
		TcpClientSettings tcpClient = new TcpClientSettings("127.0.0.1", 502);
		ConnectionSettings con = new ConnectionSettings(tcpClient); 
		
		channel.Start(con, ModbusTypes.TCP);
		
		MMBDevice device = new MMBDevice(channel, (byte)1);
		channel.AddDevice(device);
		try 
		{
			device.setIntervalReadCoil(3000);
			
			device.AddCoil((short)1);
			device.AddCoil((short)2);
			device.AddCoil((short)3);
			device.AddCoil((short)4);
			device.AddCoil((short)5);
			
			device.SetOnCoilChangeListener(new OnCoilsChangeListener() {
				
				@Override
				public void OnCoilChange(MMBCoil coil) {
					System.out.print("\nAddress " + coil.getAddress() + " update value " + coil.getValue());					
				}
			});
		}
		catch(Exception ex)
		{
			System.out.print(ex.getMessage());
		}
		
		device.Start();
		
		System.in.read();
	}
}
