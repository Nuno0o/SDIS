package fileManagement;
import java.security.MessageDigest;

public class FileChunk {
	public String fileId;
	public String fileName;
	public int chunkNo;
	public byte[] data;

	public FileChunk(String filename,byte[] data,int chunkNo){
		//File name
		this.fileName = filename;
		//File data
		this.data = data;
		//File chunk no
		this.chunkNo = chunkNo;
		//SHA-256 file id
		MessageDigest md;
		try{
			md = MessageDigest.getInstance("SHA-256");
			md.update(data);
			this.fileId = md.digest().toString();
		}catch(Exception e){

		}
	}
}
