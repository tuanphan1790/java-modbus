package Master.Request;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Vector;
import java.util.zip.CRC32;

import Common.ModbusTypes;
import Common.ModbusUtilities;
import Master.MMBChannel;
import Master.MMBDevice;
import Master.OnCoilsChangeListener;
import Master.OnHoldingRegistersChangeListener;
import Master.Point.MMBCoil;

public class ReadCoilsRequest extends Request {
	
	Vector<MMBCoil> listCoils;
	public Boolean[] results;
	
	short startAddress;
	short length;
	
	byte expectedNumberOfDataBytes;
	
	private byte[] expectedHeaderLength;
	
			
	public ReadCoilsRequest(MMBChannel channel, MMBDevice device, Vector<MMBCoil> listCoils, short startAddress, short length, int timeout) 
	{
		super(channel, device, timeout, false);
		
		this.listCoils = listCoils;
		this.startAddress = startAddress;
		this.length = length;
		
		results = new Boolean[length];
	}
	
	private void PrepareRequest()
	{
		switch(this.modbusTypes)
		{
		case TCP:
			PrepareRequest_TCP();
			break;
		case RTU:
			PrepareRequest_RTU();
			break;
		default:
			break;
		}
	}
	
	private void PrepareRequest_RTU()
	{
		byte[] number;
        byte[] buffer = new byte[8];

        buffer[0] = this.device.getIdDevice();
        buffer[1] = ModbusUtilities.FcReadCoils;

        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(this.startAddress);
        number = b.array();

        buffer[2] = number[1];
        buffer[3] = number[0];

        b.putInt(this.length);
        number = b.array();

        buffer[4] = number[1];
        buffer[5] = number[0];

        CRC32 crc = new CRC32();
        crc.update(buffer, 0, 6);  
        
        b = ByteBuffer.allocate(2);
        b.putLong(crc.getValue());
        number = b.array();
        
        buffer[6] = number[0];
        buffer[7] = number[1];

        this.sendBuffer = buffer;
	}
	
	private void PrepareRequest_TCP()
	{
		 byte[] number;
         byte[] buffer = new byte[12];

         //Transact Id
         this.transacId = GetNextTransactId();
         buffer[0] = transacId[1];
         buffer[1] = transacId[0];

         //Protocol Id
         buffer[2] = 0;
         buffer[3] = 0;

         //Length
         buffer[4] = 0;
         buffer[5] = 6; // UnitId: 1, FCode: 1, StartAddress: 2, Length: 2

         //UnitId
         buffer[6] = this.device.getIdDevice();

         //FCode
         buffer[7] = ModbusUtilities.FcReadCoils;

         //Start address
         ByteBuffer b = ByteBuffer.allocate(2);
         b.putShort(this.startAddress);
         number = b.array();

         buffer[8] = number[0];
         buffer[9] = number[1];

         //Length
         b.clear();
         b.putShort(this.length);
         number = b.array();

         buffer[10] = number[0];
         buffer[11] = number[1];

         this.sendBuffer = buffer;
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
		this.modbusTypes = modbusType;
		
		this.PrepareRequest();
		this.PrepareResponse();
		
		return this.sendBuffer;
	}
	
	private void PrepareResponse()
	{
		this.expectedNumberOfDataBytes = (byte)Math.ceil(this.length / 8.0);
		
		switch(this.modbusTypes)
		{
		case TCP:
			PrepareResponse_TCP();
			break;
		case RTU:
			PrepareResponse_RTU();
			break;
		default:
			break;
		}
	}
	
	private void PrepareResponse_RTU()
	{
		this.ExpectResponseExceptionLength = 5;
        this.ExpectResponseFrameLength = 5 + this.expectedNumberOfDataBytes;
	}
	
	private void PrepareResponse_TCP()
	{
		this.ExpectResponseExceptionLength = 9;
        this.ExpectResponseFrameLength = 9 + this.expectedNumberOfDataBytes;
        
        int headerLength = this.ExpectResponseFrameLength - 6; 
        expectedHeaderLength = new byte[2];
        expectedHeaderLength[0] = (byte) (headerLength & 0xFF);
        expectedHeaderLength[1] = (byte)((headerLength >> 8) & 0xff);
	}

