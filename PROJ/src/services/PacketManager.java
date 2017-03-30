package services;

import fileManagement.ChunksSending;
import fileManagement.ChunksStored;
import fileManagement.FileChunk;
import fileManagement.WriteFile;
import utilities.Constants;
import utilities.RandomDelay;

import java.util.ArrayList;
import java.net.DatagramPacket;
import java.util.Set;
import java.io.File;
import java.io.FilenameFilter;

public class PacketManager {
	public Peer peer;
	public WriteFile wf;

	public PacketManager(Peer peer){
		this.peer = peer;
		this.wf = new WriteFile();
	}
	public boolean handlePacket(String packet){

		String[] splitStr = packet.split("\\s+");

		if (splitStr[0].equals("PUTCHUNK")) {
			return handlePutChunk(packet);
		}

		if (splitStr[0].equals("GETCHUNK")){
			return handleGetChunk(packet);
		}

		if (splitStr[0].equals("DELETE")){
			return handleDelete(packet);
		}

		if (splitStr[0].equals("STORED")){
			return handleStored(packet);
		}

		System.out.println("Reached");
		return false;
	}

	public boolean handlePutChunk(String packet){

		System.out.println("PUTCHUNKK");
		String[] splitStr = packet.split("\\s+");
		String[] splitStr2 = packet.split(Constants.CRLF);

		if(!splitStr[1].equals(this.peer.protocol_version)){
			System.out.println("Version Mismatch: " + splitStr[1] + " != " + this.peer.protocol_version);
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			System.out.println("Peer Mismatch");
			return false;
		}
		//Create new chunk out of packet
		FileChunk chunk = new FileChunk(splitStr[3],splitStr2[2].getBytes(),Integer.parseInt(splitStr[4]),Integer.parseInt(splitStr[5]));
		//Chunk name
		String chunkname = chunk.fileId+":"+chunk.chunkNo;
		//Store chunk in filesystem
		wf.storeChunk(chunk, chunkname);
		//Add chunk to stored chunks list
		ChunksStored.addNew(chunk);
		//Sleep between 0 and 400 ms
		try{
			Thread.sleep(RandomDelay.getRandomDelay());
		}catch(Exception e){

		}
		//Send stored answer
		if (!sendStoredChunk(chunk)) return false;
		return true;
	}

	public boolean handleStored(String packet){
		String[] splitStr = packet.split("\\s+");

		if(!splitStr[1].equals(this.peer.protocol_version)){
			System.out.println("Version Mismatch: " + splitStr[1] + " != " + this.peer.protocol_version);
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			System.out.println("Peer Mismatch");
			return false;
		}

		if(ChunksSending.incrementResponses(splitStr[3],Integer.parseInt(splitStr[4]))){
			return true;
		}

		if(ChunksStored.incDegree(splitStr[3],Integer.parseInt(splitStr[4]))){
			return true;
		}

		return false;
	}

	public boolean sendStoredChunk(FileChunk chunk){
		Message m = new Message();

		String stored = m.storedMsg(this.peer.peerNumber, chunk.fileId, chunk.chunkNo);

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

	public boolean handleGetChunk(String packet){
		String[] splitStr = packet.split("\\s+");
		if(!splitStr[1].equals(this.peer.protocol_version)){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			return false;
		}

		String fileId = splitStr[3];

		ArrayList<String> storedChunksForFile = ChunksStored.getChunksStored(fileId);

		// TODO: Obter chunks

		return true;
	}

	public boolean handleDelete(String packet){
		String[] splitStr = packet.split("\\s+");
		if(!splitStr[1].equals(this.peer.protocol_version)){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			return false;
		}
		return ChunksStored.deleteFile(splitStr[3]);
	}
}
