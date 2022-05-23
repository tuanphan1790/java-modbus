package Common.Log;

import java.time.LocalTime;

public class LogEntry {
	
	LocalTime Time;
	String Log;
	int SequenceId;
	
	public LogEntry(String log) {
		Log = log;
		Time = LocalTime.now();
		SequenceId = LogSequenceIdGenerator.GenerateLogSequenceId();
	}
}
