package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class NotificationManager {
    //this will handle all the notifications, order etc...
    Vector<Notification> notificationList;
    ML l;
    
    public NotificationManager(){
        l = new ML();
        notificationList = new Vector<Notification>();
    }
    
    public void notify(String type,String title, String msg,int duration){
        Notification n = new Notification(notificationList,type,title,msg,notificationList.size()+1,duration);
        n.addMouseListener(l);
        notificationList.add(n);
        n.showUp(true);
    }
    public void notify(String type,String title, String msg){
        notify(type,title,msg,10);
    }
    // private classes
    private class ML extends MouseAdapter{
        @Override
        public void mouseReleased(MouseEvent e){
            Notification p = (Notification) e.getComponent();
            p.dismiss();
        }
    }
}
