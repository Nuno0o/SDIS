package subprotocols;

import services.Peer;
import services.Message;

import java.io.IOException;
import java.net.DatagramPacket;

public class ReclaimSubprotocol extends Thread {

    public Peer peer;
    public String fileid;
    public int chunkNo;

    public ReclaimSubprotocol(Peer peer, String fileid, int chunkNo){
        this.peer = peer;
        this.fileid = fileid;
        this.chunkNo = chunkNo;
    }

    public void run(){

        Message m = new Message();

        byte[] msg = m.removedMsg(  this.peer.peerNumber,
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

    }

}
