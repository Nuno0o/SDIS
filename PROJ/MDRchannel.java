import java.net.MulticastSocket;

public class MDRchannel extends Channel {
	
	public MDRchannel(Peer p){
		super(p);
		
		try{
			this.msocket = new MulticastSocket(this.peer.portMDR);
			
			this.msocket.joinGroup(this.peer.mcastMDR);
		}catch(Exception e){
			System.out.println("Couldn't connect to multicast channel");
			System.exit(1);
		}
	}
	
	public void run(){
		
	}
}
