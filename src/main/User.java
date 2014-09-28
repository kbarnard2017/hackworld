package main;

import java.util.ArrayList;

public class User {
	
	public Network usernet;
	public String username, password;
	public Network net;
	public int balance;
	public ArrayList<Network> database = new ArrayList<Network>();
	
	public User(String par1Username, String par2Password) {
		username = par1Username;
		password = par2Password;
		net = new Network(username+"'s Network", (Integer.toString((int)(Math.random()*1000)))+"."+(Integer.toString((int)(Math.random()*1000)))+"."+(Integer.toString((int)(Math.random()*1000)))+"."+(Integer.toString((int)(Math.random()*1000))));
		if ((username.equals("loadedUser"))==false) Main.users.add(this);
		balance=0;
	}

}
