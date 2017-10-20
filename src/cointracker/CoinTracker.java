package cointracker;

import GUI.UIManager;

public class CoinTracker {
    private static APIwrapper w;
    public static void print(Object o){System.out.println(o);}
    
    public static void main(String[] args) {
        new UIManager();
    }
}
