package fileManagement;

import utilities.Constants;
import java.io.*;
import java.security.MessageDigest;
import java.util.Arrays;

public class Chunker {
	//IO file variables
	public File file;
	public FileInputStream fis;
	//Path to file
	public String path;
	//Chunk number
	public int chunkNo;
	//How much has been read
	public int chunkRead;
	//Replication degree
	public int repDeg;
	//File id
	public String fileid;
	//Constructor
	public Chunker(String path, int repDeg){
		try{
			this.path = path;
			this.repDeg = repDeg;
			this.file = new File(path);
			this.fis = new FileInputStream(this.file);
			//SHA-256 file id
			String filename = file.getName() + ":" + System.currentTimeMillis();
			MessageDigest md;
				md = MessageDigest.getInstance("SHA-256");
				md.update(filename.getBytes());
				byte[] fileIdBytes = md.digest();
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < fileIdBytes.length; i++){
					b.append(String.format("%02X", fileIdBytes[i]));
				}
				this.fileid = b.toString();
		}catch(Exception e){
			System.err.println("Can't open file.");
			file = null;
			fis = null;
		}

		this.chunkNo = 0;
		this.chunkRead = 0;
	}
	public boolean failedToOpen(){
		return file == null;
	}


	public void close(){
		try{
			this.fis.close();
		}catch(Exception e){

		}
	}

	public FileChunk nextChunk(){
		//Data array
		byte[] data = new byte[Constants.MAX_BODY_SIZE];
		//Length read from file
		int length = -1;

		try{
			length = this.fis.read(data, 0, Constants.MAX_BODY_SIZE);
		}catch(Exception e){
			System.out.println("Error reading file");
		}
		byte[] data2 = Arrays.copyOf(data, length);
		data = data2;
		//Create chunk
		FileChunk chunk = new FileChunk(fileid,data,this.chunkNo,this.repDeg);
		//Increment chunk counter
		chunkNo++;
		//Increment length read
		chunkRead += length;

		return chunk;
	}
}
