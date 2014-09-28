package software;

import main.Network;
import processes.FirewallProc;


public class Firewall extends Program {
	
	public Firewall(String par1Name) {
		super(par1Name);
	}
	
	public void createProcess(Network net){
		net.processes.add(new FirewallProc(name, version));
	}

}
