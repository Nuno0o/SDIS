package fileManagement;

import utilities.Constants;
import java.io.*;
import java.security.MessageDigest;

public class Chunker {
	//IO file variables
	public File file;
	public BufferedReader br;
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
	public Chunker(String path, int repDeg){
		try{
			this.path = path;
			this.repDeg = repDeg;
			this.file = new File(path);
			this.br = new BufferedReader(new FileReader(this.path));
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
		}

		this.chunkNo = 0;
		this.chunkRead = 0;
	}

	
	public void close(){
		try{
			this.br.close();
		}catch(Exception e){

		}
	}

	public FileChunk nextChunk(){
		//Data array
		char[] data = new char[Constants.MAX_BODY_SIZE];
		//Length read from file
		int length = -1;
		//Read data from file

		System.out.println(chunkRead + " : " + chunkNo);

		try{
			length = this.br.read(data,0,Constants.MAX_BODY_SIZE);
		}catch(Exception e){
			System.out.println("Error reading file");
		}
		if(length < Constants.MAX_BODY_SIZE){
			char[] data2 = new char[length];
			for(int i = 0;i < length;i++){
				data2[i] = data[i];
			}
			data = data2;
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
