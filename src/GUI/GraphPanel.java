package GUI;

import cointracker.HistoryMaintainer;
import java.awt.*;
import java.util.concurrent.*;
import javax.swing.JPanel;

public class GraphPanel extends JPanel{
    // constants
    private final int W=400,H=600,PREFW=600,PREFH=800;
    
    // properties
    private HistoryMaintainer HM;
    private ScheduledExecutorService scheduler;
    private int REFRESH_MINS;
    private int ETA;
    
    GraphPanel(){
            this("USD","BTC","day",60);
    }
    
    GraphPanel(String curr,String coin, String per, int refreshrate){
        HM = new HistoryMaintainer(curr,coin,per);
        REFRESH_MINS = refreshrate;
        ETA = REFRESH_MINS;
        
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new GraphPanel.PeriodicChecker(),0,
                REFRESH_MINS,TimeUnit.MINUTES);
        this.setSize(new Dimension(W,H));
        this.setPreferredSize(new Dimension(PREFW,PREFH));
    }
    
    private class PeriodicChecker implements Runnable{
        @Override
        public void run() {HM.update();}
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double heightscale = getHeight()-2/HM.getMax();
        double widthscale = getWidth()-2/HM.getHistory().size();
        
        g2.drawLine(1, getHeight() - 1, 1, 1);
        g2.drawLine(1, getHeight() - 1, 1, 1);
    }
}