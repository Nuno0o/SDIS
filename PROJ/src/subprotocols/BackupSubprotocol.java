package subprotocols;

import services.Peer;
import services.Message;
import threading.MCchannel;
import threading.MDBchannel;
import fileManagement.FileChunk;
import utilities.RandomDelay;
import utilities.Constants;

import java.io.IOException;
import java.util.HashSet;
import java.net.MulticastSocket;
import java.net.DatagramPacket;

public class BackupSubprotocol{

    public Peer peer;
    public FileChunk chunk;
    public int repDeg;

    public BackupSubprotocol (Peer peer, FileChunk chunk, int repDeg){
        this.peer = peer;

        this.chunk = chunk;
        this.repDeg = repDeg;

    }

    public void putchunk(){

        int tries = Constants.MAX_TRIES;
        while(tries > Constants.ZERO_TRIES){

            String data = new String(this.chunk.data);

            System.out.println("Data:" + data);
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

            int threadDelay = new RandomDelay().getRandomDelay();

            System.out.println(threadDelay);

            try {
                Thread.sleep(threadDelay);
                this.peer.MC.msocket.setSoTimeout(Constants.ONE_SECOND);
            } catch (Exception e) {
                System.err.println("Thread Sleep exception: " + e.toString());
                e.printStackTrace();
            }

            int storedReceived = 0;

            HashSet<Integer> peersResponded = new HashSet<Integer>();

            System.out.println("SLEPT");

            while(storedReceived < this.repDeg){

                System.out.println("STOREDRECEIVED < THISREPDEG");

                try{
                    this.peer.MC.msocket.receive(this.peer.MC.packet);
                    System.out.println("RECEIVED MC");
                }catch(Exception e){
                    System.out.println("Didn't receive enough confirmations");
                    break;
                }

                String packetData = new String(this.peer.MC.packet.getData());
                String[] splitStr = packetData.split("\\s+");

                if(  /*versao etc*/
                    splitStr[0] == "STORED" &&
                    Integer.parseInt(splitStr[2]) != this.peer.peerNumber &&
                    splitStr[3] == this.chunk.fileId &&
                    Integer.parseInt(splitStr[4]) == this.chunk.chunkNo)
                {
                    System.out.println("TODO");
                    //TODO: check if this works...
                    if (!peersResponded.contains(new Integer(splitStr[2]))){
                        peersResponded.add(new Integer(splitStr[2]));
                        storedReceived++;
                    }
                }

            }
            if(storedReceived < this.repDeg){
                tries--;
                continue;
            }else break;
        }
    }
}
