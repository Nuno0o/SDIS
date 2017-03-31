package fileManagement;

public class FileChunk {
	public String fileId;
	public int chunkNo;
	public byte[] data;
	public int repDeg;

	public FileChunk(String fileid,byte[] data,int chunkNo,int repDeg){
		//File data
		this.data = data;
		//File chunk no
		this.chunkNo = chunkNo;
		//Desired repdeg
		this.repDeg = repDeg;
		//File id
		this.fileId = fileid;
	}


}
