package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import processes.*;
import software.*;

public class Main {

	public static ArrayList<Network> rootNet = new ArrayList<Network>();
	public static ArrayList<User> users = new ArrayList<User>();
	
	public static User loadedUser = new User("loadedUser", "");
	public static Network loadedNet = new Network("loadedNet", "");
	
	public static final String userFilePath = getUserPath();
	public static final String netFilePath = getNetPath();
	
	public static void main(String[] args) {
		
		setup();

	}
	
	public static void setup() {
		
		boot();
		
	}
	
	public static void boot() {
		loadUsers();
		loadNets();
		boolean bKill = false;
		Scanner scan = new Scanner(System.in);
		while (bKill == false) {
			System.out.println("Login or Create new account.");
			System.out.print("# ");
			String type = scan.nextLine();
			type=type.toLowerCase();
			if (type.equals("login")) {
				boolean lKill = false;
				while (lKill==false) {
					boolean kill = false;
					System.out.print("Username: ");
					String userTry = scan.nextLine();
					System.out.print("Password: ");
					String passTry = scan.nextLine();
					boolean lToken = false;
					for (int i = 0; i<users.size(); i++) {
						if (userTry.equals(users.get(i).username) && passTry.equals(users.get(i).password)) {
							loadedUser = users.get(i);
							lToken = true;
						}
					}
					if (lToken==true) {
						System.out.println("Welcome to the Hack World! Type 'starthelp' to get you started.");
						while (kill==false) {
							System.out.print(">");
							String comm = scan.nextLine();
							String[] commArgs = comm.split(" ");
							comm=comm.toLowerCase();
							
							// Update processes
							updateProcs();
							
							// Begin command processing
							if (commArgs[0].equals("help")) {
								System.out.println("List of commands:");
								System.out.println("  help - Displays this help screen");
								System.out.println("  hardware - Shows hardware of the local system");
								System.out.println("  files - Shows files on the local system");
								System.out.println("  processes - Shows processes on the local system");
								System.out.println("  balance - Displays your current balance");
								System.out.println("  database - Shows a list of acquried IPs");
								System.out.println("  connect <ip> - Connects to a certain IP");
								System.out.println("  rlogin - Accesses the connected network's terminal");
								System.out.println("  download <program> - Downloads a program");
								System.out.println("  upload <id> - Uploads a program");
								System.out.println("  run <id> - Runs a program");
								System.out.println("  logout - Logs out");
							}
							else if (commArgs[0].equals("starthelp")) {
								System.out.println("This is the start help!\nYou can come back here any time by typing 'starthelp'\nTo get you started, type 'help' for a list of commands.");
							}
							else if (commArgs[0].equals("hardware")) {
								System.out.println("IP: "+loadedUser.net.IP+"\nCPU: "+loadedUser.net.cpu+" Mhz\nStorage: "+loadedUser.net.storage+" GB\nMemory: "+loadedUser.net.memory+" MB\nBandwidth: "+loadedUser.net.bandwidth+" kb/s");
							}
							else if (commArgs[0].equals("files")) {
								System.out.println("Files:");
								for (int i = 0; i<loadedUser.net.files.size(); i++) {
									System.out.println("  "+loadedUser.net.files.get(i).name+" - "+loadedUser.net.files.get(i).id);
								}
							}
							else if (commArgs[0].equals("processes")) {
								System.out.println("Processes:");
								for (int i = 0; i<loadedUser.net.processes.size(); i++) {
									System.out.println("  "+loadedUser.net.processes.get(i).name+" - "+loadedUser.net.processes.get(i).pid);
								}
							}
							else if (commArgs[0].equals("database")) {
								System.out.println("Database:");
								for (int i = 0; i<loadedUser.database.size(); i++) {
									System.out.println("  "+loadedUser.database.get(i).name+" - "+loadedUser.database.get(i).IP);
								}
							}
							else if (commArgs[0].equals("balance")) {
								System.out.println("Balance: $"+loadedUser.balance);
							}
							else if (commArgs[0].equals("connect")) {
								if (commArgs[1]!=null) {
									loadedNet = lookup(commArgs[1]);
									boolean inDatabase = false;
									for (int i = 0; i<loadedUser.database.size(); i++) {
										if (commArgs[1].equals(loadedUser.database.get(i))) inDatabase = true;
									}
									if (inDatabase==false) loadedUser.database.add(loadedNet);
									System.out.println("Connected to "+loadedNet.IP);
								}
								else {
									System.out.print("IP: ");
									String conIP = scan.nextLine();
									loadedNet = lookup(conIP);
									System.out.println("Connected to "+loadedNet.IP);
								}
							}
							else if (commArgs[0].equals("rlogin")) {
								boolean hasFWP = false;
								FirewallProc fwp = null;
								for (int i = 0; i<loadedNet.processes.size(); i++) {
									if (loadedNet.processes.get(i) instanceof FirewallProc) {
										hasFWP = true;
										fwp = (FirewallProc)loadedNet.processes.get(i);
									}
								}
								if (hasFWP) {
									boolean hasBypasser = false;
									BypasserProc bp = null;
									for (int i = 0; i<loadedUser.net.processes.size(); i++) {
										if (loadedUser.net.processes.get(i) instanceof BypasserProc) {
											hasBypasser = true;
											bp = (BypasserProc)loadedUser.net.processes.get(i);
										}
									}
									if (hasBypasser) {
										if (bp.version>=fwp.version) {
											System.out.println("System bypassed using bypasser v"+bp.version+"...");
											boolean rKill = false;
											while (rKill==false) {
												System.out.print(">"+loadedNet.IP+">");
												String rComm = scan.nextLine();
												String[] rCommArgs = rComm.split(" ");
												
												if (rCommArgs[0].equals("logout")) {
													System.out.println("Logging out...");
													rKill=true;
												}
												else if (rCommArgs[0].equals("help")) {
													System.out.println("List of commands:");
													System.out.println("  help - Displays this help screen");
													System.out.println("  logout - Logs out");
													System.out.println("  hardware - Shows hardware on the network");
													System.out.println("  files - Shows files on the network");
													System.out.println("  processes - Shows processes on the network");
												}
												else if (rCommArgs[0].equals("hardware")) {
													System.out.println("CPU: "+loadedNet.cpu+" Mhz\nStorage: "+loadedNet.storage+" GB\nMemory: "+loadedNet.memory+" MB\nBandwidth: "+loadedNet.bandwidth+" kb/s");
												}
												else if (rCommArgs[0].equals("files")) {
													System.out.println("Files:");
													for (int i = 0; i<loadedNet.files.size(); i++) {
														System.out.println("  "+loadedNet.files.get(i).name+" - "+loadedNet.files.get(i).id);
													}
												}
												else if (rCommArgs[0].equals("processes")) {
													System.out.println("Processes:");
													for (int i = 0; i<loadedNet.processes.size(); i++) {
														System.out.println("  "+loadedNet.processes.get(i).name+" - "+loadedNet.processes.get(i).pid);
													}
												}
												else if (rCommArgs[0].equals("run")) {
													for (int i = 0; i<loadedNet.files.size(); i++) {
														if (rCommArgs[1].equals(Integer.toString(loadedNet.files.get(i).id))) {
															loadedNet.run(loadedNet.files.get(i));
															System.out.println("Running "+loadedNet.files.get(i).name+"...");
														}
													}
												}
											}
										}
										else System.out.println("Bypass software too low!");
									}
									else System.out.println("No running bypass software!");
								}
								else {
									boolean rKill = false;
									while (rKill==false) {
										System.out.print(">"+loadedNet.IP+">");
										String rComm = scan.nextLine();
										String[] rCommArgs = rComm.split(" ");
										
										if (rCommArgs[0].equals("logout")) {
											System.out.println("Logging out...");
											rKill=true;
										}
										else if (rCommArgs[0].equals("help")) {
											System.out.println("List of commands:");
											System.out.println("  help - Displays this help screen");
											System.out.println("  logout - Logs out");
											System.out.println("  hardware - Shows hardware on the network");
											System.out.println("  files - Shows files on the network");
											System.out.println("  processes - Shows processes on the network");
										}
										else if (rCommArgs[0].equals("hardware")) {
											System.out.println("CPU: "+loadedNet.cpu+" Mhz\nStorage: "+loadedNet.storage+" GB\nMemory: "+loadedNet.memory+" MB\nBandwidth: "+loadedNet.bandwidth+" kb/s");
										}
										else if (rCommArgs[0].equals("files")) {
											System.out.println("Files:");
											for (int i = 0; i<loadedNet.files.size(); i++) {
												System.out.println("  "+loadedNet.files.get(i).name+" - "+loadedNet.files.get(i).id);
											}
										}
										else if (rCommArgs[0].equals("processes")) {
											System.out.println("Processes:");
											for (int i = 0; i<loadedNet.processes.size(); i++) {
												System.out.println("  "+loadedNet.processes.get(i).name+" - "+loadedNet.processes.get(i).pid);
											}
										}
										else if (rCommArgs[0].equals("run")) {
											for (int i = 0; i<loadedNet.files.size(); i++) {
												if (rCommArgs[1].equals(Integer.toString(loadedNet.files.get(i).id))) {
													loadedNet.run(loadedNet.files.get(i));
													System.out.println("Running "+loadedNet.files.get(i).name+"...");
												}
											}
										}
									}
								}
							}
							else if (commArgs[0].equals("download")) {
								if (commArgs[1].equals("firewall")) loadedUser.net.files.add(new Firewall("Firewall"));
								if (commArgs[1].equals("adware")) loadedUser.net.files.add(new Adware("Adware"));
								if (commArgs[1].equals("bypasser")) loadedUser.net.files.add(new Bypasser("Bypasser"));
							}
							else if (commArgs[0].equals("upload")) {
								for (int i = 0; i<loadedUser.net.files.size(); i++) {
									if (commArgs[1].equals(Integer.toString(loadedUser.net.files.get(i).id))) {
										loadedNet.files.add(loadedUser.net.files.get(i));
									}
								}
							}
							else if (commArgs[0].equals("run")) {
								for (int i = 0; i<loadedUser.net.files.size(); i++) {
									if (commArgs[1].equals(Integer.toString(loadedUser.net.files.get(i).id))) {
										loadedUser.net.run(loadedUser.net.files.get(i));
										System.out.println("Running "+loadedUser.net.files.get(i).name+"...");
									}
								}
							}
							else if (commArgs[0].equals("buy")) {
								System.out.println("Establishing secure connection to dealer...");
								System.out.println("What would you like to buy?");
								System.out.println("  Hardware\n  Software");
								System.out.print(">buy>");
								String obj = scan.nextLine();
								obj = obj.toLowerCase();
								if (obj.equals("hardware")) {
									System.out.println("What type of hardware?");
									System.out.println("  CPU\n  Storage\n  RAM\n  Bandwidth");
									System.out.print(">buy>hardware>");
									String hardType = scan.nextLine();
									hardType = hardType.toLowerCase();
									System.out.println("How much?");
									System.out.println("  50 MHz CPU - $300\n  1 GB Storage - $20\n  512 MB RAM - $50\n  1 kb/s Bandwidth - $200");
									System.out.print(">buy>hardware>"+hardType+">");
									String amtString = scan.nextLine();
									int amount = Integer.parseInt(amtString);
									updateProcs();
									if (hardType.equals("cpu") && loadedUser.balance>=300*amount) {
										loadedUser.net.cpu+=amount*50;
										loadedUser.balance-=amount*300;
										System.out.println("Successfully purchased "+amount*50+" MHz CPU. Your new CPU is "+loadedUser.net.cpu+" MHz.");
									}
									else if (hardType.equals("storage") && loadedUser.balance>=20*amount) {
										loadedUser.net.storage+=amount;
										loadedUser.balance-=amount*20;
										System.out.println("Successfully purchased "+amount+" GB storage. Your new storage is "+loadedUser.net.storage+" GB.");
									}
									else if (hardType.equals("ram") && loadedUser.balance>=50*amount) {
										loadedUser.net.memory+=amount*512;
										loadedUser.balance-=amount*50;
										System.out.println("Successfully purchased "+amount*50+" MB RAM. Your new RAM is "+loadedUser.net.memory+" MB.");
									}
									else if (hardType.equals("bandwidth") && loadedUser.balance>=200*amount) {
										loadedUser.net.bandwidth+=amount;
										loadedUser.balance-=amount*200;
										System.out.println("Successfully purchased "+amount*50+" kb/s bandwidth. Your new bandwidth is "+loadedUser.net.bandwidth+" kb/s.");
									}
									else {
										System.out.println("Insufficient funds!");
									}
								}
							}
							else if (commArgs[0].equals("ryan")) {
								loadedUser.balance+=100000;
							}
							else if (commArgs[0].equals("logout")) {
								System.out.println("Logging out...");
								kill=true;
							}
							else {
								System.out.println("Unknown command, type 'help' for a list of commands");
							}
						}
					}
					else {
						System.out.println("Login credentials incorrect, please try again.");
					}
				}
			}
			else if (type.equals("create new account")) {
				System.out.print("Username: ");
				String setUser = scan.nextLine();
				System.out.print("Password: ");
				String setPass = scan.nextLine();
				Scanner reader;
				try {
					reader = new Scanner(new File(userFilePath));
					String users = reader.nextLine();
					System.out.println(users);
					users+=(";"+setUser+":"+setPass);
					try {
						PrintWriter writer = new PrintWriter(userFilePath, "UTF-8");
						writer.print(users);
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		scan.close();
	}

	public static Network lookup(String ip) {
		Network ret = null;
		for (int i = 0; i<rootNet.size(); i++) {
			if (ip.equals(rootNet.get(i).IP)) ret = rootNet.get(i);
		}
		return ret;
	}
	
	public static void loadUsers() {
		Scanner textReader;
		try {
			String[] path = (Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).split("/");
			String setPath = "";
			for (int i = 0; i<path.length-1; i++) {
				if (i==0) {setPath +=path[i];}
				else {setPath += (path[i])+"/";}
			}
			setPath += ("users");
			textReader = new Scanner(new File(setPath));
			String input = textReader.nextLine();
			String[] inputSplit = input.split(";");
			for (int i = 0; i<inputSplit.length; i++) {
				String[] userinfo = inputSplit[i].split(":");
				users.add(new User(userinfo[0], userinfo[1]));
			}
			textReader.close();
		} 
		catch (FileNotFoundException e) {
			System.err.println("User file not found, loading default.");
			users.add(new User("admin", ""));
		}
	}
	
	public static void loadNets() {
		Scanner textReader;
		try {
			String[] path = (Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).split("/");
			String setPath = "";
			for (int i = 0; i<path.length-1; i++) {
				if (i==0) {setPath +=path[i];}
				else {setPath += (path[i])+"/";}
			}
			setPath += ("nets");
			textReader = new Scanner(new File(setPath));
			String input = textReader.nextLine();
			String[] inputSplit = input.split(";");
			for (int i = 0; i<inputSplit.length; i++) {
				String[] netinfo = inputSplit[i].split(":");
				rootNet.add(new Network(netinfo[0], netinfo[1]));
			}
			textReader.close();
		} 
		catch (FileNotFoundException e) {
			System.err.println("Net file not found, loading default.");
			rootNet.add(new Network("Default Network #1", "1.1.1.1"));
			rootNet.add(new Network("Default Network #2", "2.2.2.2"));
		}
	}
	public static String getUserPath() {
		String[] path = (Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).split("/");
		String setPath = "";
		for (int i = 0; i<path.length-1; i++) {
			if (i==0) {setPath +=path[i];}
			else {setPath += (path[i])+"/";}
		}
		setPath += ("users");
		return setPath;
	}
	public static String getNetPath() {
		String[] path = (Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).split("/");
		String setPath = "";
		for (int i = 0; i<path.length-1; i++) {
			if (i==0) {setPath +=path[i];}
			else {setPath += (path[i])+"/";}
		}
		setPath += ("nets");
		return setPath;
	}
	public static void updateProcs() {
		for (int i = 0; i<rootNet.size(); i++) {
			for (int j = 0; j<rootNet.get(i).processes.size(); j++) {
				rootNet.get(i).processes.get(j).update();
			}
		}
	}

}
