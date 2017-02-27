import java.io.*;
import java.util.ArrayList;
import java.net.*;

public class Client {

  private static MulticastSocket clientSocket;
  private static DatagramSocket clientDatagramSocket;
  private static DatagramPacket datagramPacket;
  private static InetAddress multicastAddress;
  private static InetAddress addr;
  private static int portNo;
  private static int portNumber;

  private static String msg;
  private static byte[] buffer;

  public static void initRequirements(String hostName, String portString, String message) throws Exception{

      clientDatagramSocket = new DatagramSocket();

      // Inicializar um buffer
      buffer = new byte[512];

      // Decode do endereco obtido por argumento
      multicastAddress = InetAddress.getByName(hostName);

      // decode do port number
      portNumber = Integer.parseInt(portString);

      // Inicializar um packet, com length escolhida no buffer
      datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, multicastAddress, portNumber);

      clientSocket = new MulticastSocket(portNumber);
      clientSocket.setTimeToLive(1);

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

      // Inicializar socket, packet, endereco
      initRequirements(args[0], args[1], msg);

      // join multicast group
      clientSocket.joinGroup(multicastAddress);

      System.out.println("Address: " + multicastAddress + ":" + portNumber);
      // send request
      //clientSocket.send(datagramPacket);

      boolean done = false;

      while (!done){

        // reset packet object
        datagramPacket = new DatagramPacket(buffer, buffer.length);

        // receive response
        clientSocket.receive(datagramPacket);

        String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());

        String[] decoded = received.split(":");
        String[] decodedAddr = decoded[0].split("/");

        portNo = Integer.parseInt(decoded[1]);
        addr = InetAddress.getByName(decodedAddr[1]);
        datagramPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, portNo);

        clientDatagramSocket.send(datagramPacket);

        datagramPacket = new DatagramPacket(buffer, buffer.length);
        clientDatagramSocket.receive(datagramPacket);

        received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println(msg + ": " + received);

        done = true;
      }

      // done talking, let's leave
      clientSocket.leaveGroup(multicastAddress);

      // close the socket used
      clientSocket.close();
  }

}
