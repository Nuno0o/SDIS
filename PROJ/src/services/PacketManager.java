package services;

import fileManagement.ChunksRestSending;
import fileManagement.ChunksSending;
import fileManagement.ChunksStored;
import fileManagement.FileChunk;
import fileManagement.WriteFile;
import fileManagement.PutchunksSending;
import subprotocols.RestoreSendChunk;
import utilities.Constants;
import utilities.RandomDelay;
import subprotocols.BackupSubprotocol;

import java.util.Arrays;
import java.util.ArrayList;
import java.net.DatagramPacket;

public class PacketManager {
	public Peer peer;
	public WriteFile wf;

	public PacketManager(Peer peer){
		this.peer = peer;
		this.wf = new WriteFile();
	}
	//public boolean handlePacket(String packet){
	public boolean handlePacket(byte[] packetData, int length){
		String[] splitStr = new String(packetData, 0, length).split("\\s+");

		String packet = new String(packetData, 0, length);

		if (splitStr[0].equals("PUTCHUNK")) {
			return handlePutChunk(packetData, length);
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

		if (splitStr[0].equals("CHUNK")){
			return handleChunk(packetData, length);
		}

		if (splitStr[0].equals("REMOVED")){
			return handleRemoved(packet);
		}
		return false;
	}

	public boolean handlePutChunk(byte[] packetData, int length){

		String[] splitStr = new String(packetData, 0, length).split("\\s+");
		String[] splitStr2 = new String(packetData, 0, length).split(Constants.CRLF);

		if(!splitStr[1].equals(this.peer.protocol_version)){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			return false;
		}

		if(PutchunksSending.incrementResponses(splitStr[3], Integer.parseInt(splitStr[4]))){
			return true;
		}

		//Create new chunk out of packet
		FileChunk chunk = new FileChunk(splitStr[3],Arrays.copyOfRange(packetData, splitStr2[0].length()+splitStr2[1].length(), length -1),Integer.parseInt(splitStr[4]),Integer.parseInt(splitStr[5]));
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
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
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

		byte[] stored = m.storedMsg(this.peer.peerNumber, chunk.fileId, chunk.chunkNo);

		DatagramPacket packet = new DatagramPacket( stored,
	            stored.length,
	            this.peer.mcastMC,
	            this.peer.portMC);
		try{
			this.peer.MC.msocket.send(packet);
		}catch(Exception e){
				System.out.println("Error sending stored msg");
		}

		return true;
	}

	public boolean sendChunkMessage(String fileId, int chunkNo, String data){
		Message m = new Message();
		byte[] chunkMsg = m.chunkMsg(this.peer.peerNumber, fileId, chunkNo, data.getBytes());

		DatagramPacket packet = new DatagramPacket(chunkMsg,
											chunkMsg.length,
											this.peer.mcastMDR,
											this.peer.portMDR);

		try {
			this.peer.MDR.msocket.send(packet);
		} catch (Exception e){
			System.err.println("Errror sending chunk message");
			e.printStackTrace();
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

		System.out.println(storedChunksForFile.toString());

		if (storedChunksForFile.contains(fileId+":"+splitStr[4])){
			String data = new String(ChunksStored.getChunkData(fileId, Integer.parseInt(splitStr[4])));
			try{
				RestoreSendChunk r = new RestoreSendChunk(this.peer,fileId,Integer.parseInt(splitStr[4]),data);
				r.start();
			}catch(Exception e){

			}

			return true;

		}

		return false;
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

	public boolean handleChunk(byte[] packetData, int length){
		String[] splitStr = new String(packetData, 0, length).split("\\s+");
		String[] splitStr2 = new String(packetData, 0, length).split(Constants.CRLF);
		if(!splitStr[1].equals(this.peer.protocol_version)){
			return false;
		}
		if(Integer.parseInt(splitStr[2]) == this.peer.peerNumber){
			return false;
		}

		//In case it's a chunk that's being sent and this is another peer that also responded with that chunk
		if(ChunksRestSending.incrementResponses(splitStr[3], Integer.parseInt(splitStr[4]))){
			return true;
		}

		FileChunk chunk = new FileChunk(splitStr[3],Arrays.copyOfRange(packetData, splitStr2[0].length()+splitStr2[1].length(), length -1),Integer.parseInt(splitStr[4]),1);
		wf.storeChunk(chunk, splitStr[3]+":"+splitStr[4]);
		ChunksStored.addNew(chunk);

		return true;
	}

	public boolean handleRemoved(String packet){

		String[] splitStr = packet.split("\\s+");

		// update local count of chunk (count --)
		ChunksStored.decRepDeg(splitStr[3],Integer.parseInt(splitStr[4]));

		// if count drops below, initiate BackupSubprotocol after delay
		if (ChunksStored.lessThanReal(splitStr[3],Integer.parseInt(splitStr[4]))){

			try {
				Thread.sleep(RandomDelay.getRandomDelay());
			} catch(Exception e){
				System.err.println("InterruptedException: " + e.toString());
				e.printStackTrace();
			}

			//TODO: if  during the delay gets PUTCHUNK of the same file chunk, back off
			if(PutchunksSending.hasResponses(splitStr[3], Integer.parseInt(splitStr[4]))){
	    		return true;
	    	}
			// else

			new BackupSubprotocol(	this.peer,
			 						new FileChunk(splitStr[3],
										new String (ChunksStored.getChunkData(splitStr[3],Integer.parseInt(splitStr[4]))).getBytes(),
										Integer.parseInt(splitStr[4]),
										ChunksStored.getRepDeg(splitStr[3],Integer.parseInt(splitStr[4]))),
									ChunksStored.getRepDeg(splitStr[3],Integer.parseInt(splitStr[4]))).start();

			System.out.println("Started BackupSubprotocol from Removed, everything operational.");
		}

		return true;
	}
}
