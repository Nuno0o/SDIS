package subprotocol;

import services.Peer;
import services.Message;
import threading.MCchannel;
import threading.MDBchannel;
import fileManagement.FileChunk;

public class BackupSubprotocol{

    public MCchannel MC;
    public MDBchannel MDB;
    public FileChunk chunk;
    public int repDeg;
    public int peerNumber;

    public BackupSubprotocol (Peer peer, FileChunk chunk, int repDeg){

        this.MC = peer.MC;
        this.MDB = peer.MDB;

        this.chunk = chunk;
        this.repDeg = repDeg;
        this.peerNumber = peer.peerNumber;

    }

    public void putchunk(){

        String data = new String(chunk.data);
        Message m = new Message();
        
        String msg = m.putchunkMsg(this.peerNumber, chunk.fileId, chunk.chunkNo, this.repDeg, data);

        //TODO: write to MDB putchunk msg
        //TODO: wait for STORED messages

    }

}
