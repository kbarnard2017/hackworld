package processes;

public class Proc {

	public String name;
	public int pid;
	public int version;
	
	public Proc(String par1Name, int par2Version) {
		name = par1Name;
		pid = (int)(Math.random()*100000);
		version = par2Version;
	}
	
	public void update() {
		// Does nothing by default
	}
	
}
