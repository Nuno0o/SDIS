package fileManagement;

import java.util.Hashtable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


public class FileRestoring {
	//file id
	public static String fileid;
	//path to future file
	public static String path;
	//number of chunks
	public static int nchunks;
	//maps chunk index to it's path
	public static Hashtable<Integer,String> chunksReceived;
	//check if file was restored
	public static boolean restored;
	
	public static void init(String fileid,String path,int nchunks){
		FileRestoring.fileid = fileid;
		FileRestoring.path = path;
		FileRestoring.nchunks = nchunks;
		FileRestoring.chunksReceived = new Hashtable<Integer,String>();
		FileRestoring.restored = false;
	}
	
	public synchronized static void addReceived(int chunkNo,String path){
		chunksReceived.put(chunkNo, path);
		if(checkComplete() && restored == false){
			FileRestoring.restore();
		}
	}
	
	public synchronized static void restore(){
		DataOutputStream os = null;
		try{
			os = new DataOutputStream(new FileOutputStream(path));
		}catch(Exception e){
			
		}
		
		for(int i = 0;i < nchunks;i++){
			byte[] data = loadFromFile(chunksReceived.get(i));
			try{
				os.write(data);
			}catch(Exception e){
				
			}
		}
		try{
			os.close();
		}catch(Exception e){
			
		}
		restored = true;
	}
	
	public synchronized static boolean checkComplete(){
		for(int i = 0;i < nchunks;i++){
			if(chunksReceived.get(i) == null){
				return false;
			}
		}
		return true;
	}
	
	public static byte[] loadFromFile(String path){
		Path pathd = Paths.get(path);
		byte[] data = null;
		try{
			data = Files.readAllBytes(pathd);
		}catch(Exception e){
			
		}
		
		return data;
	}
}
