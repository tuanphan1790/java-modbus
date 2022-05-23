package Master;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import Master.Point.MMBCoil;
import Master.Point.MMBDiscreteInput;
import Master.Point.MMBHoldingRegister;
import Master.Point.MMBInputRegister;
import Master.Request.ReadCoilsRequest;
import Master.Request.ReadDiscreteInputsRequest;
import Master.Request.ReadHoldingRegistersRequest;
import Master.Request.ReadInputRegistersRequest;
import Master.Request.Request;

public class MMBDevice {
	
	private byte idDevice;
	
	Object lock = new Object();
	
	final int DEFAULT_TIMEOUT = 3000;
	
	private MMBChannel parent;
	
	private Boolean started;
	
	int intervalReadCoil;
	int intervalReadDiscreteInput;
	int intervalReadHoldingRegister;
	int intervalReadInputRegister;
	
	public int getIntervalReadCoil() {
		return intervalReadCoil;
	}


	public void setIntervalReadCoil(int intervalReadCoil) {
		this.intervalReadCoil = intervalReadCoil;
	}


	public int getIntervalReadDiscreteInput() {
		return intervalReadDiscreteInput;
	}


	public void setIntervalReadDiscreteInput(int intervalReadDiscreteInput) {
		this.intervalReadDiscreteInput = intervalReadDiscreteInput;
	}


	public int getIntervalReadHoldingRegister() {
		return intervalReadHoldingRegister;
	}


	public void setIntervalReadHoldingRegister(int intervalReadHoldingRegister) {
		this.intervalReadHoldingRegister = intervalReadHoldingRegister;
	}


	public int getIntervalReadInputRegister() {
		return intervalReadInputRegister;
	}


	public void setIntervalReadInputRegister(int intervalReadInputRegister) {
		this.intervalReadInputRegister = intervalReadInputRegister;
	}
	
	Boolean readCoilFlag;
	Vector<MMBCoil> listCoils;
	Timer timerReadCoil;
	TimerTask taskReadCoil;
	

	class TimerReadCoild extends TimerTask {
		@Override
		public void run() {
			synchronized (lock) {
				readCoilFlag = true;
			}
		}
	}
	
	Boolean readDiscreateFlag;
	Vector<MMBDiscreteInput> listDiscreteInputs;
	Timer timerReadDiscreteInput;
	TimerTask taskReadDiscreteInput;

	class TimerReadDiscreteInput extends TimerTask {
		@Override
		public void run() {
			synchronized (lock) {
				readDiscreateFlag = true;
			}
		}
	}
	
	Boolean readHoldingRegisterFlag;
	Vector<MMBHoldingRegister> listHoldingRegisters;
	Timer timerReadHoldingRegisters;
	TimerTask taskReadHoldingRegisters;

	class TimerReadHoldingRegisters extends TimerTask {
		@Override
		public void run() {
			synchronized (lock) {
				readHoldingRegisterFlag = true;
			}
		}
	}
	
	Boolean readInputRegister;
	Vector<MMBInputRegister> listInputRegisters;
	Timer timerReadInputRegisters;
	TimerTask taskReadInputRegisters;

	class TimerReadInputRegisters extends TimerTask {
		@Override
		public void run() {
			synchronized (lock) {
				readInputRegister = true;
			}
		}
	}
	
	private int maxCoilPerRequest;
	private int maxDiscreteInputPerRequest;
	private int maxHoldingRegisterRequest;
	private int maxInputRegisterRequest;
	
	private Vector<ReadCoilsRequest> listReadCoilRequests;
	private Vector<ReadDiscreteInputsRequest> listReadDiscreteInputRequest;
	private Vector<ReadHoldingRegistersRequest> listReadHoldingRegisterRequests;
	private Vector<ReadInputRegistersRequest> listReadInputRegisterRequests;
	
	OnCoilsChangeListener coilChangeListener;
	OnHoldingRegistersChangeListener holdingRegisterChangeListener;
	
	public void SetOnCoilChangeListener(OnCoilsChangeListener listener)
	{
		coilChangeListener = listener;
	}
	
	public void OnCoilChange(MMBCoil coil)
	{
		if(coilChangeListener != null)
		{
			coilChangeListener.OnCoilChange(coil);
		}
	}
	
