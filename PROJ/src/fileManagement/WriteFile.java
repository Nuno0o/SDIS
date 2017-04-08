package fileManagement;


import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class WriteFile {

    public BufferedOutputStream bw;
    public FileOutputStream fw;

    public void storeChunk(FileChunk chunk,String name){
        // write file
        try{
            this.fw = new FileOutputStream(name);
            this.bw = new BufferedOutputStream(this.fw);
            this.bw.write(chunk.data);
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
