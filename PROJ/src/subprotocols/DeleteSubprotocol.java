package subprotocols;

import services.Peer;
import utilities.Constants;
import services.Message;
import fileManagement.*;

import java.io.IOException;
import java.net.*;

public class DeleteSubprotocol extends Thread {

    public Peer peer;
    public String fileid;

    public DeleteSubprotocol(Peer peer, String fileid){
        this.peer = peer;
        this.fileid = fileid;
    }

    public void run(){

        Message m = new Message();

        String msg = m.deleteMsg( this.peer.peerNumber, this.fileid);

        DatagramPacket packet = new DatagramPacket( msg.getBytes(),
        msg.getBytes().length,
        this.peer.mcastMC,
        this.peer.portMC);

        try {
        	int NTries = Constants.MAX_TRIES;
        	while(NTries > 0){
        		this.peer.MDB.msocket.send(packet);
        		NTries--;
        		Thread.sleep(300);
        	}
            Chunker.removeMetaData(this.fileid);
        } catch (Exception e){
            System.err.println("DeleteSubprotocol Exception. Couldn't send packet. " + e.toString());
            e.printStackTrace();
        }

    }

}