	public void SetOnHoldingRegistersChangeListener(OnHoldingRegistersChangeListener listener)
	{
		holdingRegisterChangeListener = listener;
	}
	
	
	
	public int getMaxCoilPerRequest() {
		return maxCoilPerRequest;
	}


	public void setMaxCoilPerRequest(int maxCoilPerRequest) {
		this.maxCoilPerRequest = maxCoilPerRequest;
	}


	public int getMaxDiscreteInputPerRequest() {
		return maxDiscreteInputPerRequest;
	}


	public void setMaxDiscreteInputPerRequest(int maxDiscreteInputPerRequest) {
		this.maxDiscreteInputPerRequest = maxDiscreteInputPerRequest;
	}


	public int getMaxHoldingRegisterRequest() {
		return maxHoldingRegisterRequest;
	}


	public void setMaxHoldingRegisterRequest(int maxHoldingRegisterRequest) {
		this.maxHoldingRegisterRequest = maxHoldingRegisterRequest;
	}


	public int getMaxInputRegisterRequest() {
		return maxInputRegisterRequest;
	}


	public void setMaxInputRegisterRequest(int maxInputRegisterRequest) {
		this.maxInputRegisterRequest = maxInputRegisterRequest;
	}


	public byte getIdDevice() {
		return idDevice;
	}


	public MMBChannel getParent() {
		return parent;
	}


	public MMBDevice(MMBChannel channel, byte id) {
		parent = channel;
		idDevice = id;
		
		started = false;
		
		readCoilFlag = false;
		listCoils = new Vector<MMBCoil>();
		
		readHoldingRegisterFlag = false;
		listHoldingRegisters = new Vector<MMBHoldingRegister>();
		
		readDiscreateFlag = false;
		listDiscreteInputs = new Vector<MMBDiscreteInput>();
		
		readInputRegister = false;
		listInputRegisters = new Vector<MMBInputRegister>();
		
		maxCoilPerRequest = 20;
		maxDiscreteInputPerRequest = 20;
		maxHoldingRegisterRequest = 20;
		maxInputRegisterRequest = 20;
	}
	
	public void Start()
	{
		synchronized (lock) {
			if(started)
				return;
			
			started = true;
			
			this.BuildReadCoilRequest();
			//this.BuildReadHoldingRegisterRequest();
			
			this.timerReadCoil = new Timer();
			this.taskReadCoil = new TimerReadCoild();
			this.timerReadCoil.schedule(taskReadCoil, intervalReadCoil, intervalReadCoil);
			
//			this.timerReadHoldingRegisters = new Timer();
//			this.taskReadHoldingRegisters = new TimerReadHoldingRegisters();
//			this.timerReadCoil.schedule(taskReadHoldingRegisters, intervalReadHoldingRegister);			
		}
	}
	
	public void Stop()
	{
		this.timerReadCoil.cancel();
		
		started = false;
		
		readCoilFlag = false;
		readDiscreateFlag = false;
		readHoldingRegisterFlag = false;
		readInputRegister = false;
	}
	
	
	public MMBCoil AddCoil(short address) throws Exception {
		synchronized (lock) {
			
			for(MMBCoil c : listCoils)
			{
				if(c.getAddress() == address)
					throw new Exception();
			}
			
			MMBCoil coil = new MMBCoil(this, address);
			
			listCoils.add(coil);
			
			this.SortCoils();
			
			this.BuildReadCoilRequest();
			
			return coil;
		}
	}
	
	public void RemoveCoil(short address) {
		synchronized (lock)
		{
			for(int i =0; i< listCoils.size(); i++)
			{
				if(listCoils.get(i).getAddress() == address)
				{
					listCoils.remove(i);
					return;
				}
			}
		}
	}
	
	private void SortCoils() {
		listCoils.sort(new Comparator<MMBCoil>() {
			public int compare(MMBCoil o1, MMBCoil o2) {
				if(o1.getAddress()> o2.getAddress())
					return 1;
				else
					return -1;
			};
		});	
	}
	
