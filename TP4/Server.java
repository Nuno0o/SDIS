import java.io.*;
import java.net.*;

public class Server {
	public static void main(String [ ] args) throws Exception{
		//Start thread
		new ServerThread(args[0]).start();
	}
}
