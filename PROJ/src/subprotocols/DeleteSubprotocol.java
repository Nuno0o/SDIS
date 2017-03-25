package subprotocols;

import services.Peer;
import services.Message;
import fileManagement.FileChunk;

import java.io.IOException;
import java.net.*;

public class DeleteSubprotocol {

    public Peer peer;
    public FileChunk chunk;

    public DeleteSubprotocol(Peer peer, FileChunk chunk){
        this.peer = peer;
        this.chunk = chunk;
    }

    public void delete(){

        Message m = new Message();

        String msg = m.deleteMsg( this.peer.peerNumber, this.chunk.fileId);

        DatagramPacket packet = new DatagramPacket( msg.getBytes(),
        msg.getBytes().length,
        this.peer.mcastMC,
        this.peer.portMC);

        try {
            this.peer.MDB.msocket.send(packet);
        } catch (IOException e){
            System.err.println("DeleteSubprotocol Exception. Couldn't send packet. " + e.toString());
            e.printStackTrace();
        }

        // TODO: Repeat as many times as necessary to ensure all chunks were deleted.

    }

}
