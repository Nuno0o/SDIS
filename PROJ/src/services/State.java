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

		System.out.println("\n\n   -----------------------------------------------------------------");
		System.out.println("   --------------------------- PEER NO. "+peerNumber+" --------------------------");
		System.out.println("   -----------------------------------------------------------------");
		System.out.println("   ---");
		System.out.println("   --->        Max Storage Space: " + maxSize + " bytes ");
		System.out.println("   --->        Currently Used: " + ChunksStored.getSpaceUsed() + " bytes (" + ChunksStored.getSpaceUsed() / maxSize + "% used)");
		System.out.println("   ---");

		if (filepath.size() == 0){
			System.out.println("   --->        No files have been backed up from this peer!");
			System.out.println("   ---");
			System.out.println("   -----------------------------------------------------------------");
		}else {
			System.out.println("   --->        Files stored:");
			System.out.println("   ---");
			for(int i = 0;i < filepath.size();i++){
				System.out.println("   --->   Path: " + filepath.get(i));
				System.out.println("   ---       Id:" + fileid.get(i));
			}
		}

		if (size.size() == 0){
			System.out.println("   ---");
			System.out.println("   --->        No chunks have been backed up in this peer!");
			System.out.println("   ---");
			System.out.println("   -----------------------------------------------------------------");
		}
		else {
			System.out.println("   ---");
			System.out.println("   --->        Chunks stored:");
			System.out.println("   ---");
			for(int i = 0;i < chunkid.size();i++){
				System.out.println("   --->   Chunk:");
				System.out.println("   ---            No:"+chunkid.get(i).split(":")[1]);
				System.out.println("   ---            ID:"+ chunkid.get(i).split(":")[0]);
				System.out.println("   ---            Size (bytes): " + size.get(i));
				System.out.println("   ---            Perceived replication degree: " + realrepdegree.get(i));
				System.out.println("   ---");
			}
		}

		System.out.println("   -----------------------------------------------------------------");
		System.out.println("   -----------------------------------------------------------------\n\n");
	}


}
