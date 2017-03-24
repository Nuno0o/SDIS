package fileManagement;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class WriteFile {

    public BufferedWriter bw;
    public FileWriter fw;

    public void storeChunk(String[] splitStr){

        // TODO: get body

        // write file

        try{
            this.fw = new FileWriter(splitStr[3] + "::" + splitStr[4]);
            this.bw = new BufferedWriter(fw);
            this.bw.write(/*data*/);
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
