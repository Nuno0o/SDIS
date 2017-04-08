package fileManagement;

import java.util.ArrayList;

public class PutchunksSending {
	public static class PutchunkSent{
		String fileid;
		int chunkNo;
		int nConfirmations;
		public PutchunkSent(String id,int chunkNo){
			this.fileid = id;
			this.chunkNo = chunkNo;
			this.nConfirmations = 0;
		}
	}
	public static ArrayList<PutchunkSent> chunks = new ArrayList<PutchunkSent>();

	public synchronized static void add(String fileid, int chunkNo){
		chunks.add(new PutchunkSent(fileid,chunkNo));
	}

	public synchronized static void remove(String fileid,int chunkNo){
		for(int i = 0;i < chunks.size();i++){
			if(chunks.get(i).fileid.equals(fileid) && chunks.get(i).chunkNo == chunkNo){
				chunks.remove(i);
			}
		}
	}

	public synchronized static Boolean incrementResponses(String fileid, int chunkNo){
		for(int i = 0;i < chunks.size();i++){
			if(chunks.get(i).fileid.equals(fileid) && chunks.get(i).chunkNo == chunkNo){
				chunks.get(i).nConfirmations++;
				return true;
			}
		}
		return false;
	}

	public synchronized static Boolean hasResponses(String fileid,int chunkNo){
		for(int i = 0;i < chunks.size();i++){
			if(chunks.get(i).fileid.equals(fileid) && chunks.get(i).chunkNo == chunkNo){
				boolean ret = false;
				if(chunks.get(i).nConfirmations > 0)
					ret = true;
				chunks.remove(i);
				return ret;
			}
		}
		return false;
	}
}
