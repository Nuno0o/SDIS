package fileManagement;

import utilities.Constants;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class WriteFile {


    public void storeChunk(FileChunk chunk,String name){
        // write file
    	String data = new String(chunk.data);
    	FileWriter fw = null;
    	BufferedWriter bw = null;
        try{
            fw = new FileWriter(name);
            bw = new BufferedWriter(fw);
            bw.write(data);
        }  catch(IOException e) {
            System.err.println("Error storing chunk: " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException e){
                System.err.println("Error closing BW FW: " + e.toString());
                e.printStackTrace();
            }
        }
    }

}
