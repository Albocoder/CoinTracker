package cointracker;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryMaintainer {
    public final String curr;
    public final String coin;
    public final String period;
    
    private final APIwrapper w;
    private ArrayList<HistorySampleContainer> historyArray;
            
    public HistoryMaintainer(String c1,String c2, String p){
        curr = c1;
        coin = c2;
        period = p;
        
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
    public double getMax(){
        Iterator i = historyArray.iterator();
        double max = 0.0;
        while(i.hasNext()){
            HistorySampleContainer c = (HistorySampleContainer)i.next();
            if (max < c.amount)
                max = c.amount;
        }
        return max;
    }
    
    public double getMin(){
        Iterator i = historyArray.iterator();
        double min = 0.0;
        while(i.hasNext()){
            HistorySampleContainer c = (HistorySampleContainer)i.next();
            if (min > c.amount)
                min = c.amount;
        }
        return min;
    }
    
    public ArrayList<HistorySampleContainer> getHistory(){return historyArray;}
}
