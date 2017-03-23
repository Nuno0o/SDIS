package fileManagement;

import utilities.Constants;
import java.io.*;

public class Chunker {
	//IO file variables
	File file;
	FileInputStream in = null;
	FileOutputStream out = null;

	//Chunk number
	int chunkNo;
	//How much has been read
	int chunkRead;

	public Chunker(String path){
		try{
			this.file = new File(path);
			this.in = new FileInputStream(this.file);
			this.out = new FileOutputStream(this.file);
		}catch(Exception e){
			System.err.println("Can't open file.");
		}

		this.chunkNo = 0;
		this.chunkRead = 0;
	}

	public FileChunk nextChunk(){
		//Data array
		byte[] data = new byte[Constants.MAX_BODY_SIZE];
		//Length read from file
		int length;
		//Read data from file
		try{
			length = in.read(data,chunkRead,Constants.MAX_BODY_SIZE);
		}catch(Exception e){
			System.out.println("Error reading file");
			return null;
		}
		//Create chunk
		FileChunk chunk = new FileChunk(file.getName() + ":" + System.currentTimeMillis(),data,this.chunkNo);
		//Increment chunk counter
		chunkNo++;
		//Increment length read
		chunkRead += length;

		return chunk;
	}
}
