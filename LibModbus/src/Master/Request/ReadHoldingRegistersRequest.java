package Master.Request;

import java.util.Vector;

import Common.ModbusTypes;
import Master.MMBChannel;
import Master.MMBDevice;
import Master.Point.MMBCoil;
import Master.Point.MMBHoldingRegister;

public class ReadHoldingRegistersRequest extends Request{
	
	Vector<MMBHoldingRegister> listRegisters;
	short startAddress;
	short length;
	
	public ReadHoldingRegistersRequest(MMBChannel channel, MMBDevice device, Vector<MMBHoldingRegister> listRegisters, short startAddress, short length, int timeout) {
		super(channel, device, timeout, false);
		
		this.listRegisters = listRegisters;
		this.startAddress = startAddress;
		this.length = length;
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
