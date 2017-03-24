package services;

public class FileManager {
	Peer peer;
	public FileManager(Peer peer){
		this.peer = peer;
	}
	
	public Boolean storeChunk(String name,String data){
		return true;
	}
}
