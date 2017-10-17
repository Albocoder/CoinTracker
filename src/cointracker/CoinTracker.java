package cointracker;

public class CoinTracker {
    private static APIwrapper w;
    public static void print(Object o){System.out.println(o);}
    
    public static void main(String[] args) {
        w = new APIwrapper("historic"); //wrapper here will be used for display
        print(w.getRates());
    }
    
}
