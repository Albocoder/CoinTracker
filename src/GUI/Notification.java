package GUI;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

public class Notification extends JPanel{
    // constrants
    private final int W = 250;
    private final int H = 60;
    private final int TEXT_MAX_CHAR = 20;
    private final int TITLE_MAX_CHAR = 10;
    // UI related
    private JFrame f;
    private JLabel text;
    private JLabel title;
    private JPanel textholder;
    //properties
    private String type;    //danger,warning,success
    private int order;
    private int xPos,yPos;
    private int TTL;
    private Vector<Notification> allNotification;
    
    public Notification(String ty,String ti,String msg){
        this(null,ty,ti,msg,1,10);
    }
    public Notification(String ty,String ti,String msg,int o,int life){
        this(null,ty,ti,msg,o,life);
    }
    public Notification(Vector<Notification> n,String ty,String ti,String msg){
        this(n,ty,ti,msg,1,10);
    }
    public Notification(Vector<Notification> n,String ty,String ti,String msg,int o,int life) {
        super(true);
        type = ty;
        TTL = life;
        order = o;
        allNotification = n;
        /*
        URL url = getClass().getResource("/foo/bar/sound.wav");
        AudioClip clip = Applet.newAudioClip(url);
        */
        title = new JLabel("<html><body><font size='5'>"+ti+"</font></body></html>");
        text = new JLabel("<html><body><font size='2'>"+msg+"</font></body></html>");
        
        this.setLayout(new BorderLayout());
        add(new JLabel(" "),BorderLayout.WEST);
        add(new JLabel(" "),BorderLayout.EAST);
        textholder = new JPanel();
        textholder.setLayout(new BorderLayout());
        textholder.add(title,BorderLayout.NORTH);
        textholder.add(text,BorderLayout.CENTER);
        add(textholder);
        makeFrame();
        colorize();
    }
   
    // colorize according to the notification type
    private void colorize(){
        Color panelBackColor;
        Color titleColor;
        Color textColor;
        if(type.equalsIgnoreCase("danger")){
            panelBackColor = Color.RED;
            titleColor = Color.YELLOW;
            textColor = Color.YELLOW;
        }
        else if(type.equalsIgnoreCase("warning")){
            panelBackColor = Color.ORANGE;
            titleColor = Color.BLACK;
            textColor = Color.BLACK;
        }
        else{
            panelBackColor = Color.GREEN;
            titleColor = Color.BLACK;
            textColor = Color.BLACK;
        }
        this.setBackground(panelBackColor);
        textholder.setBackground(panelBackColor);
        text.setForeground(textColor);
        title.setForeground(titleColor);
    }
            
            
    // constructor of the frame
    private void makeFrame(){
        f = new JFrame("TranslucentWindow");
        f.setUndecorated( true );
        f.setBackground(new Color(0f, 0f, 0f, 1f / 3f));
        f.setSize(new Dimension(W,H));     // can be made screen dependant
        f.setAlwaysOnTop(true);
        f.setShape(new RoundRectangle2D.Double(0, 0, W, H, 20, 20));
        f.add(this);
        f.setAutoRequestFocus(false);
        reorder();
    }
    
    // taking care of the location in screen
    private void reorder(){
        //calculation the position based on order
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle screen = gd.getDefaultConfiguration().getBounds();
        xPos = (int)screen.getMaxX()-f.getWidth()-5;
        yPos = (int)screen.getMaxY()-(f.getHeight()+5)*order-5;
        f.setLocation(xPos,yPos);
    }
    public void setOrder(int neworder){
        order = neworder;
        reorder();
    }
    
    // showing and hiding notification
    public void showUp(boolean autokill) {
        /*
        clip.play();
        */
        reorder();
        if(autokill){
            Thread t = new Thread(new AutoKiller());
            t.start();
        }
        f.setResizable(false);
        try{
            fadeIn(f);
        } catch(InterruptedException e){f.setOpacity(0.8f);f.setVisible(true);}
    }
    public void dismiss(){
        try {
            if(f != null){
                this.fadeOut(f);
                if(allNotification == null)
                    return;
                int myplace = allNotification.indexOf(this);
                if(myplace < allNotification.size()-1){
                    for(int i = myplace+1; i < allNotification.size(); i++){
                        allNotification.elementAt(i).setOrder(i);
                    }
                }
                allNotification.remove(this);
            }else
                return;
        } catch (InterruptedException ex) {f.setVisible(false);}
        f = null;
    }
    
    // supported animations
    @SuppressWarnings("SleepWhileInLoop")
    public void fadeIn(JFrame f) throws InterruptedException{
        f.setOpacity(0.1f);
        f.setVisible(true);
        for (float o = 0.2f;o <= 0.8;o+=0.1f){
            f.setOpacity(o);
            Thread.sleep(100);
        }
        f.revalidate();
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public void fadeOut(JFrame f) throws InterruptedException{
        for (float o = 0.7f;o >= 0.1;o-=0.1f){
            f.setOpacity(o);
            Thread.sleep(100);
        }
        f.setVisible(false);
    }
    
    // getters
    public int getxPos(){return xPos;}
    public int getyPos(){return yPos;}
    
    // inner classes
    private class AutoKiller implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(TTL*1000);
            } catch (InterruptedException ex) {ex.printStackTrace();}
            dismiss();
        }
    }
}