	private void BuildReadCoilRequest()
	{
		ReadCoilsRequest request;
		Vector<MMBCoil> coils = null;
		
		Boolean newReq = true;
		short startAddress = 0;
		short length = 0;
		
		this.listReadCoilRequests =  new Vector<ReadCoilsRequest>();
		
		for(MMBCoil coil : listCoils)
		{
			if(newReq)
			{
				coils = new Vector<MMBCoil>();
				coils.add(coil);
				
				startAddress = coil.getAddress();
				length = (short) (length +1);
				
				newReq = false;
				continue;
			}
			else
			{
				if(coil.getAddress() == startAddress +length)
				{
					if(coils.size() > maxCoilPerRequest)
					{
						request = new ReadCoilsRequest(parent, this, coils, startAddress, length, DEFAULT_TIMEOUT);
						listReadCoilRequests.add(request);
						
						newReq = true;
						length = 0;
						startAddress = 0;
					}
					else
					{
						coils.add(coil);
						length=(short) (length+1);
						
						if(listCoils.lastElement() == coil)
						{
							request = new ReadCoilsRequest(parent, this, coils, startAddress, length, DEFAULT_TIMEOUT);
							listReadCoilRequests.add(request);
						}
					}					
				}
				else
				{
					request = new ReadCoilsRequest(parent, this, coils, startAddress, length, DEFAULT_TIMEOUT);
					listReadCoilRequests.add(request);
					
					newReq = true;
					length = 0;
					startAddress = 0;
				}
			}
		}
	}
	
	public MMBHoldingRegister AddHoldingRegister(short address, byte length) throws Exception
	{
		synchronized (lock) {
			
			for(MMBHoldingRegister c : listHoldingRegisters)
			{
				if(c.getAddress() == address)
					throw new Exception();
			}
			
			MMBHoldingRegister holdingRegister = new MMBHoldingRegister(this, address, length);
			
			listHoldingRegisters.add(holdingRegister);
			
			this.SortHoldingRegister();
			
			this.BuildReadHoldingRegisterRequest();
			
			return holdingRegister;
		}
	}
	
	private void SortHoldingRegister()
	{
		listHoldingRegisters.sort(new Comparator<MMBHoldingRegister>() {
			public int compare(MMBHoldingRegister o1, MMBHoldingRegister o2) {
				if(o1.getAddress()> o2.getAddress())
					return 1;
				else
					return -1;
			};
		});	
	}
	
	private void BuildReadHoldingRegisterRequest()
	{
		ReadHoldingRegistersRequest request;
		Vector<MMBHoldingRegister> holdRegisters = null;
		
		Boolean newReq = true;
		short startAddress = 0;
		short length = 0;
		
		this.listReadHoldingRegisterRequests = new Vector<ReadHoldingRegistersRequest>();
		
		for(MMBHoldingRegister register : listHoldingRegisters)
		{
			if(newReq)
			{
				holdRegisters = new Vector<MMBHoldingRegister>();
				holdRegisters.add(register);
				
				newReq = false;
				startAddress = register.getAddress();
				length = (short) (length + register.getAddressLength());
			}
			else
			{
				if(register.getAddress() == startAddress + length)
				{
					if(holdRegisters.size() > maxHoldingRegisterRequest)
					{
						request = new ReadHoldingRegistersRequest(parent, this, holdRegisters, startAddress, length, DEFAULT_TIMEOUT);
						listReadHoldingRegisterRequests.add(request);
						
						startAddress = 0;
						length = 0;
						newReq = true;
					}
					else
					{
						length = (short) (length + register.getAddressLength());
						holdRegisters.add(register);
						
						if(listHoldingRegisters.lastElement() == register)
						{
							request = new ReadHoldingRegistersRequest(parent, this, holdRegisters, startAddress, length, DEFAULT_TIMEOUT);
							listReadHoldingRegisterRequests.add(request);
						}
					}					
				}
				else
				{
					request = new ReadHoldingRegistersRequest(parent, this, holdRegisters, startAddress, length, DEFAULT_TIMEOUT);
					listReadHoldingRegisterRequests.add(request);
					
					startAddress = 0;
					length = 0;
					newReq = true;					
				}
			}
		}
	}
	
	public Vector<Request> GetRequests() {
		Vector<Request> requests = new Vector<Request>();
		
		synchronized (lock) {
			if(readCoilFlag)
			{
				readCoilFlag = false;
				requests.addAll(listReadCoilRequests);
			}
			
			if(readHoldingRegisterFlag)
			{
				readHoldingRegisterFlag = false;
				requests.addAll(listReadHoldingRegisterRequests);
			}
		}
		
		return requests;
	}
		
}
