package fileManagement;


import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class WriteFile {

    public BufferedWriter bw;
    public FileWriter fw;

    public void storeChunk(FileChunk chunk,String name){
        // write file
    	String data = new String(chunk.data);
        try{
            this.fw = new FileWriter(name);
            this.bw = new BufferedWriter(this.fw);
            this.bw.write(data);
        }  catch(IOException e) {
            System.err.println("Error storing chunk: " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (this.bw != null)
                    this.bw.close();
                if (this.fw != null)
                    this.fw.close();
            } catch (IOException e){
                System.err.println("Error closing BW FW: " + e.toString());
                e.printStackTrace();
            }
        }
    }

}
