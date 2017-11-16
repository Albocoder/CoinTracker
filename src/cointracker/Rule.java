package cointracker;

import Exceptions.MalformedRuleStringException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import org.json.JSONObject;

public class Rule {
    // public globals
    public static ArrayList<String> CURRENCY_LIST; //make it all final uneditable
    public static final String[] CHECK_TYPES = {"increase","decrease","price"};
    public static final String[] COMPARISON_TYPES = {"<", ">", "<=", ">=", "="};
    
    // inner constants
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
        REFRESH_INTERVAL = (rint<0?-rint:rint);
        THRESH_AMOUNT = (a<0?-a:a);
        CMP_TYPE = ct;
        CURR = c1.toUpperCase();
        COIN = c2.toUpperCase();
        RTYPE = r.toLowerCase();
        testArgumentSanity();
        api = new APIwrapper(RTYPE,COIN,CURR);
        currState = getCurrentState();
        newState = currState;
    }
    
    public Rule(String toParse){
        if (toParse == null)
            throw new MalformedRuleStringException();
        String [] tokens = toParse.split(":");
        if (tokens.length != 7)
            throw new MalformedRuleStringException();
        try{
            TYPE_OF_CHECK = tokens[0];
            CMP_TYPE = tokens[1];
            double a = Double.parseDouble(tokens[2]);
            THRESH_AMOUNT = (a<0?-a:a);
            CURR = tokens[3].toUpperCase();
            COIN = tokens[4].toUpperCase();
            RTYPE = tokens[5].toLowerCase();
            int r = Integer.parseInt(tokens[6]);
            REFRESH_INTERVAL = (r<0?-r:r);
        }catch(Exception e){throw new MalformedRuleStringException();}
        testArgumentSanity();
        api = new APIwrapper(RTYPE,COIN,CURR);
        currState = getCurrentState();
        newState = currState;
    }
    // sanity checker
    private void testArgumentSanity(){
        getAvailableCurrencies();
        if (!TYPE_OF_CHECK.equalsIgnoreCase("increase") 
                && !TYPE_OF_CHECK.equalsIgnoreCase("decrease") 
                && !TYPE_OF_CHECK.equalsIgnoreCase("price"))
            throw new MalformedRuleStringException("checking only increase/decrease/price");
        
        if (!CMP_TYPE.equals("<") && !CMP_TYPE.equals("<=") 
                && !CMP_TYPE.equals(">") && !CMP_TYPE.equals(">=") && !CMP_TYPE.equals("="))
            throw new MalformedRuleStringException();
        
        if(!CURRENCY_LIST.contains(CURR))
            throw new MalformedRuleStringException("Currency type not supported");
        
        if(!COIN.equals("BTC") && !COIN.equals("ETH")&& !COIN.equals("LTC"))
            throw new MalformedRuleStringException("Cryptocurrency type not supported");
        
        if(!RTYPE.equals("buy") && !RTYPE.equals("sell")&& !RTYPE.equals("spot"))
            throw new MalformedRuleStringException("Rate type not supported");
    }
    
    private double getCurrentState(){
        JSONObject o = api.getRates();
        if (o == null)
            return currState;
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
    
    
    // generate all the currency codes
    private void getAvailableCurrencies() {
        if(CURRENCY_LIST != null)
            return;
        CURRENCY_LIST = new ArrayList<>();
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            try {
                CURRENCY_LIST.add(Currency.getInstance(locale).getCurrencyCode().toUpperCase());
            } catch (Exception e) {}
        }
    }
    
    // GETTERS
    public int getRefreshInterval(){return REFRESH_INTERVAL;}
    public String getCurr(){return CURR;}
    public String getRtype(){return RTYPE;}
    public String getCoin(){return COIN;}
    public double getAmount(){return THRESH_AMOUNT;}
    public String getCmpType(){return CMP_TYPE;}
    
    // TOSTRING OVERRIDE 
    @Override
    public String toString(){
        return TYPE_OF_CHECK+":"+CMP_TYPE+":"+THRESH_AMOUNT+":"+CURR+":"+COIN
                +":"+RTYPE+":"+REFRESH_INTERVAL;
    }
}
