import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

	public DatagramSocket socket1 = null;
	public boolean morePlates = true;
	public Parking park = new Parking();
	public InetAddress addrmcst;
	public Integer portmcst;
	public InetAddress addr;
	public Integer port;



	public ServerThread(int port,String addrmcast,int portmcast) throws IOException{
			super("Servidor");

			this.addrmcst = InetAddress.getByName(addrmcast);
			this.portmcst = portmcast;

			// abrir socket com port number introduzido
			socket1 = new DatagramSocket(port);

			this.addr = InetAddress.getByName("localhost");
			this.port = port;
	}

	public void run(){

		while(morePlates){
			try{
				byte[] buf = new byte[256];
				byte[] buf2 = new byte[256];

				//broadcast server ip
				String toBuf2 = this.addr + ":" + this.port;
				buf2 = toBuf2.getBytes();

				DatagramPacket broadcast = new DatagramPacket(buf2,buf2.length,addrmcst,portmcst);
				socket1.send(broadcast);
				
				System.out.println("multicast: " + this.addrmcst + " " + this.portmcst + " : " + this.addr + " " + this.port);

				//Receive request
				DatagramPacket packet = new DatagramPacket(buf,buf.length);
				socket1.setSoTimeout(1000);
				socket1.receive(packet);

				//Do the job
				String plateAndOwner = new String(packet.getData());

				System.out.println("A cowboy as answered my call :" + plateAndOwner);

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
				socket1.send(packet);

			}catch(Exception e){

			}
		}
		socket1.close();
	}
}
