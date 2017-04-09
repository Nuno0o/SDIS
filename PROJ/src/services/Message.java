package services;
import utilities.Constants;
import java.io.ByteArrayOutputStream;
public class Message {

  // <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>
  // <MessageType> - The subprotocol
  // <Version> - Unenhanced: 1.0
  // <SenderId> - The id of the server that has sent the message. variable length
  // <FileId> - SHA-256 of the file ID
  // <ChunkNo> - This field together with FileId specifies a chunk in the file


  public byte[] createHeader(String type, String version, int senderId, String fileId, int chunkNo, int repDeg){

    if (chunkNo > Constants.MAX_CHUNK_SIZE){
      System.out.println("ChunkId larger than max allowed!");
      return null;
    }

    if (repDeg > Constants.MAX_CHUNK_SIZE){
      System.out.println("Replication Degree set to larger than max!");
      return null;
    }

    if (chunkNo == -1){
      return (type + ' ' + version + ' ' + senderId + ' ' + fileId + ' ' + Constants.CRLF).getBytes();
    }

    if(repDeg != -1)
      return (type + ' ' + version + ' ' + senderId + ' ' + fileId + ' ' + chunkNo + ' ' + repDeg + ' ' + Constants.CRLF).getBytes();

    return (type + ' ' + version + ' ' + senderId + ' ' + fileId + ' ' + chunkNo + ' ' + Constants.CRLF).getBytes();

  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public byte[] putchunkMsg(int senderId, String fileId, int chunkNo, int repDeg, byte[] body){

    if (body.length > Constants.MAX_BODY_SIZE){
      System.out.println("Error! Chunk bigger than " + Constants.MAX_CHUNK_SIZE + " bytes");
      return null;
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

    byte[] header = this.createHeader("PUTCHUNK", "1.0", senderId, fileId, chunkNo, repDeg);

    if (!(header == null) ){
        try {
        outputStream.write(header);
        outputStream.write(Constants.CRLF.getBytes());
        outputStream.write(body);
        return outputStream.toByteArray();
        } catch (Exception e) {}
    }

    return null;
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public byte[] storedMsg(int senderId, String fileId, int chunkNo){

    byte[] header = this.createHeader("STORED", "1.0", senderId, fileId, chunkNo, -1);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

    if (!(header == null)){
        try {
            outputStream.write(header);
            outputStream.write(Constants.CRLF.getBytes());
            return outputStream.toByteArray();
        } catch(Exception e) {}
    }

    return null;
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public byte[] getchunkMsg(int senderId, String fileId, int chunkNo){

    byte[] header = this.createHeader("GETCHUNK", "1.0", senderId, fileId, chunkNo, -1);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

    if (!(header == null)){
        try {
            outputStream.write(header);
            outputStream.write(Constants.CRLF.getBytes());
            return outputStream.toByteArray();
        } catch (Exception e) {}
    }

    return null;
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // CHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public byte[] chunkMsg(int senderId, String fileId, int chunkNo, byte[] body){

    if (body.length > Constants.MAX_BODY_SIZE){
      System.out.println("Error! Chunk bigger than 64KByte: " + body.length);
      return null;
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

    byte[] header = this.createHeader("CHUNK", "1.0", senderId, fileId, chunkNo, -1);

    if (!(header == null)){
        try {
            outputStream.write(header);
            outputStream.write(Constants.CRLF.getBytes());
            outputStream.write(body);
            return outputStream.toByteArray();
        } catch (Exception e) {}
    }

    return null;
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // DELETE <Version> <SenderId> <FileId> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public byte[] deleteMsg(int senderId, String fileId){

    byte[] header = this.createHeader("DELETE", "1.0", senderId, fileId, -1, -1);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

    if (!(header == null)){
        try {
            outputStream.write(header);
            outputStream.write(Constants.CRLF.getBytes());
            return outputStream.toByteArray();
        } catch (Exception e) {}
    }

    return null;
  }

  // -------------------------------------------------------------
  // -------------------------------------------------------------
  // REMOVED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
  // -------------------------------------------------------------
  // -------------------------------------------------------------

  public byte[] removedMsg(int senderId, String fileId, int chunkNo){

    byte[] header = this.createHeader("REMOVED", "1.0", senderId, fileId, chunkNo, -1);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

    if (!(header == null)){
        try {
            outputStream.write(header);
            outputStream.write(Constants.CRLF.getBytes());
            return outputStream.toByteArray();
        } catch (Exception e) {}
    }

    return null;
  }

}
