package cointracker;

import Exceptions.MalformedRuleStringException;
import java.io.*;
import java.util.ArrayList;

public class RuleHandler {
    //global constants
    public static final String DEFAULT_FILE = "rulefile.txt";
    
    private static ArrayList<Rule> rulez = null;
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
        deleteRule(oldr);
        rulez.add(newr);
    }
    public synchronized void deleteRule(Rule target){
        rulez.remove(target);
    }
    public synchronized void addRule(Rule newr){
        rulez.add(newr);
    }
}