package threading;

import utilities.Constants;
import services.FileManager;
import services.PacketManager;
import services.Peer;
import java.net.*;

public class Channel extends Thread {
	public Peer peer;
	public MulticastSocket msocket;
	public byte[] buffer;
	public DatagramPacket packet;
	public PacketManager packetmanager;
	public FileManager filemanager;

	public Channel(Peer p){
		super("Peer" + p.peerNumber);

		this.peer = p;

		this.msocket = null;

		this.buffer = new byte[Constants.MAX_BUFFER_SIZE];

		this.packet = new DatagramPacket(this.buffer,Constants.MAX_BUFFER_SIZE);
		
		this.packetmanager = new PacketManager(this.peer);
	}

}
