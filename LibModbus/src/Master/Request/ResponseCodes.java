package Master.Request;

public enum ResponseCodes {
	None,
    BadCRC,
    BadFunctionCode,
    BadDeviceId,
    BadAddress,
    BadData,
    BadTransactId,
    BadProtocolId,
    BadHeaderLength,
    ExceptionReturn,
    Good
}
