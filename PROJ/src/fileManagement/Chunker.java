package fileManagement;

import utilities.Constants;
import java.io.*;
import java.security.MessageDigest;

public class Chunker {
	//IO file variables
	File file;
	BufferedReader br;

	//Chunk number
	int chunkNo;
	//How much has been read
	int chunkRead;

	int repDeg;

	public Chunker(String path, int repDeg){
		try{
			this.repDeg = repDeg;
			this.file = new File(path);
			this.br = new BufferedReader(new FileReader(path));
		}catch(Exception e){
			System.err.println("Can't open file.");
		}

		this.chunkNo = 0;
		this.chunkRead = 0;
	}

	public FileChunk nextChunk(){
		//Data array
		char[] data = new char[Constants.MAX_BODY_SIZE];
		//Length read from file
		int length;
		//Read data from file
		try{
			length = this.br.read(data,chunkRead,Constants.MAX_BODY_SIZE);
		}catch(Exception e){
			System.out.println("Error reading file");
			return null;
		}
		//SHA-256 file id
		String filename = file.getName() + ":" + System.currentTimeMillis();
		String fileid = "";
		MessageDigest md;
		try{
			md = MessageDigest.getInstance("SHA-256");
			md.update(filename.getBytes());
			byte[] fileIdBytes = md.digest();
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < fileIdBytes.length; i++){
				b.append(String.format("%02X", fileIdBytes[i]));
			}
			fileid = b.toString();

		}catch(Exception e){
		}
		//Create chunk
		
		FileChunk chunk = new FileChunk(fileid,new String(data).getBytes(),this.chunkNo,this.repDeg);
		//Increment chunk counter
		chunkNo++;
		//Increment length read
		chunkRead += length;

		return chunk;
	}
}
