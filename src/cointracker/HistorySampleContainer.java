package cointracker;

public class HistorySampleContainer {
    // time values broken down
    public int y;
    public int mo;
    public int d;
    public int h;
    public int mi;
    public int s;
    // exchange rate
    public double amount;
    
    HistorySampleContainer(double a, String rawtime){
        parseRawTime(rawtime);
        amount = a;
    }
    private void parseRawTime(String r){
        int index = r.indexOf('-');
        y = Integer.parseInt(r.substring(0,index));
        int index2 = r.indexOf('-',index+1);
        mo = Integer.parseInt(r.substring(index+1,index2));
        index = index2;
        index2 = r.indexOf('T',index);
        d = Integer.parseInt(r.substring(index+1,index2));
        index = index2;
        index2 = r.indexOf(':',index);
        h = Integer.parseInt(r.substring(index+1,index2));
        index = index2;
        index2 = r.indexOf(':',index);
        mi = Integer.parseInt(r.substring(index+1,index2));
        s = Integer.parseInt(r.substring(index2+1,index2+3));
    }
}
