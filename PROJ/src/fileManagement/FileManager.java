package fileManagement;

import services.Peer;

public class FileManager {

	public Peer peer;

	public FileManager(Peer peer){
		this.peer = peer;
	}

	public boolean storeChunk(String name, String data, int repDeg){
		return true;
	}
}
