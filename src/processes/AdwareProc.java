package processes;

import main.Main;
import main.User;

public class AdwareProc extends Proc {

	public User owner;
	public long startTime, lastTime;
	
	public AdwareProc(String par1Name, int par2Version) {
		super(par1Name, par2Version);
		owner = Main.loadedUser;
		startTime = (System.currentTimeMillis()/1000);
		lastTime = startTime;
	}
	
	public void update() {
		owner.balance += (int)((System.currentTimeMillis()/1000)-lastTime);
		lastTime=(int)(System.currentTimeMillis()/1000);
	}
	
}
