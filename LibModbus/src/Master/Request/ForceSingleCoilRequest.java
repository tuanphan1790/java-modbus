package Master.Request;

import Common.ModbusTypes;
import Master.MMBChannel;
import Master.MMBDevice;

public class ForceSingleCoilRequest extends Request{
	public ForceSingleCoilRequest(MMBChannel channel, MMBDevice device, int timeout) {
		super(channel, device, timeout, true);
	}

	@Override
	protected String GetRequestInfo_RTU() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String GetRequestInfo_TCP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] GetRequest(ModbusTypes modbusType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ProcessResponse(byte[] data, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void FireEvent_Exception() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void FireEvent_Good() {
		// TODO Auto-generated method stub
		
	}
	
	
}
