package services;

import fileManagement.Chunker;
import fileManagement.FileChunk;
import fileManagement.Metadata;
import subprotocols.BackupSubprotocol;
import subprotocols.DeleteSubprotocol;
import subprotocols.RestoreSubprotocol;
import utilities.Constants;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteService extends UnicastRemoteObject implements RemoteServiceInterface{

  public Peer peer;
  public Chunker chunker;
  public BackupSubprotocol backupSubprotocol;
  public DeleteSubprotocol deleteSubprotocol;
  public RestoreSubprotocol restoreSubprotocol;

  public RemoteService(Peer peer) throws RemoteException{
    this.peer = peer;
  }

  // The method that implements the backup.
  public void backup(String path, int repDeg){

      System.out.println("Backing up file in :" + path);

      this.chunker = new Chunker(path,repDeg);

      FileChunk currentChunk = this.chunker.nextChunk();

      Metadata.saveMetadata(path,chunker.fileid);

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
      try {
          System.out.println("Deleting file in :" + path);

          String fileid = Metadata.findMetadata(path);

          System.out.println("Found fileID: " + fileid);

          if(fileid == null){
        	  System.out.println("This file hasn't been backed up");
        	  return;
          }

          this.deleteSubprotocol = new DeleteSubprotocol(this.peer,fileid);
          this.deleteSubprotocol.start();

      } catch(Exception e){
    	  System.out.println("Couldn't execute delete protocol");
      }

  }

  // The method that implements the restoring of chunks.
  public void restore(String path) {
      try {
          System.out.println("Trying to restore file: " + path);

          String fileid = Metadata.findMetadata(path);
          int noChunks = Metadata.findFileNoChunks(path);

          if (noChunks < 0) {
              System.out.println("File not found?");
              return;
          }

          System.out.println("Found fileID: " + fileid + " \nwith " + noChunks + " chunks.");

          if (fileid == null){
              System.out.println("This file hasn't been backed up.");
              return;
          }

          int currentChunk = 0;

          while (currentChunk < noChunks)
          {
              this.restoreSubprotocol = new RestoreSubprotocol(this.peer, fileid, currentChunk);
              this.restoreSubprotocol.start();
              currentChunk++;
          }
      } catch(Exception e){
          System.err.println("Couldn't execute restore subprotocol");
      }
  }

}
