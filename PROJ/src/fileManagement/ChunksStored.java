package fileManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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
	public static ArrayList<ChunkInfo> list = new ArrayList<ChunkInfo>();
	//
	public static void load(){
		list.clear();
		//each line read
		String line = null;
		try{
			FileReader reader = new FileReader(filename);
			BufferedReader bufreader = new BufferedReader(reader);
			while((line = bufreader.readLine()) != null){
				String[] splitline = line.split(";");
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
		String line = new String();

		try{
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			for(int i = 0;i < list.size();i++){
				System.out.println(list.size());
				pw.println(list.get(i).fileid + ";" + list.get(i).chunkNo + ";" + list.get(i).filehash + ";" + list.get(i).RepDegree + ";" + list.get(i).realRepDegree);
			}
			pw.close();
		}catch(Exception e){
			System.out.println("Error printing store message");
		}
	}
	public static Boolean addNew(FileChunk chunk){
		load();
		for(int i = 0;i < list.size();i++){
			if(list.get(i).fileid == chunk.fileId && list.get(i).chunkNo == chunk.chunkNo){
				return false;
			}
		}
		ChunkInfo info = new ChunkInfo();
		info.fileid = chunk.fileId;
		info.chunkNo = chunk.chunkNo;
		info.filehash = chunk.fileId + ":" + chunk.chunkNo;
		info.RepDegree = chunk.repDeg;
		info.realRepDegree = 1;//if this peer contains the file, then the realrepdegree starts at 1, increments each time a store message is received
		list.add(info);
		store();
		return true;
	}
	//Inc replication degree of chunk
	public static boolean incDegree(String fileid,int chunkno){
		load();
		for(int i = 0; i < list.size();i++){
			if(list.get(i).fileid == fileid && list.get(i).chunkNo == chunkno){
				list.get(i).realRepDegree++;
				store();
				return true;
			}
		}
		return false;
	}
	//Returns true if at least 1 chunk has been deleted
	public static boolean deleteFile(String fileid){
		load();
		boolean ret = false;
		for(int i = 0;i < list.size();i++){
			if(list.get(i).fileid == fileid){
				try{
					File f = new File(list.get(i).filehash);
					f.delete();
					ret = true;
				}catch(Exception e){

				}
			}
		}
		store();
		return ret;
	}
}
