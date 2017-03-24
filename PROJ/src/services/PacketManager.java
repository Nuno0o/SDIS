package services;

public class PacketManager {
	public Peer peer;

	public PacketManager(Peer peer){
		this.peer = peer;
	}
	public boolean handlePacket(String packet){
		String[] splitStr = packet.split("\\s+");

		switch(splitStr[0]){
		case "PUTCHUNK":
			return handlePutChunk(splitStr);
		default:
			return false;
		}
	}

	public boolean handlePutChunk(String[] splitStr){
		if(splitStr[1] != this.peer.protocol_version){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			return false;
		}
		return true;
	}
}
