package cointracker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class Rule {
    private final String TYPE_OF_CHECK;        // increase, decrease, price
    private final String CMP_TYPE;             // <, >, <=, >=, =
    private final double THRESH_AMOUNT;
    private final String CURR;
    private final String COIN;
    private final String RTYPE;                // buy,sell,spot
    private final int REFRESH_INTERVAL;
    private final APIwrapper api;
    private final ScheduledExecutorService scheduler;
    
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
        scheduler = Executors.newScheduledThreadPool(1);
    }
    
    public void start(){
        scheduler.scheduleAtFixedRate(new PeriodicChecker(),REFRESH_INTERVAL,
                REFRESH_INTERVAL,TimeUnit.SECONDS);
    }
    
    private double getCurrentState(){
        JSONObject o = api.getRates();
        return o.getJSONObject("data").getDouble("amount");
    }
    
    private boolean checkRule(){
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
    
    private class PeriodicChecker implements Runnable{
        @Override
        public void run() {
            boolean check = checkRule();
            if (check){
                // notify 
            }
        }
    }
}