	@Override
	public void ProcessResponse(byte[] data, int length) {
		switch(modbusTypes)
		{
		case TCP:
			ProcessResponse_TCP(data, length);
			break;
		case RTU:
			ProcessResponse_RTU(data, length);
			break;
		}
		
	}
	
	private void ProcessResponse_RTU(byte[] data, int length)
	{
		if(length == ExpectResponseExceptionLength)
			ProcessResponse_RTU_Exception(data,length);
		else
			ProcessResponse_RTU_Data(data,length);
	}
	
	private void ProcessResponse_RTU_Exception(byte[] data, int length)
	{
		
	}
	
	private void ProcessResponse_RTU_Data(byte[] data, int length)
	{
					
	}
	
	private void ProcessResponse_TCP(byte[] data, int length)
	{
		if(length == ExpectResponseExceptionLength)
			ProcessResponse_TCP_Exception(data,length);
		else
			ProcessResponse_TCP_Data(data,length);
	}
	
	private void ProcessResponse_TCP_Exception(byte[] data, int length)
	{
		if (data[7] == ModbusUtilities.FcExceptionReadCoils)
        {
            if (data[0] != this.sendBuffer[0] || data[1] != this.sendBuffer[1])
            {
                this.responseResult = ResponseCodes.BadTransactId;
                return;
            }

            if (data[2] != 0 || data[3] != 0)
            {
                this.responseResult = ResponseCodes.BadProtocolId;
                return;
            }

            if (data[4] != 0 || data[5] != 3)
            {
                this.responseResult = ResponseCodes.BadHeaderLength;
                return;
            }

            if (data[6] != this.device.getIdDevice())
            {
                this.responseResult = ResponseCodes.BadDeviceId;
                return;
            }

            this.responseResult = ResponseCodes.ExceptionReturn;
            this.exceptionCode = data[8];
        }
        else
            this.responseResult = ResponseCodes.None;
	}
	
	private void ProcessResponse_TCP_Data(byte[] data, int length)
	{
		if (data[7] == ModbusUtilities.FcReadCoils)
        {
            if (data[0] != this.sendBuffer[0] || data[1] != this.sendBuffer[1])
            {
                this.responseResult = ResponseCodes.BadTransactId;
                return;
            }

            if (data[2] != 0 || data[3] != 0)
            {
                this.responseResult = ResponseCodes.BadProtocolId;
                return;
            }

            if (data[4] != this.expectedHeaderLength[1] || data[5] != this.expectedHeaderLength[0])
            {
                this.responseResult = ResponseCodes.BadHeaderLength;
                return;
            }

            if (data[6] != this.device.getIdDevice())
            {
                this.responseResult = ResponseCodes.BadDeviceId;
                return;
            }

            if (data[8] != this.expectedNumberOfDataBytes)
            {
                this.responseResult = ResponseCodes.BadData;
                return;
            }

            this.responseResult = ResponseCodes.Good;
            this.ProcessResponse_Data(data, 9);
        }
        else
            this.responseResult = ResponseCodes.BadFunctionCode;
	}
	
	private void ProcessResponse_Data(byte[] data, int index)
	{
		byte[] buffers = Arrays.copyOfRange(data, index, data.length);
		int lengthResult = listCoils.size();
		int count =0;
		
		for(byte buf : buffers)
		{
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x01) != 0);
				count++;			
			}
			else
				break;
			
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x02) != 0);
				count++;			
			}
			else
				break;
			
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x04) != 0);
				count++;			
			}
			else
				break;
			
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x08) != 0);
				count++;			
			}
			else
				break;
			
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x10) != 0);
				count++;			
			}
			else
				break;
			
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x20) != 0);
				count++;			
			}
			else
				break;
			
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x40) != 0);
				count++;			
			}
			else
				break;
			
			if(count < lengthResult)
			{
				results[count] = ((buf & 0x80) != 0);
				count++;			
			}
			else
				break;
			
		}
	}

	@Override
	protected void FireEvent_Exception() {
		
	}

	@Override
	protected void FireEvent_Good() {
		int count = 0;
		for(MMBCoil coil: listCoils)
		{
			if(coil.UpdateValue(results[count]))
			{
				this.device.OnCoilChange(coil);
			}
			count++;
		}
	}
}
