package cointracker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.*;


public class APIwrapper {
    // parameters
    public String rtype;
    public String coin;
    public String curr;
    public String histfreq;
    
    // constructors
    public APIwrapper(){this("spot","BTC","USD","day");}        // DEFAULT SETUP
    public APIwrapper(String r){this(r,"BTC","USD","day");}
    public APIwrapper(String r,String c1){this(r,c1,"USD","day");}
    public APIwrapper(String r, String c1, String c2){this(r,c1,c2,"day");}
    public APIwrapper(String r,String c1,String c2, String h){
        rtype = r;      // possibilities: buy, sell, spot, historic
        coin = c1;      // possibilities: BTC, ETH, LTC
        curr = c2;      // possibilities: USD, EUR, ALL, TRY ...
        histfreq = h;   // possibilities: hour,day,week,month,year,all
    }
    
    public JSONObject getRates() {
        String reqstring = null;
        if (rtype.equalsIgnoreCase("historic"))
            reqstring = "https://www.coinbase.com/api/v2/prices/"+coin+"-"+curr+
                    "/historic?period="+histfreq;
        else
            reqstring = "https://www.coinbase.com/api/v2/prices/"+coin+"-"+curr+
                    "/"+rtype;
        URL url;
        try {
            url = new URL(reqstring);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("CB-VERSION","2015-04-08");
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            String body = IOUtils.toString(in, encoding);
            return new JSONObject(body);
        } catch (IOException | JSONException e) {return null;}
    }
}
