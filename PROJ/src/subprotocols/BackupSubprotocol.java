package subprotocols;

import services.Peer;
import services.Message;
import fileManagement.ChunksSending;
import fileManagement.FileChunk;
import utilities.Constants;

import java.io.IOException;
import java.net.DatagramPacket;

public class BackupSubprotocol extends Thread{

    public Peer peer;
    public FileChunk chunk;
    public int repDeg;

    public BackupSubprotocol (Peer peer, FileChunk chunk, int repDeg){
    	super(chunk.fileId+":"+chunk.chunkNo);

        this.peer = peer;

        this.chunk = chunk;
        this.repDeg = repDeg;

    }

    public void run(){

        int tries = Constants.MAX_TRIES;
        while(tries > Constants.ZERO_TRIES){

        	ChunksSending.add(chunk);

            String data = new String(this.chunk.data);
            Message m = new Message();

            String msg = m.putchunkMsg( this.peer.peerNumber,
                                        this.chunk.fileId,
                                        this.chunk.chunkNo,
                                        this.repDeg,
                                        data);

            // write putchunk
            DatagramPacket packet = new DatagramPacket( msg.getBytes(),
            msg.getBytes().length,
            this.peer.mcastMDB,
            this.peer.portMDB);

            try {
                this.peer.MDB.msocket.send(packet);
            } catch (IOException e){
                System.err.println("BackupSubprotocol Exception. Couldn't send packet. " + e.toString());
                e.printStackTrace();
            }

            try{
            	Thread.sleep(Constants.ONE_SECOND);
            }catch(Exception e){
            	//???
            }
            if(ChunksSending.hasEnoughResponses(chunk)){
            	break;
            }else{
            	tries--;
            	continue;
            }
        }
    }
}
