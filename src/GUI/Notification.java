package GUI;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class Notification extends JPanel{
    // constrants
    private final int W = 250;
    private final int H = 60;
    private final int TEXT_MAX_CHAR = 33;
    private final int TITLE_MAX_CHAR = 15;
    // UI related
    private JFrame f;
    private JLabel text;
    private JLabel title;
    private JPanel textholder;
    //properties
    private String type;        //danger,warning,success
    private int order;
    private String position;
    private final int X_PADDING,Y_PADDING;
    private int xPos,yPos;
    
    public Notification(String ty,String ti,String msg){
        this(ty,ti,msg,"bottomright",10,10,1);
    }
    public Notification(String ty,String ti,String msg,String p){
        this(ty,ti,msg,p,10,10,1);
    }
    public Notification(String ty,String ti,String msg,String p,int xpad,int ypad){
        this(ty,ti,msg,p,xpad,ypad,1);
    }
    public Notification(String ty,String ti,String msg,String p,int xpad,int ypad,int o) {
        super(true);
        type = ty;
        order = o;
        position = p;
        X_PADDING = xpad;
        Y_PADDING = ypad;
        /*
        URL url = getClass().getResource("/foo/bar/sound.wav");
        AudioClip clip = Applet.newAudioClip(url);
        */
        if(ti.length()> TITLE_MAX_CHAR+3)
            ti = ti.substring(0,TITLE_MAX_CHAR-3)+"...";
        if(msg.length()> TEXT_MAX_CHAR+3)
            msg = msg.substring(0,TEXT_MAX_CHAR-3)+"...";
        
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
    }
    
    // taking care of the location in screen
    private void reorder(){
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle screen = gd.getDefaultConfiguration().getBounds();
        int xmax = (int)screen.getMaxX(),ymax = (int)screen.getMaxY();
        if (position.equalsIgnoreCase("bottomright"))
            reorderBottomRight(xmax,ymax);
        else if(position.equalsIgnoreCase("bottomleft"))
            reorderBottomLeft(xmax,ymax);
        else if(position.equalsIgnoreCase("topright"))
            reorderTopRight(xmax,ymax);
        else
            reorderTopLeft(xmax,ymax);
    }
    private void reorderBottomRight(int xmax, int ymax){
        xPos = xmax-f.getWidth()-X_PADDING;
        yPos = ymax-(f.getHeight()+5)*order-Y_PADDING;
        f.setLocation(xPos,yPos);
    }
    private void reorderTopRight(int xmax, int ymax){
        xPos = (f.getWidth()+5)*order+X_PADDING;
        yPos = (f.getHeight()+5)*order+Y_PADDING;
        f.setLocation(xPos,yPos);
    }
    private void reorderBottomLeft(int xmax, int ymax){
        xPos = X_PADDING;
        yPos = ymax-(f.getHeight()+5)*order-Y_PADDING;
        f.setLocation(xPos,yPos);
    }
    private void reorderTopLeft(int xmax, int ymax){
        System.out.println(order);
        xPos = X_PADDING;
        yPos = Y_PADDING + (f.getHeight()+5)*(order-1); //this is not OK
        f.setLocation(xPos,yPos);
    }
    public void setOrder(int neworder){
        if(neworder == 0)
            neworder = 1;
        order = neworder;
        reorder();
    }
    
    // showing and hiding notification
    public void showUp() {
        /*
        clip.play();
        */
        reorder();
        f.setResizable(false);
        try{
            fadeIn(f);
        } catch(InterruptedException e){f.setOpacity(0.8f);f.setVisible(true);}
    }
    public void dismiss(){
        try {
            if(f != null){
                this.fadeOut(f);
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
}
