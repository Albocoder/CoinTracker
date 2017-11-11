package cointracker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class RuleHandler {
    //global constants
    public static final String DEFAULT_FILE = "rulefile.txt";
    
    private static ArrayList<Rule> rulez;
    private String rulezfname = DEFAULT_FILE;
    
    public RuleHandler(String fname){
        if (rulez == null){
            rulez = new ArrayList();
            rulezfname = fname;
            readRules();
        }
    }
    public RuleHandler(){this(DEFAULT_FILE);}
    
    private synchronized void writeRules() {
        try {
            try (PrintWriter p = new PrintWriter(new FileOutputStream(rulezfname))) {
                for (Rule r : rulez)
                    p.println(r);
                p.flush();
            }
        } catch (IOException ex) {} 
    }
    private synchronized void readRules() {
        try {
            try (ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream(rulezfname))) {
                rulez = (ArrayList<Rule>)input.readObject();
                input.close();
            }
        } catch (IOException | ClassNotFoundException ex) {rulez = new ArrayList();} 
    }
    public ArrayList<Rule> getRules(){return rulez;}
    
    // TODO: test logic for these
    public synchronized void editRule(Rule oldr,Rule newr){
        deleteRule(oldr);
        rulez.add(newr);
    }
    public synchronized void deleteRule(Rule target){
        rulez.remove(target);
    }
}