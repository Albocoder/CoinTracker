package cointracker;

import Exceptions.MalformedRuleStringException;
import java.io.*;
import java.util.ArrayList;

public class RuleHandler {
    //global constants
    public static final String DEFAULT_FILE = "rulefile.txt";
    private static boolean STARTED = false;
    
    private static ArrayList<Rule> rulez = null;
    private String rulezfname = DEFAULT_FILE;
    
    public RuleHandler(String fname){
        if (!STARTED){
            restart(fname);
            STARTED = true;
        }
    }
    public RuleHandler(){this(DEFAULT_FILE);}
    
    private synchronized void writeRules() {
        if (rulez == null)
            return;
        try {
            try (PrintWriter p = new PrintWriter(new FileOutputStream(rulezfname))) {
                for (Rule r : rulez)
                    p.println(r);
                p.flush();
            }
        } catch (IOException ex) {} 
    }
    
    private synchronized void readRules() {
        if(rulez == null)
            rulez = new ArrayList<>();
        else
            rulez.clear();
        try (BufferedReader br = new BufferedReader (new FileReader(rulezfname))) {
            String line = "";
            while((line = br.readLine())!=null){
                try{
                    rulez.add(new Rule(line));
                }catch(MalformedRuleStringException e){}
            }
        } catch (IOException ex){}
    }
    public ArrayList<Rule> getRules(){return rulez;}
    
    public synchronized void editRule(Rule oldr,Rule newr){
        rulez.remove(oldr);
        rulez.add(newr);
    }
    public synchronized void deleteRule(Rule target){
        rulez.remove(target);
    }
    public synchronized void addRule(Rule newr){
        rulez.add(newr);
    }
    public synchronized void shutdown(){
        this.writeRules();
        rulez = null;
    }
    public final synchronized void restart(String fname){
        rulez = new ArrayList();
        rulezfname = fname;
        readRules();
    }
}