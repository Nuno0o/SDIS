import java.net.*;

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
	MCchannel MC;
	MDBchannel MDB;
	MDRchannel MDR;

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
	}
}
