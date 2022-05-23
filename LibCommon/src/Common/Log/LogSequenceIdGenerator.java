package Common.Log;

public class LogSequenceIdGenerator 
{
	private static int CurrentSequenceId = 0;
	
	public static int GenerateLogSequenceId() 
	{
		CurrentSequenceId = CurrentSequenceId +1;
		
		return CurrentSequenceId;
	}
}
