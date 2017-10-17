package cointracker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Notifier {
    private Rule[] rulez;
    private final String rulezfname = "rulefile.bin";
    
    public Notifier(){
        readRules();
        
    }
    
    
    private void writeRules() {
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream(rulezfname, true))) {
                output.writeObject(rulez);
                output.flush();
            }
        } catch (IOException ex) {} 
    }
    private void readRules() {
        try {
            try (ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream(rulezfname))) {
                rulez = (Rule[])input.readObject();
                input.close();
            }
        } catch (IOException | ClassNotFoundException ex) {rulez = null;} 
    }
}
