package Master.Point;

import Master.MMBDevice;

public class MMBHoldingRegister {
	
	private MMBDevice device;
	private short address;
	private Boolean bigEndian;
	
	public Boolean getBigEndian() {
		return bigEndian;
	}

	public void setBigEndian(Boolean bigEndian) {
		this.bigEndian = bigEndian;
	}

	public short getAddress() {
		return address;
	}

	public void setAddress(short address) {
		this.address = address;
	}

	private byte addressLength;
	
	public byte getAddressLength() {
		return addressLength;
	}

	public void setAddressLength(byte addressLength) {
		this.addressLength = addressLength;
	}

	public MMBHoldingRegister(MMBDevice device, short address, byte addressLength) {
		this.device = device;
		this.address = address;
		this.addressLength = addressLength;
	}
}
