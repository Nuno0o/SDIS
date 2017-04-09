package fileManagement;


import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class WriteFile extends Thread {

    public BufferedOutputStream bw;
    FileChunk chunk;
    String name;

    public WriteFile(FileChunk chunk,String name){
    	this.chunk = chunk;
    	this.name = name;
    }

    public void run(){
        // write file
        try{
            this.bw = new BufferedOutputStream(new FileOutputStream(name));
            this.bw.write(chunk.data);
            this.bw.flush();

        }  catch(IOException e) {
            System.err.println("Error storing chunk: " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (this.bw != null)
                    this.bw.close();
            } catch (IOException e){
                System.err.println("Error closing BW FW: " + e.toString());
                e.printStackTrace();
            }
        }
    }

}
