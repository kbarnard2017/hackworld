package software;

import processes.Proc;
import main.Network;

public class Program {

	public String name;
	public int id;
	public int version;
	
	public Program(String par1Name) {
		version = 1;
		name = par1Name + " v" + version;
		id = (int)(Math.random()*100000);
	}
	
	public void createProcess(Network net) {
		net.processes.add(new Proc(name, version));
	}
	
	public void setVersion(int v) {this.version = v;}
	
}
