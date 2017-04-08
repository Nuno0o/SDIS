package fileManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import utilities.Constants;

public class Metadata {
	public static class FileMetadata{
		public String fpath;
		public String fid;
		public long fsize;
		public FileMetadata(String fpath,String fid,long fsize){
			this.fpath = fpath;
			this.fid = fid;
			this.fsize = fsize;
		}
	}
	public static String filename = "metadata.txt";
	public static ArrayList<FileMetadata> metad = new ArrayList<FileMetadata>();
	
	public synchronized static void load(){
		metad.clear();
		//each line read
		String line = null;
		try{
			FileReader reader = new FileReader(filename);
			BufferedReader bufreader = new BufferedReader(reader);
			while((line = bufreader.readLine()) != null){
				String[] splitline = line.split(":");
				FileMetadata metadata = new FileMetadata(splitline[0],splitline[1],Integer.parseInt(splitline[2]));
				metad.add(metadata);
			}
			bufreader.close();
		}catch(Exception e){
		}
	}
	
	public synchronized static void store(){
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			for(int i = 0;i < metad.size();i++){
				pw.println(metad.get(i).fpath + ":" + metad.get(i).fid + ":" + metad.get(i).fsize);
			}
			pw.close();
		}catch(Exception e){
			System.out.println("Error printing store message");
		}
	}
	
	public synchronized static void saveMetadata(String path, String fileid){
		load();
		FileMetadata newMetad = new FileMetadata(path,fileid,new File(path).length());
		metad.add(newMetad);
		store();
	}
	
	public synchronized static String findMetadata(String path){
		load();
		String ret = null;
		for(int i = 0;i < metad.size();i++){
			if(metad.get(i).fpath.equals(path)){
				ret = metad.get(i).fid;
			}
		}
		store();
		return ret;
	}
	
    public synchronized static int findFileNoChunks(String path){
    	load();
    	long ret = -1;
    	for(int i = 0;i < metad.size();i++){
    		if(metad.get(i).fpath.equals(path)){
    			ret = metad.get(i).fsize / Constants.MAX_BODY_SIZE + 1;
    		}
    	}
    	store();
    	return (int)ret;
    }
    
    public synchronized static void removeMetaData(String fid){
    	load();
    	for(int i = 0;i < metad.size();i++){
    		if(metad.get(i).fid.equals(fid)){
    			metad.remove(i);
    			i--;
    		}
    	}
    	store();
    	return;
    }
}
