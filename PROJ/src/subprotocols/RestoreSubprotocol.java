package subprotocols;

import services.Peer;
import services.Message;

import java.io.IOException;
import java.net.DatagramPacket;

public class RestoreSubprotocol extends Thread {

    public Peer peer;
    public String fileid;
    public int chunkNo;

    public RestoreSubprotocol(Peer peer, String fileid, int chunkNo){
        this.peer = peer;
        this.fileid = fileid;
        this.chunkNo = chunkNo;
    }

    public void run(){

        Message m = new Message();
        byte[] msg = m.getchunkMsg( this.peer.peerNumber,
                                    this.fileid,
                                    this.chunkNo );

        DatagramPacket packet = new DatagramPacket( msg,
                                                    msg.length,
                                                    this.peer.mcastMC,
                                                    this.peer.portMC);

        try {
            this.peer.MC.msocket.send(packet);
        } catch (IOException e){
            System.err.println("RestoreSubprotocol Exception. Couldn't send packet. " + e.toString());
            e.printStackTrace();
        }

        // Wait for CHUNK messages

    }

}
