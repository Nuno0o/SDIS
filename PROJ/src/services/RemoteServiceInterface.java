package services;

import java.rmi.*;

public interface RemoteServiceInterface extends Remote{

  // The backup execution method
  public boolean backup();

  // The restore execution method
  //public void restore();

  // The delete execution method
  //public void delete();

  // The reclaim execution method
  //public void reclaim();

}
