package Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import javax.comm.*;

public class SerialSettings extends SerialPort{

	@Override
	public void addEventListener(SerialPortEventListener arg0) throws TooManyListenersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBaudRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDataBits() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFlowControlMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getParity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStopBits() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCD() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCTS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDSR() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDTR() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRI() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRTS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notifyOnBreakInterrupt(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnCTS(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnCarrierDetect(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnDSR(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnDataAvailable(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnFramingError(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnOutputEmpty(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnOverrunError(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnParityError(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOnRingIndicator(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEventListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBreak(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDTR(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFlowControlMode(int arg0) throws UnsupportedCommOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRTS(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSerialPortParams(int arg0, int arg1, int arg2, int arg3) throws UnsupportedCommOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableReceiveFraming() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableReceiveThreshold() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableReceiveTimeout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableReceiveFraming(int arg0) throws UnsupportedCommOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableReceiveThreshold(int arg0) throws UnsupportedCommOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableReceiveTimeout(int arg0) throws UnsupportedCommOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInputBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOutputBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getReceiveFramingByte() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReceiveThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReceiveTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isReceiveFramingEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReceiveThresholdEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReceiveTimeoutEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInputBufferSize(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOutputBufferSize(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
