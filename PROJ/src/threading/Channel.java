package threading;

import utilities.Constants;
import services.Peer;
import java.net.*;

public class Channel extends Thread {
	public Peer peer;
	public MulticastSocket msocket;
	public byte[] buffer;
	public DatagramPacket packet;

	public Channel(Peer p){
		super("Peer" + p.peerNumber);

		this.peer = p;

		this.msocket = null;

		this.buffer = new byte[Constants.MAX_BUFFER_SIZE];

		this.packet = new DatagramPacket(this.buffer,Constants.MAX_BUFFER_SIZE);
	}

}
