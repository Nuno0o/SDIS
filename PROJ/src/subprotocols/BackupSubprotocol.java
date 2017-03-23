package subprotocol;

import services.Peer;
import services.Message;
import threading.MCchannel;
import threading.MDBchannel;
import fileManagement.FileChunk;
import utilities.RandomDelay;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.io.IOException;

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

        String msg = m.putchunkMsg( this.peerNumber,
                                    chunk.fileId,
                                    chunk.chunkNo,
                                    this.repDeg,
                                    data);

        // write putchunk

        DatagramPacket packet = new DatagramPacket( msg.getBytes(),
                                                    msg.getBytes().length,
                                                    this.MDB.peer.mcastMC,
                                                    this.MDB.peer.portMC);

        try {
            this.MDB.msocket.send(packet);
        } catch (IOException e){
            System.err.println("BackupSubprotocol Exception. Couldn't send packet. " + e.toString());
            e.printStackTrace();
        }

        int threadDelay = new RandomDelay().getRandomDelay();

        try {
            Thread.sleep(threadDelay);
        } catch (Exception e) {
            System.err.println("Thread Sleep exception: " + e.toString());
            e.printStackTrace();
        }

        // TODO: get STORED messages

    }

}
