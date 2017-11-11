package cointracker;

import org.json.JSONObject;
import Exceptions.MalformedRuleStringException;

public class Rule {
    private final String TYPE_OF_CHECK;        // increase, decrease, price
    private final String CMP_TYPE;             // <, >, <=, >=, =
    private final double THRESH_AMOUNT;
    private final String CURR;
    private final String COIN;
    private final String RTYPE;                // buy,sell,spot
    private final int REFRESH_INTERVAL;
    private final APIwrapper api;
    
    // keeping up with the rates
    private double currState;
    private double newState;
    
    public Rule(String tc,int rint,double a,String ct,String c1, String c2, String r){
        TYPE_OF_CHECK = tc;
        REFRESH_INTERVAL = rint;
        if (a < 0)
            a = -a;
        THRESH_AMOUNT = a;
        CMP_TYPE = ct;
        CURR = c1;
        COIN = c2;
        RTYPE = r;
        api = new APIwrapper(RTYPE,COIN,CURR);
        currState = getCurrentState();
        newState = currState;
    }
    
    public Rule(String toParse){
        //TYPE_OF_CHECK = tc;
        //REFRESH_INTERVAL = rint;
        //if (a < 0)
        //    a = -a;
        //THRESH_AMOUNT = a;
        //CMP_TYPE = ct;
        //CURR = c1;
        //COIN = c2;
        //RTYPE = r;
        try{
            //parse and assign
            //if(-1){
            //    throw new MalformedRuleStringException();
            //}
        }
        catch(Exception e){
            throw new MalformedRuleStringException();
        }
    }
    
    private double getCurrentState(){
        JSONObject o = api.getRates();
        return o.getJSONObject("data").getDouble("amount");
    }
    
    public boolean checkRule(){
        newState = getCurrentState();
        double val;
        
        if (TYPE_OF_CHECK.equalsIgnoreCase("increase"))
            val = newState-currState;
        else if (TYPE_OF_CHECK.equalsIgnoreCase("decrease"))
            val = currState-newState;
        else
            val = newState;
        
        currState = newState;
        
        if(CMP_TYPE.equalsIgnoreCase("<"))
            return val < THRESH_AMOUNT;
        else if(CMP_TYPE.equalsIgnoreCase(">"))
            return val > THRESH_AMOUNT;
        else if(CMP_TYPE.equalsIgnoreCase(">="))
            return val >= THRESH_AMOUNT;
        else if(CMP_TYPE.equalsIgnoreCase("<="))
            return val <= THRESH_AMOUNT;
        else
            return val == THRESH_AMOUNT;
    }
    
    // GETTERS
    public int getRefreshInterval(){return REFRESH_INTERVAL;}
    
    // TOSTRING OVERRIDE 
    @Override
    public String toString(){
        return TYPE_OF_CHECK+":"+CMP_TYPE+":"+THRESH_AMOUNT+":"+CURR+":"+COIN
                +":"+RTYPE+":"+REFRESH_INTERVAL;
    }
}
