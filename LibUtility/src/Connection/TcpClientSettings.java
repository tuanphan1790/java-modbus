package Connection;

public class TcpClientSettings {

	String address;
	int port;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public TcpClientSettings(String address, int port)
	{
		this.address = address;
		this.port = port;
	}
}
