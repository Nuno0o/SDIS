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

public class RestoreSendChunk extends Thread {
	public Peer peer;
    public String fileid;
    public int chunkNo;
    public String data;

    public RestoreSendChunk (Peer peer, String fileid , int chunkNo, String data){
    	super(fileid+":"+chunkNo);

        this.peer = peer;

        this.fileid = fileid;
        this.chunkNo = chunkNo;
        this.data = data;

    }

    public void run(){
    	try{
			Thread.sleep(RandomDelay.getRandomDelay());
		}catch(Exception e){

		}

    	if(ChunksRestSending.hasResponses(this.fileid, this.chunkNo)){
    		return;
    	}

    	Message m = new Message();
		byte[] chunkMsg = m.chunkMsg(this.peer.peerNumber, fileid, chunkNo, data.getBytes());

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

		return;
    }
}
