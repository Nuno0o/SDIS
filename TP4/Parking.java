import java.util.ArrayList;

public class Parking {
	//Veiculo
	public class Vehicle{
		String plate;
		String owner;

		public Vehicle(String plate,String owner){
			this.plate = plate;
			this.owner = owner;
		}
	}
	//List of vehicles
	ArrayList<Vehicle> vehicles;
	public Parking(){
		this.vehicles = new ArrayList<Vehicle>();
	}

	public String addVehicle(String plate,String owner){
		Vehicle vehicle = new Vehicle(plate,owner);
		for(int i = 0;i < this.vehicles.size();i++){
			if(this.vehicles.get(i).plate.equals(plate)){
				return "" + -1;
			}
		}
		this.vehicles.add(vehicle);
		return "" + this.vehicles.size();
	}

	public String lookupVehicle(String plate){
		for(int i = 0;i < this.vehicles.size();i++){
			if(this.vehicles.get(i).plate.equals(plate)){
				return this.vehicles.get(i).owner;
			}
		}
		return "NOT_FOUND";
	}
}
