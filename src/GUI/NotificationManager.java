package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class NotificationManager {
    private final Vector<Notification> notificationList;
    private final MouseListener ml;
    private final String position;
    private final int XPAD,YPAD;
    
    public NotificationManager(){
        this("bottomright",10,10);
    }
    public NotificationManager(String p){
        this(p,10,10);
    }
    public NotificationManager(String p,int xpad,int ypad){
        ml = new MouseListener();
        XPAD = xpad;
        YPAD = ypad;
        notificationList = new Vector<Notification>();
        position = p;
    }
    
    public void notify(String type,String title, String msg,int duration){
        Notification n = new Notification(type,title,msg,position,XPAD,YPAD,notificationList.size()+1);
        n.addMouseListener(ml);
        if(duration != -1)
            new Thread(new AutoKiller(n,duration)).start();
        notificationList.add(n);
        n.showUp();
    }
    public void notify(String type,String title, String msg){
        notify(type,title,msg,10);
    }
    public void notifyNoAutokill(String type,String title, String msg){
        notify(type,title,msg,-1);
    }
    public void killNotification(Notification n){
        n.dismiss();
        if(notificationList == null)    
            return;
        int myplace = notificationList.indexOf(n);
        if(myplace < notificationList.size()-1){
            for(int i = myplace+1; i < notificationList.size(); i++){
                notificationList.elementAt(i).setOrder(i);
            }
        }
        notificationList.remove(n);
    }
    
    // private classes
    private class MouseListener extends MouseAdapter{
        @Override
        public void mouseReleased(MouseEvent e){
            Notification n = (Notification) e.getComponent();
            killNotification(n);
        }
    }
    private class AutoKiller implements Runnable {
        Notification n;
        int timer;
        public AutoKiller(Notification n,int t){
            this.n = n;
            timer = t;
        }    
        @Override
        public void run() {
            try {
                Thread.sleep(timer*1000);
            } catch (InterruptedException ex) {ex.printStackTrace();}
            killNotification(n);
        }
    }
}
