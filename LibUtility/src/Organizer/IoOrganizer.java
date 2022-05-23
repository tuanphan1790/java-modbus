package Organizer;

import java.util.Vector;

public class IoOrganizer {
	
	Vector<IoSubOrganizer> ListIoSubs;
	
	public IoOrganizer() {
		ListIoSubs = new Vector<IoSubOrganizer>();
	}
	
	public void AddSubOrganizer(IoSubOrganizer sub) {
		ListIoSubs.add(sub);
	}
	
	public void RemoveSubOrganizer(IoSubOrganizer sub) {
		for (IoSubOrganizer ioSubOrganizer : ListIoSubs) {
			if(sub == ioSubOrganizer)
			{
				
			}
		}
	}

}
