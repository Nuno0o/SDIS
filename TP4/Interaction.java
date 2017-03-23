import java.rmi.*;

public interface Interaction extends Remote{

  String processRequest(String request) throws RemoteException;

}
