package Master.Point;

import Master.MMBDevice;

public class MMBHoldingRegisterFloat extends MMBHoldingRegister  {

	private Boolean firstRegister;
	
	public MMBHoldingRegisterFloat(MMBDevice device, short address, byte addressLength) {
		super(device, address, addressLength);
		// TODO Auto-generated constructor stub
	}

	public Boolean UpdateValue(float val)
	{
		if(value!= val)
		{
			value = val;
			return true;
		}
		
		return false;
	}
	
	float value;

	public float getValue() {
		return value;
	}
}
