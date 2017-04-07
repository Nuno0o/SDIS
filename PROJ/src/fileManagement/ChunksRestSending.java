package fileManagement;

import java.util.ArrayList;

import fileManagement.ChunksSending.ChunkSent;

public class ChunksRestSending {
	public static class ChunkRestSent{
		String fileid;
		int chunkNo;
		int nConfirmations;
		public ChunkRestSent(String id,int chunkNo){
			this.fileid = id;
			this.chunkNo = chunkNo;
			this.nConfirmations = 0;
		}
	}
	public static ArrayList<ChunkRestSent> chunks = new ArrayList<ChunkRestSent>();

	public static void add(String fileid, int chunkNo){
		chunks.add(new ChunkRestSent(fileid,chunkNo));
	}

	public static void remove(String fileid,int chunkNo){
		for(int i = 0;i < chunks.size();i++){
			if(chunks.get(i).fileid == fileid && chunks.get(i).chunkNo == chunkNo){
				chunks.remove(i);
			}
		}
	}

	public static Boolean incrementResponses(String fileid, int chunkNo){
		for(int i = 0;i < chunks.size();i++){
			if(chunks.get(i).fileid.equals(fileid) && chunks.get(i).chunkNo == chunkNo){
				chunks.get(i).nConfirmations++;
				return true;
			}
		}
		return false;
	}

	public static Boolean hasResponses(String fileid,int chunkNo){
		for(int i = 0;i < chunks.size();i++){
			if(chunks.get(i).fileid == fileid && chunks.get(i).chunkNo == chunkNo){
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
