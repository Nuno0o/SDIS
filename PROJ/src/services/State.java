package services;

import java.io.Serializable;
import java.util.ArrayList;

import fileManagement.ChunksStored;
import fileManagement.Metadata;

public class State implements Serializable {
	public ArrayList<String> filepath;
	public ArrayList<String> fileid;
	
	public ArrayList<String> chunkid;
	public ArrayList<Integer> size;
	public ArrayList<Integer> realrepdegree;
	
	public Integer peerNumber;
	public Integer maxSize;
	
	public State(Peer p){
		filepath = Metadata.getMetadPaths();
		fileid = Metadata.getMetadIds();
		
		chunkid = ChunksStored.getIds();
		size = ChunksStored.getLengths();
		realrepdegree = ChunksStored.getRealRepd();
		
		maxSize = p.storageSpace;
		peerNumber = p.peerNumber;
	}
	
	public void display(){
		System.out.println("Printing information for peer no." + peerNumber + " (" + maxSize + " bytes max storage)");
		System.out.println("Files stored:");
		System.out.println("Path ; File ID");
		for(int i = 0;i < filepath.size();i++){
			System.out.println(filepath.get(i) + " ; " + fileid.get(i));
		}
		System.out.println("Chunks stored:");
		System.out.println("Chunk ID ; Size in bytes ; Perceived replication degree");
		for(int i = 0;i < chunkid.size();i++){
			System.out.println(chunkid.get(i) + " ; " + size.get(i) + " ; " + realrepdegree.get(i));
		}
	}
	
	
}
