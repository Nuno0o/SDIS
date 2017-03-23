import java.net.*;

public class Peer {
	public String protocol_version;
	public String remote_name;
	public int peerNumber;
	
	public InetAddress mcastMC;
	public InetAddress mcastMDB;
	public InetAddress mcastMDR;
	
	public int portMC;
	public int portMDB;
	public int portMDR;
	
	MCchannel MC;
	MDBchannel MDB;
	MDRchannel MDR;

	public Peer(String[] args){
		this.protocol_version = args[0];
		this.peerNumber = Integer.parseInt(args[1]);
		this.remote_name = args[2];
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
	
	public void start(){
		this.MC.start();
		this.MDB.start();
		this.MDR.start();
	}
}
