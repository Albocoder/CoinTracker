package cointracker;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryMaintainer {
    private final String curr;
    private final String coin;
    private final String period;
    private final String rate_type;
    
    private final APIwrapper w;
    private ArrayList<HistorySampleContainer> historyArray;
            
    public HistoryMaintainer(String c1,String c2, String p, String rtype){
        curr = c1;
        coin = c2;
        period = p;
        rate_type = rtype;
        
        w = new APIwrapper("historic",coin,curr,p);
        historyArray = null;
    }
        
    public void update(){
        JSONObject jo = w.getRates();
        JSONArray a = jo.getJSONObject("data").getJSONArray("prices");
        historyArray = new ArrayList<HistorySampleContainer>();
        Iterator i = a.iterator();
        while(i.hasNext()){
            JSONObject tmpo = (JSONObject) i.next();
            HistorySampleContainer tmpHSC = new HistorySampleContainer(
                    tmpo.getDouble("price") , tmpo.getString("time") );
        }
    }
}
