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
		//Channel cycle
		while(true){
			//Receive UDP datagram, continue if failed
			try{
				this.msocket.receive(this.packet);
			}catch(Exception e){
				System.out.println("Error receiving packet on MC");
				continue;
			}
			//Parse packet
			String packetData = new String(this.packet.getData());
			String []splitStr = packetData.split("\\s+");
			
			
			
		}
	}

}
