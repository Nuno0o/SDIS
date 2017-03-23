import java.io.*;
import java.util.ArrayList;
import java.net.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client implements Interaction{

  private static String msg;

  public String processRequest(String request){
    return "";
  }

  public static boolean checkOper(String[] args){
      if (!(args[2].equals("register") || args[2].equals("lookup"))) return false;
      if (args[2].equals("register")){
        if (args.length != 5) return false;
      }
      if (args[2].equals("lookup")){
        if (args.length != 4) return false;
      }
      return true;
  }

  public static boolean isGoodPlate(String plate){
      String[] parts = plate.split("-");
      if (parts.length != 3) return false;
      for( String part : parts){
        if (!part.matches("\\w\\w") && !part.matches("\\d\\d")) return false;
      }
      return true;
  }

  public static String getRequestMsg(String[] args){

    ArrayList<String> untreatedMsg = new ArrayList<String>();
    for (int i = 0; i < args.length; i++){
      if (i == 0 || i == 1) continue;
      if (i == 2) {
        untreatedMsg.add(args[i].toUpperCase());
        continue;
      }
      else untreatedMsg.add(args[i]);
    }

    String retorno = new String();
    for (String s : untreatedMsg){
      retorno += s + " ";
    }
    return retorno;
  }

  public static void main (String args[]) throws Exception{

      // verificar argumentos
      if (args.length < 4 || args.length > 5){
        System.out.println("ERROR: Invalid number of arguments.");
        return;
      }

      String host = new String();

      if (args[0] != null) host = args[0];
      else {
        System.err.println("host name is null!");
        System.exit(1);
      }

      // verificar se a operacao esta correta
      if (!checkOper(args)){
        System.out.println("ERROR: Invalid operation submitted.");
        return;
      }

      // verificar plate
      if (!isGoodPlate(args[3])){
        System.out.println("ERROR: " + args[3] + " is not a valid plate!");
        return;
      }

      msg = getRequestMsg(args);

      try {

        Registry registry = LocateRegistry.getRegistry(host);
        Interaction stub = (Interaction) registry.lookup(args[1]);

        String received = new String();
        received = stub.processRequest(msg);
        System.out.println("response: " + received..);
        System.out.println(msg + ": " + received);

      } catch (Exception e){
        System.err.println("Client exception: " + e.toString());
        e.printStackTrace();
      }
  }

}
