import java.net.*;

public class MCchannel extends Channel {
	
	public MCchannel(Peer p){
		super(p);
		
		try{
			this.msocket = new MulticastSocket(this.peer.portMC);
			
			this.msocket.joinGroup(this.peer.mcastMC);
		}catch(Exception e){
			System.out.println("Couldn't connect to multicast channel");
			System.exit(1);
		}
	}
	
	public void run(){
		while(true){
			
		}
	}

}
