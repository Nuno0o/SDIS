package services;

import fileManagement.WriteFile;
import java.net.DatagramPacket;

public class PacketManager {
	public Peer peer;
	public WriteFile writeFile;

	public PacketManager(Peer peer){
		this.peer = peer;
		this.writeFile = new WriteFile();
	}
	public boolean handlePacket(String packet){
		String[] splitStr = packet.split("\\s+");
		if (splitStr[0].equals("PUTCHUNK")) {
			if(handlePutChunk(splitStr)){
				writeFile.storeChunk(packet);
				if (!sendStoredChunk(splitStr)) return false;
				return true;
			}
			return false;
		}

		if (splitStr[0].equals("GETCHUNK")){
			if(handleGetChunk(splitStr)){
				sendChunkResp(splitStr);
				return true;
			}
			return false;
		}

		return false;

	}

	public boolean handlePutChunk(String[] splitStr){
		if(!splitStr[1].equals(this.peer.protocol_version)){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			return false;
		}
		return true;
	}

	public boolean sendStoredChunk(String[] splitStr){
		Message m = new Message();

		String stored = m.storedMsg(this.peer.peerNumber, splitStr[3], Integer.parseInt(splitStr[4]));

		DatagramPacket packet = new DatagramPacket( stored.getBytes(),
	            stored.getBytes().length,
	            this.peer.mcastMC,
	            this.peer.portMC);
		try{
			this.peer.MC.msocket.send(packet);
		}catch(Exception e){
			System.out.println("Error sending stored msg");
		}

		return true;
	}

	public boolean handleGetChunk(String[] splitStr){
		if(splitStr[1] != this.peer.protocol_version){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			return false;
		}

		//TODO: Check if file exists return true if so else false
		return true;
	}

	public boolean sendChunkResp(String[] splitStr){

		return true;
	}
}
