package services;

import fileManagement.Chunker;
import fileManagement.FileChunk;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteService extends UnicastRemoteObject implements RemoteServiceInterface{

  public int peerNo;
  public Chunker chunker;

  public RemoteService(Peer peer) throws RemoteException{
    this.peerNo = peer.peerNumber;
  }

  // The method that implements the backup.
  public boolean backup(String path, int repDeg){
      this.chunker = new Chunker(path);

      FileChunk currentChunk = this.chunker.nextChunk();

      while (currentChunk != null) {
          //TODO:Call subprotocol with chunk...
          currentChunk = this.chunker.nextChunk();
      }
      return true;
  }

}
