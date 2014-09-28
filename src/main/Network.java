package main;

import java.util.ArrayList;

import processes.Proc;

import software.Program;

public class Network {
	
	public static final int baseCpu = 50;
	public static final int baseStorage = 2;
	public static final int baseMemory = 512;
	public static final int baseBandwidth = 50;

	public int cpu, storage, memory, bandwidth;
	public String name, IP;
	
	public ArrayList<Program> files = new ArrayList<Program>();
	public ArrayList<Proc> processes = new ArrayList<Proc>();
	
	public Network(int par1Cpu, int par2Storage, int par3Memory, int par4Bandwidth, String par5Name, String par6IP) {
		cpu = par1Cpu;
		storage = par2Storage;
		memory = par3Memory;
		bandwidth = par4Bandwidth;
		name = par5Name;
		IP = par6IP;
		if ((name.equals("loadedNet"))==false) Main.rootNet.add(this);
	}
	public Network(String par1Name, String par2IP) {
		this(baseCpu, baseStorage, baseMemory, baseBandwidth, par1Name, par2IP);
	}
	public void run(Program prog) {
		prog.createProcess(this);
	}
	
}
