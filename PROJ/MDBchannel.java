import java.net.MulticastSocket;

public class MDBchannel extends Channel {
	
	public MDBchannel(Peer p){
		super(p);
		
		try{
			this.msocket = new MulticastSocket(this.peer.portMDB);
			
			this.msocket.joinGroup(this.peer.mcastMDB);
		}catch(Exception e){
			System.out.println("Couldn't connect to multicast channel");
			System.exit(1);
		}
	}
	
	public void run(){
		
	}
}
