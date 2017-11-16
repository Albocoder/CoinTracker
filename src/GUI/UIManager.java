package GUI;
import albocoder.*;

public class UIManager {
    private static SetupScreen ss;
    private static RuleUI rp;
    private static NotificationManager nm;
    
    public UIManager(String notification_location, String animation){
        rp = new RuleUI();
        rp.setVisible(true);
        //ss = new SetupScreen(this);
        this.nm = new SingletonNotificationManager(notification_location,animation).nm;
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
