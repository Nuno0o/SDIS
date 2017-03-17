import java.net.*;

public class Channel extends Thread {
	protected static Peer peer;
	protected static MulticastSocket msocket;
	protected static byte[] buffer;
	
	public Channel(Peer p){
		super("Peer" + p.peerNumber);
		
		this.peer = p;
		
		this.msocket = null;
		
		this.buffer = new byte[256000];
	}
	
}
