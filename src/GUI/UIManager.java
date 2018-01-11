package GUI;

import albocoder.*;
import cointracker.CoinTracker;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public final class UIManager {
    private static SetupScreen ss;
    private static RuleUI rp;
    private static NotificationManager nm;
    
    public UIManager(String notification_location, String animation){
        startSingleProgramServer();
        rp = new RuleUI();
            Image image = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("icon.png"));
        rp.setIconImage(image);
        rp.setVisible(true);
        this.nm = new SingletonNotificationManager(notification_location,animation).nm;
        
    }
    public UIManager(){ 
        this(Notification.BOTTOM_RIGHT,Notification.ANIMATION_FADING);
    }
    
    /*
    public void terminate(int code){
        System.exit(code);
    }
    public void Notify(String type,String title,String msg,int duration){
        nm.notify(type, title, msg,duration);
    }
    public void Notify(String type,String title,String msg){
        nm.notify(type, title, msg);
    }
    */
    public void startSingleProgramServer(){
        try {
            ServerSocket srvs = new ServerSocket(54312);
            new Thread(new ServerSocketRunner(srvs)).start();
        } catch (IOException ex) {
            CoinTracker.notifyOnError("Port error", "Can't use port 514312! Is it in use?");
        }
    }
    
    public class ServerSocketRunner implements Runnable {

        ServerSocket ss;

        ServerSocketRunner(ServerSocket s) {
            super();
            ss = s;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket s = ss.accept();
                    try (PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {
                        out.println(CoinTracker.SINGLE_RUN_STRING);
                    }
                } catch (IOException ex) {}
            }
        }

    }
}
