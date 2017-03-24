package services;

public class PacketManager {
	public Peer peer;
	
	public PacketManager(Peer peer){
		this.peer = peer;
	}
	public void handlePacket(String packet){
		String[] splitStr = packet.split("\\s+");
		
		switch(splitStr[0]){
		case "PUTCHUNK":
			handlePutChunk(splitStr);
			break;
		}
	}
	
	public Boolean handlePutChunk(String[] splitStr){
		if(splitStr[1] != peer.protocol_version){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == peer.peerNumber){
			return false;
		}
		return true;
	}
}