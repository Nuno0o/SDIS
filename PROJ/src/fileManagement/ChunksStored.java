package fileManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ChunksStored {
	public static class ChunkInfo{
		//Formato fileid:chunkNo:filehash:repdegree:realrep
		String fileid;//chunk id
		int chunkNo;//chunk number
		String filehash;//tiny file name
		int RepDegree;//minimum rep degree desired
		int realRepDegree;//real rep degree obtained by counting the STORED messages
		
		public ChunkInfo(){
		}
	}
	//list of chunks stored
	public static String filename = "storedchunks.txt";
	public static ArrayList<ChunkInfo> list;
	//
	public static void load(){
		list.clear();
		//each line read
		String line = null;
		try{
			FileReader reader = new FileReader(filename);
			BufferedReader bufreader = new BufferedReader(reader);
			while((line = bufreader.readLine()) != null){
				String[] splitline = line.split(":");
				ChunkInfo chunk = new ChunkInfo();
				chunk.fileid = splitline[0];
				chunk.chunkNo = Integer.parseInt(splitline[1]);
				chunk.filehash = splitline[2];
				chunk.RepDegree = Integer.parseInt(splitline[3]);
				chunk.realRepDegree = Integer.parseInt(splitline[4]);
				list.add(chunk);
			}
			bufreader.close();
		}catch(Exception e){
			
		}
	}
	public static void store(){
		String line = null;
		
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			for(int i = 0;i < list.size();i++){
				pw.println(list.get(i).fileid + ":" + list.get(i).chunkNo + ":" + list.get(i).filehash + ":" + list.get(i).RepDegree + ":" + list.get(i).realRepDegree);
			}
		}catch(Exception e){
			
		}
	}
	public static void addNew(FileChunk chunk,String filename){
		ChunkInfo info = new ChunkInfo();
		info.fileid = chunk.fileId;
		info.chunkNo = chunk.chunkNo;
		info.filehash = filename;
		info.RepDegree = chunk.repDeg;
		info.realRepDegree = 1;//if this peer contains the file, then the realrepdegree starts at 1, increments each time a store message is received
		list.add(info);
		store();
	}
}