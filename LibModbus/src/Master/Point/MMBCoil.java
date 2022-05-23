package Master.Point;

import Master.MMBDevice;;

public class MMBCoil {
	
	private MMBDevice device;
	short address;
			
	public short getAddress() {
		return address;
	}

	public MMBCoil(MMBDevice device, short address) {
		this.device = device;
		this.address = address;
		
		value = false;
	}
	
	public Boolean UpdateValue(Boolean val)
	{
		if(value!= val)
		{
			value = val;
			return true;
		}
		
		return false;
	}
	
	Boolean value;

	public Boolean getValue() {
		return value;
	}
	
}
