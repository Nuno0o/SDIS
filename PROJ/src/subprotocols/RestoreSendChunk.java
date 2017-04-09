package subprotocols;

import java.io.IOException;
import java.net.DatagramPacket;

import fileManagement.ChunksRestSending;
import fileManagement.ChunksSending;
import fileManagement.FileChunk;
import services.Message;
import services.Peer;
import utilities.Constants;
import utilities.RandomDelay;

import java.net.MulticastSocket;

public class RestoreSendChunk extends Thread {
	public Peer peer;
    public String fileid;
    public int chunkNo;
    public byte[] data;

    public RestoreSendChunk (Peer peer, String fileid , int chunkNo, byte[] data){
    	super(fileid+":"+chunkNo);

        this.peer = peer;

        this.fileid = fileid;
        this.chunkNo = chunkNo;
        this.data = data;

		System.out.println("Data Size: " + data.length);

    }

    public void run(){
    	
    	
    	
    	if(ChunksRestSending.hasResponses(this.fileid, this.chunkNo)){
    		ChunksRestSending.remove(this.fileid, this.chunkNo);
    		return;
    	}
    	ChunksRestSending.remove(this.fileid, this.chunkNo);

    	Message m = new Message();
		byte[] chunkMsg = m.chunkMsg(this.peer.peerNumber, fileid, chunkNo, data);

		DatagramPacket packet = new DatagramPacket(chunkMsg,
											chunkMsg.length,
											this.peer.mcastMDR,
											this.peer.portMDR);

		try {
			this.peer.MDR.writeToMulticast(packet);
		} catch (Exception e){
			System.err.println("Errror sending chunk message");
			e.printStackTrace();
		}

		return;
    }
}
