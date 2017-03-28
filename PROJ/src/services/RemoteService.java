package services;

import fileManagement.Chunker;
import fileManagement.FileChunk;
import subprotocols.BackupSubprotocol;
import subprotocols.DeleteSubprotocol;
import utilities.Constants;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteService extends UnicastRemoteObject implements RemoteServiceInterface{

  public Peer peer;
  public Chunker chunker;
  public BackupSubprotocol backupSubprotocol;
  public DeleteSubprotocol deleteSubprotocol;

  public RemoteService(Peer peer) throws RemoteException{
    this.peer = peer;
  }

  // The method that implements the backup.
  public void backup(String path, int repDeg){

      System.out.println("I've been summoned");

      this.chunker = new Chunker(path,repDeg);

      FileChunk currentChunk = this.chunker.nextChunk();

      chunker.saveMetaData();

      while (currentChunk != null) {

          System.out.println("Im here");

          this.backupSubprotocol = new BackupSubprotocol(this.peer, currentChunk, repDeg);

          this.backupSubprotocol.start();

          if(currentChunk.data.length < Constants.MAX_BODY_SIZE)
            break;

          currentChunk = this.chunker.nextChunk();

      }
      this.chunker.close();
  }

  // The method that implements the deleting of chunks.
  public void delete(String path) {
      this.chunker = new Chunker(path, 0);

      FileChunk currentChunk = this.chunker.nextChunk();

      while(currentChunk != null){

          this.deleteSubprotocol = new DeleteSubprotocol(this.peer, currentChunk);
          this.deleteSubprotocol.delete();

          currentChunk = this.chunker.nextChunk();

      }
  }

}
