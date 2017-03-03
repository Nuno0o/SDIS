import java.io.*;
import java.net.*;

public class Server {
	public static void main(String [ ] args) throws Exception{

		//Port Number
		Integer port1 = Integer.parseInt(args[0]);
		//Start thread
		new ServerThread(port1).start();
	}
}
