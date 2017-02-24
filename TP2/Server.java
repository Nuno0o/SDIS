import java.io.*;
import java.net.*;

public class Server {
	public static void main(String args[]) throws Exception{

		//Port Number
		Integer port1 = Integer.parseInt(args[0]);
		
		//Mcast addr
		String mcast = args[1];
		
		//Mcast port
		Integer portmcast = Integer.parseInt(args[2]);
		
		//Start thread
		new ServerThread(port1,mcast,portmcast).start();
	}
}
