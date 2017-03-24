package services;

import fileManagement.Chunker;
import fileManagement.FileChunk;
import subprotocols.BackupSubprotocol;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteService extends UnicastRemoteObject implements RemoteServiceInterface{

  public Peer peer;
  public Chunker chunker;
  public BackupSubprotocol backupSubprotocol;

  public RemoteService(Peer peer) throws RemoteException{
    this.peer = peer;
  }

  // The method that implements the backup.
  public boolean backup(String path, int repDeg){
      this.chunker = new Chunker(path);

      FileChunk currentChunk = this.chunker.nextChunk();

      while (currentChunk != null) {

          this.backupSubprotocol = new BackupSubprotocol(this.peer, currentChunk, repDeg);
          this.backupSubprotocol.putchunk();

          currentChunk = this.chunker.nextChunk();
      }
      return true;
  }

}
