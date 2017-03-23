package threading;

import services.Peer;
import java.net.*;

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
			String[] splitStr = packetData.split("\\s+");


		}
	}
}
