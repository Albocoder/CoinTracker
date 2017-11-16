package GUI;

import albocoder.NotificationManager;

public class SingletonNotificationManager {
    public static NotificationManager nm = null;
    public SingletonNotificationManager(String pos){
        if (nm == null)
            nm = new NotificationManager(pos);
    }
    public SingletonNotificationManager(){
        if (nm == null)
            nm = new NotificationManager();
    }

    SingletonNotificationManager(String notification_location, String animation) {
        if (nm == null)
            nm = new NotificationManager(notification_location,animation);
    }
}
