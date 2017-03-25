package fileManagement;
import java.security.MessageDigest;

public class FileChunk {
	public String fileId;
	public String fileName;
	public int chunkNo;
	public byte[] data;
	public int repDeg;

	public FileChunk(String filename,byte[] data,int chunkNo,int repDeg){
		//File name
		this.fileName = filename;
		//File data
		this.data = data;
		//File chunk no
		this.chunkNo = chunkNo;
		//Desired repdeg
		this.repDeg = repDeg;

		//SHA-256 file id
		MessageDigest md;
		try{
			md = MessageDigest.getInstance("SHA-256");
			md.update(filename.getBytes());
			this.fileId = md.digest().toString();
		}catch(Exception e){

		}
	}

	public FileChunk(String fileId, byte[] data, int chunkNo, int repDeg, boolean alreadyId){

		if (alreadyId) {
		this.fileName = "";
		this.data = data;
		this.chunkNo = chunkNo;
		this.repDeg = repDeg;
		this.fileId = fileId;
		}
	}
}
