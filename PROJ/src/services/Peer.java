package services;

import threading.Channel;
import threading.MCchannel;
import threading.MDBchannel;
import threading.MDRchannel;
import fileManagement.FileChunk;
import subprotocols.BackupSubprotocol;

import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Peer {

	//Peer information
	public String protocol_version;
	public String remote_name;
	public int peerNumber;

	//Multicast IPs
	public InetAddress mcastMC;
	public InetAddress mcastMDB;
	public InetAddress mcastMDR;

	//Multicast ports
	public int portMC;
	public int portMDB;
	public int portMDR;

	//Multicast Channels
	public MCchannel MC;
	public MDBchannel MDB;
	public MDRchannel MDR;
	
	public Peer(String[] args){
		//Init peer information
		this.protocol_version = args[0];
		this.peerNumber = Integer.parseInt(args[1]);
		this.remote_name = args[2];
		//Set multicast channels ips and ports
		try{
			this.mcastMC = InetAddress.getByName(args[3]);
			this.portMC = Integer.parseInt(args[4]);

			this.mcastMDB = InetAddress.getByName(args[5]);
			this.portMDB = Integer.parseInt(args[6]);

			this.mcastMDR = InetAddress.getByName(args[7]);
			this.portMDR = Integer.parseInt(args[8]);

			this.MC = new MCchannel(this);
			this.MDB = new MDBchannel(this);
			this.MDR = new MDRchannel(this);
		}catch(Exception e){
			System.out.println("Couldn't open all channels...");
			System.exit(1);
		}
	}
	//Start channels
	public void start(){

		this.MC.start();
		this.MDB.start();
		this.MDR.start();

		try {

			RemoteService remoteService = new RemoteService(this);

			try {
				Registry registry = LocateRegistry.getRegistry();
				registry.bind(this.remote_name, remoteService);
			} catch (AlreadyBoundException e) {
				System.err.println("Already bound: " + e.toString());
			}

			System.out.println("Remote Object Created");

		} catch (Exception e){
			System.err.println("Peer exception on remote try:" + e.toString());
			e.printStackTrace();
		}

	}

}
