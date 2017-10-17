package cointracker;

import org.json.JSONObject;

public class HistoryMaintainer {
    private final String curr;
    private final String coin;
    private final String period;
    private final String rate_type;
    
    private APIwrapper w;
    private Object[][] time_amount_table;
            
    public HistoryMaintainer(String c1,String c2, String p, String rtype){
        curr = c1;
        coin = c2;
        period = p;
        rate_type = rtype;
        
        w = new APIwrapper("historic",coin,curr,p);
        time_amount_table = null;
    }
        
    public void update(){
        JSONObject jo = w.getRates();
        
    }
}
