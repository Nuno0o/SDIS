import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

	public ServerSocket srvsocket = null;
	public Socket socket = null;
	public boolean morePlates = true;
	public Parking park = new Parking();

	public ServerThread(int port) throws IOException{
			super("Servidor");

			// abrir socket com port number introduzido
			srvsocket = new ServerSocket(port);
	}

	public void run(){

		System.out.println("Server running cowboy");

		while(morePlates){
			try{
				String buf = new String();
				//Receive request
				socket = srvsocket.accept();

				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
				//Do the job
				String plateAndOwner = inFromClient.readLine();

				System.out.println(plateAndOwner);

				String[] parts = plateAndOwner.split("\\s+");

				String ret;
				if(parts[0].equals("REGISTER")){
					ret = this.park.addVehicle(parts[1], parts[2]);
					buf = ret;
				}else if(parts[0].equals("LOOKUP")){
					ret = this.park.lookupVehicle(parts[1]);
					buf = (parts[1] + " " + ret);
				}
				outToClient.writeBytes(buf);

			}catch(IOException e){
				morePlates = false;
			}
		}
		try{
			socket.close();
			srvsocket.close();
		}catch(Exception e){
			
		}
	}
}
