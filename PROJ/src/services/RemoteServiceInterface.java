package services;

import java.rmi.*;

public interface RemoteServiceInterface extends Remote{

    // The backup execution method
    public void backup(String path, int repDeg) throws RemoteException;

    // The restore execution method
    //public void restore();

    // The delete execution method
    //public void delete(String path);

    // The reclaim execution method
    //public void reclaim();

}
