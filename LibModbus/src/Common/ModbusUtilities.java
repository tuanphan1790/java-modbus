package Common;

import java.util.zip.CRC32;

public class ModbusUtilities {
	public final static byte FcReadCoils = 0x01;
    public final static byte FcReadDiscreteInputs = 0x02;
    public final static byte FcReadHoldingRegisters = 0x03;
    public final static byte FcReadInputRegisters = 0x04;

    public final static byte FcForceSingleCoil = 0x05;
    public final static byte FcPresetSingleRegister = 0x06;
    public final static byte FcForceMultipleCoils = 0x0F;
    public final static byte FcPresetMultipleRegisters = 0x10;

    public final static byte FcExceptionReadCoils = (byte) 0x81;
    public final static byte FcExceptionReadDiscreteInputs = (byte) 0x82;
    public final static byte FcExceptionReadHoldingRegisters = (byte) 0x83;
    public final static byte FcExceptionReadInputRegisters = (byte) 0x84;

    public final static byte FcExceptionForceSingleCoil = (byte) 0x85;
    public final static byte FcExceptionPresetSingleRegister = (byte) 0x86;
    public final static byte FcExceptionForceMultipleCoils = (byte) 0x8F;
    public final static byte FcExceptionPresetMultipleRegisters = (byte) 0x90;

    public final static byte ExCode01IllegalFunction = 0x01;
    public final static byte ExCode02IllegalDataAddress = 0x02;
    public final static byte ExCode03IllegalDataValue = 0x03;
    public final static byte ExCode04SlaveDeviceFailure = 0x04;
    public final static byte ExCode05Acknowledge = 0x05;
    public final static byte ExCode06SlaveDeviceBusy = 0x06;
    public final static byte ExCode07NegativeAcknowledge = 0x07;
    public final static byte ExCode08MemoryParityError = 0x08;

    public final static byte ExCode10GatewayPathUnavailable = 0x0A;
    public final static byte ExCode11GatewayTargetDeviceFailedtoRespond = 0x0B;
    
    
}
