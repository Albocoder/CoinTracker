package GUI;
import albocoder.*;

public class UIManager {
    SetupScreen ss;
    NotificationManager nm;
    public UIManager(String notification_location, String animation){
        ss = new SetupScreen(this);
        nm = new NotificationManager(notification_location,animation);
    }
    public UIManager(){
        this(Notification.BOTTOM_RIGHT,Notification.ANIMATION_FADING);
    }
    
    public void terminate(int code){
        System.exit(code);
    }
    public void Notify(String type,String title,String msg,int duration){
        nm.notify(type, title, msg,duration);
    }
    public void Notify(String type,String title,String msg){
        nm.notify(type, title, msg);
    }
}
