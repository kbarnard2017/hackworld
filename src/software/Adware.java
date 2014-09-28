package software;

import processes.AdwareProc;
import main.Network;

public class Adware extends Program {

	public Adware(String par1Name) {
		super(par1Name);
	}
	
	public void createProcess(Network net){
		net.processes.add(new AdwareProc(name, version));
	}

}
