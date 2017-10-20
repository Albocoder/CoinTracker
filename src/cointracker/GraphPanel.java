package cointracker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

public class GraphPanel extends JPanel{
    private final HistoryMaintainer HM;
    private final ScheduledExecutorService scheduler;
    private final int REFRESH_MINS;
    
    GraphPanel(String c1,String c2, String p, int refreshrate){
        HM = new HistoryMaintainer(c1,c2,p);
        REFRESH_MINS = refreshrate;
        
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new GraphPanel.PeriodicChecker(),REFRESH_MINS,
                REFRESH_MINS,TimeUnit.MINUTES);
    }
    
    private class PeriodicChecker implements Runnable{
        @Override
        public void run() {HM.update();}
    }
}
