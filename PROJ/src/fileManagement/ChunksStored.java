package fileManagement;

import utilities.Constants;

import java.io.BufferedReader;
import java.io.File;
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
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			for(int i = 0;i < list.size();i++){
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
			if(list.get(i).fileid.equals(chunk.fileId) && list.get(i).chunkNo == chunk.chunkNo){
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
			if(list.get(i).fileid.equals(fileid) && list.get(i).chunkNo == chunkno){
				list.get(i).realRepDegree++;
				store();
				return true;
			}
		}
		return false;
	}

	public static int getRepDeg(String fileid, int chunkNo){
		load();
		for (int i = 0; i < list.size(); i++){
			if(list.get(i).fileid.replaceAll("(\\r|\\n)","").equals(fileid)){
				if (list.get(i).chunkNo == chunkNo){
					return list.get(i).RepDegree;
				}

			}
		}
		return -1;
	}

	//Returns true if at least 1 chunk has been deleted
	public static boolean deleteFile(String fileid){
		load();
		boolean ret = false;
		for(int i = 0;i < list.size();i++){
			System.out.println(list.get(i).fileid + ":" + list.get(i).chunkNo);
			if(list.get(i).fileid.replaceAll("(\\r|\\n)","").equals(fileid)){
				try{

					File f = new File(list.get(i).filehash);
					f.delete();
					ret = true;
					list.remove(i);
					i--;
				}catch(Exception e){

				}
			}
		}
		store();
		return ret;
	}

	public static boolean decRepDeg(String fileid, int chunkNo){
		load();
		boolean success = false;
		for (int i = 0; i < list.size(); i++){
			if(list.get(i).fileid.replaceAll("(\\r|\\n)","").equals(fileid)){
				if (list.get(i).chunkNo == chunkNo){
					list.get(i).RepDegree--;
					success = true;
				}

			}
		}
		if (success) store();
		return success;
	}

	public static boolean lessThanReal(String fileid, int chunkNo){
		load();
		for (int i = 0; i < list.size(); i++){
			if(list.get(i).fileid.replaceAll("(\\r|\\n)","").equals(fileid)){
				if (list.get(i).chunkNo == chunkNo){
					if(list.get(i).realRepDegree < list.get(i).RepDegree)
						return true;
				}
			}
		}
		return false;
	}

	// Returns chunks for a given file
	public static ArrayList<String> getChunksStored(String fileid){
		load();
		ArrayList<String> toReturn = new ArrayList<String>();
		for(int i = 0;i < list.size();i++){
			if(list.get(i).fileid.replaceAll("(\\r|\\n)","").equals(fileid)){
				toReturn.add(list.get(i).fileid + ":" + list.get(i).chunkNo);
			}
		}
		return toReturn;
	}

	public static char[] getChunkData(String fileid, int chunkNo){
		load();
		for(int i = 0;i < list.size();i++){
			System.out.println(list.get(i).fileid + ":" + list.get(i).chunkNo);
			if(list.get(i).fileid.replaceAll("(\\r|\\n)","").equals(fileid) && list.get(i).chunkNo == chunkNo){
				try{
					FileReader reader = new FileReader(new String(fileid+":"+chunkNo));
					BufferedReader bufreader = new BufferedReader(reader);

					char[] data = new char[Constants.MAX_BODY_SIZE];
 					bufreader.read(data,0,Constants.MAX_BODY_SIZE);
					return data;

				}catch(Exception e){
					System.err.println("Exception");
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static int getSpaceUsed(){
		load();
		int retorno = 0;
		for(int i = 0;i < list.size();i++){
			retorno += new File(list.get(i).fileid+":"+list.get(i).chunkNo).length();
		}
		return retorno;
	}

}
