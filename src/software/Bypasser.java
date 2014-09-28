package software;

import processes.BypasserProc;
import main.Network;

public class Bypasser extends Program {

	public Bypasser(String par1Name) {
		super(par1Name);
	}
	
	public void createProcess(Network net) {
		net.processes.add(new BypasserProc(name, version));
	}

}
