package Master.Point;

import Master.MMBDevice;

public class MMBHoldingRegisterInt16 extends MMBHoldingRegister {

	public MMBHoldingRegisterInt16(MMBDevice device, short address, byte addressLength) {
		super(device, address, addressLength);
		// TODO Auto-generated constructor stub
	}
	
	public Boolean UpdateValue(short val)
	{
		if(value!= val)
		{
			value = val;
			return true;
		}
		
		return false;
	}
	
	short value;

	public short getValue() {
		return value;
	}

}
