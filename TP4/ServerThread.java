import java.io.*;
import java.net.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerThread extends Thread implements Interaction{

	public boolean morePlates = true;
	public Parking park = new Parking();
	public String objName;

	public ServerThread(String name){
			super("Servidor");
			objName = name;
	}

	public String processRequest(String request){

		String plateAndOwner = new String(request);

		System.out.println(plateAndOwner);

		String[] parts = plateAndOwner.split("\\s+");

		String ret = new String();
		if(parts[0].equals("REGISTER")){
			ret = this.park.addVehicle(parts[1], parts[2]);
		}else if(parts[0].equals("LOOKUP")){
			ret = this.park.lookupVehicle(parts[1]);
			ret = parts[1] + " " + ret;
		}

		return ret;

	}

	public void run(){
		try{
			Interaction stub = (Interaction) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind(objName, stub);
			System.err.println("Server running cowboy");
		} catch (Exception e){
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}

	}
}
