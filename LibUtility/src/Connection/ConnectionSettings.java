package Connection;

public class ConnectionSettings {

	public ConnectionSettings(SerialSettings serialSettings)
	{
		this.serialSettings = serialSettings;
	}
	
	public ConnectionSettings(TcpClientSettings tcpClientSettings) {
		this.clientSettings = tcpClientSettings;
	}
	
	public ConnectionSettings(TcpServerSettings tcpServerSettings) {
		this.serverSettings = tcpServerSettings;
	}
	
	public SerialSettings getSerialSettings() {
		return serialSettings;
	}

	public TcpClientSettings getClientSettings() {
		return clientSettings;
	}

	public TcpServerSettings getServerSettings() {
		return serverSettings;
	}

	private ConnectionType connectionType;
	
	private SerialSettings serialSettings;
	private TcpClientSettings clientSettings;
	private TcpServerSettings serverSettings;
	
	
	
}
