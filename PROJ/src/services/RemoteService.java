package services;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteService extends UnicastRemoteObject implements RemoteServiceInterface{

  public int peerNo;

  public RemoteService(Peer peer) throws RemoteException{
    this.peerNo = peer.peerNumber;
  }

  public boolean backup(){
    return true;
  }

}
