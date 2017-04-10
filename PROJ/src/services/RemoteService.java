package services;

import fileManagement.Chunker;
import fileManagement.FileChunk;
import fileManagement.FileRestoring;
import fileManagement.ChunksStored;
import fileManagement.Metadata;
import subprotocols.BackupSubprotocol;
import subprotocols.DeleteSubprotocol;
import subprotocols.RestoreSubprotocol;
import subprotocols.ReclaimSubprotocol;
import utilities.Constants;
import utilities.RandomDelay;

import java.util.TreeMap;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteService extends UnicastRemoteObject implements RemoteServiceInterface{

  public Peer peer;
  public Chunker chunker;
  public DeleteSubprotocol deleteSubprotocol;
  public RestoreSubprotocol restoreSubprotocol;

  public RemoteService(Peer peer) throws RemoteException{
    this.peer = peer;
  }

  // The method that implements the backup.
  public void backup(String path, int repDeg){

      System.out.println("Backing up file in :" + path);

      this.chunker = new Chunker(path,repDeg);
      
      if(chunker.failedToOpen()){
    	  return;
      }

      FileChunk currentChunk = this.chunker.nextChunk();

      Metadata.saveMetadata(path,chunker.fileid);

      while (currentChunk != null) {

          System.out.println("Im here");

          if (currentChunk.getSpace() > (this.peer.storageSpace - ChunksStored.getSpaceUsed())){
              System.out.println("Couldn't store chunk: not enough space!");
              break;
          }

          new BackupSubprotocol(this.peer, currentChunk, repDeg).start();

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
              System.out.println("File not found");
              return;
          }

          System.out.println("Found fileID: " + fileid + " \nwith " + noChunks + " chunks.");

          if (fileid == null){
              System.out.println("This file hasn't been backed up.");
              return;
          }

          FileRestoring.init(fileid, path, noChunks);

          //Create threads that will request the chunks
          int currentChunk = 0;

          while (currentChunk < noChunks)
          {
              this.restoreSubprotocol = new RestoreSubprotocol(this.peer, fileid, currentChunk);
              this.restoreSubprotocol.start();
              currentChunk++;
              try{
      			Thread.sleep(5);
      		}catch(Exception e){

      		}
          }
      } catch(Exception e){
          System.err.println("Couldn't execute restore subprotocol");
      }
  }

  public void manageStorage (int kbs) {

      int spaceUsed = ChunksStored.getSpaceUsed();

      System.out.println("Storage space: " + this.peer.storageSpace + "; used: " + spaceUsed);

      if (spaceUsed <= kbs){
          System.out.println("setting storage from: " + this.peer.storageSpace + " to " + kbs);
        this.peer.storageSpace = kbs;
        return;
      }
      // else start reclaiming

      TreeMap<String, Integer> map = ChunksStored.getOrderedRepDegs();

      if (map == null) return;
      System.out.println("Mapsize: " + map.size());

      int tries = 5;
      while (tries != 0){
          System.out.println(map.lastEntry());
              String[] splitStr = map.lastEntry().getKey().split(":");
              ChunksStored.deleteChunk(splitStr[0], Integer.parseInt(splitStr[1]));
              new ReclaimSubprotocol(this.peer, splitStr[0], Integer.parseInt(splitStr[1])).start();
              System.out.println("Removed: " + splitStr[0] + ":" + splitStr[1]);
              if (ChunksStored.getSpaceUsed() < kbs) return;
          tries--;
      }

      System.out.println("Couldn't reclaim.");

  }
  
  public State state(){
	  State state = new State(this.peer);
	  
	  return state;
  }

}
