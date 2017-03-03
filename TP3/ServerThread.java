import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

	public DatagramSocket socket = null;
	public boolean morePlates = true;
	public Parking park = new Parking();

	public ServerThread(int port) throws IOException{
			super("Servidor");

			// abrir socket com port number introduzido
			socket = new DatagramSocket(port);
	}

	public void run(){

		System.out.println("Server running cowboy");

		while(morePlates){
			try{
				byte[] buf = new byte[256];

				//Receive request
				DatagramPacket packet = new DatagramPacket(buf,buf.length);
				socket.receive(packet);

				//Do the job
				String plateAndOwner = new String(packet.getData());

				System.out.println(plateAndOwner);

				String[] parts = plateAndOwner.split("\\s+");

				String ret;
				if(parts[0].equals("REGISTER")){
					ret = this.park.addVehicle(parts[1], parts[2]);
					buf = ret.getBytes();
				}else if(parts[0].equals("LOOKUP")){
					ret = this.park.lookupVehicle(parts[1]);
					buf = (parts[1] + " " + ret).getBytes();
				}

				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buf,buf.length,address,port);
				socket.send(packet);

			}catch(IOException e){
				morePlates = false;
			}
		}
		socket.close();
	}
}
