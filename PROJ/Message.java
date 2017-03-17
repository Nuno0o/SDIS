public class Message {

  // <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>
  // <MessageType> - The subprotocol
  // <Version> - Unenhanced: 1.0
  // <SenderId> - The id of the server that has sent the message. variable length
  // <FileId> - SHA-256 of the file ID
  // <ChunkNo> - This field together with FileId specifies a chunk in the file

  public int SHA_256_SIZE = 256;
  public int MAX_CHUNK_SIZE = 999999;
  public int MAX_REP_DEG_SIZE = 9;
  public int MAX_BODY_SIZE = 64000;

  public String CRLF = Character.toString((char)13) + Character.toString((char)10);


  public String createHeader(String type, String version, int senderId, String fileId, int chunkNo, int repDeg){

    if (fileId.length() != this.SHA_256_SIZE){
      System.out.println("Hashed fileId length != 256!");
      return "";
    }

    if (!version.matches("(0-9)+(.)(0-9)+")){
      System.out.println("Wrong version!");
      return "";
    }

    if (chunkNo > this.MAX_CHUNK_SIZE){
      System.out.println("ChunkId larger than max allowed!");
      return "";
    }

    if (repDeg > this.MAX_CHUNK_SIZE){
      System.out.println("Replication Degree set to larger than max!");
      return "";
    }

    if (chunkNo == -1)
      return type + ' ' + version + ' ' + senderId + ' ' + fileId + ' ' + this.CRLF;

    if(repDeg != -1)
      return type + ' ' + version + ' ' + senderId + ' ' + fileId + ' ' + chunkNo + ' ' + repDeg + ' ' + this.CRLF;

    return type + ' ' + version + ' ' + senderId + ' ' + fileId + ' ' + chunkNo + ' ' + this.CRLF;

  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public String putchunkMsg(int senderId, String fileId, int chunkNo, int repDeg, String body){

    if (body.length() > this.MAX_BODY_SIZE){
      System.out.println("Error! Chunk bigger than " + this.MAX_CHUNK_SIZE + " bytes");
      return "";
    }

    String header = this.createHeader("PUTCHUNK", "1.0", senderId, fileId, chunkNo, repDeg);

    if (!header.equals("")){
      return header + this.CRLF + body;
    }

    return "";
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public String storedMsg(int senderId, String fileId, int chunkNo){

    String header = this.createHeader("STORED", "1.0", senderId, fileId, chunkNo, -1);

    if (!header.equals("")){
      return header + this.CRLF;
    }

    return "";
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public String getchunkMsg(int senderId, String fileId, int chunkNo){

    String header = this.createHeader("GETCHUNK", "1.0", senderId, fileId, chunkNo, -1);

    if (!header.equals("")){
      return header + this.CRLF;
    }

    return "";
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // CHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public String chunkMsg(int senderId, String fileId, int chunkNo, String body){

    if (body.length() > this.MAX_BODY_SIZE){
      System.out.println("Error! Chunk bigger than 64KByte");
      return "";
    }

    String header = this.createHeader("CHUNK", "1.0", senderId, fileId, chunkNo, -1);

    if (!header.equals("")){
      return header + this.CRLF + body;
    }

    return "";
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // DELETE <Version> <SenderId> <FileId> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public String deleteMsg(int senderId, String fileId){

    String header = this.createHeader("DELETE", "1.0", senderId, fileId, -1, -1);

    if (!header.equals("")){
      return header + this.CRLF;
    }

    return "";
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // REMOVED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public String removedMsg(int senderId, String fileId, int chunkNo){

    String header = this.createHeader("REMOVED", "1.0", senderId, fileId, chunkNo, -1);

    if (!header.equals("")){
      return header + this.CRLF;
    }

    return "";
  }


}
