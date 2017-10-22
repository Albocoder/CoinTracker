package GUI;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;


public class Notification extends JPanel{
    private final int W = 150;
    private final int H = 60;
    
    private JFrame f;
    private JLabel text;
    private JLabel title;
    private String type;    //danger,warning,success
    private int order;
    
    public Notification(String ty,String ti,String msg){
        this(ty,ti,msg,1);
    }
    public Notification(String ty,String ti,String msg,int o) {
        super(true);
        makeFrame();
        type = ty;
        order = o;
        title = new JLabel(ti);
        text = new JLabel("<html><body><font size='20'>hello</font></body></html>");
    }
    
    public final void makeFrame(){
        f = new JFrame("TranslucentWindow");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
            setTranslucency( f );
        }
        f.setUndecorated( true );
        f.setBackground(new Color(0f, 0f, 0f, 1f / 3f));
        f.setSize(new Dimension(W,H));     // can be made screen dependant
    }
    
    private void reorder(){
        //calculation the position based on order
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle screen = gd.getDefaultConfiguration().getBounds();
        int x = (int)screen.getMaxX()-f.getWidth()+5;
        int y = (int)screen.getMaxY()-(f.getHeight()*order)+5;    //TODO: must have same height
        f.setLocation(x,y);
    }
    public void setOrder(int neworder){
        order = neworder;
        reorder();
    }
    
    public void showUp() {
        f.setResizable(false);
        try{
            fadeIn(f);
        } catch(InterruptedException e){f.setOpacity(0.8f);f.setVisible(true);}
    }
    
    public void fadeIn(JFrame f) throws InterruptedException{
        f.setOpacity(0.1f);
        f.setVisible(true);
        f.setOpacity(0.2f);
        Thread.sleep(100);
        f.setOpacity(0.3f);
        Thread.sleep(100);
        f.setOpacity(0.4f);
        Thread.sleep(100);
        f.setOpacity(0.5f);
        Thread.sleep(100);
        f.setOpacity(0.6f);
        Thread.sleep(100);
        f.setOpacity(0.7f);
        Thread.sleep(100);
        f.setOpacity(0.8f);
        f.revalidate();
    }
    public void fadeOut(JFrame f) throws InterruptedException{
        f.setOpacity(0.7f);
        Thread.sleep(100);
        f.setOpacity(0.6f);
        Thread.sleep(100);
        f.setOpacity(0.5f);
        Thread.sleep(100);
        f.setOpacity(0.4f);
        Thread.sleep(100);
        f.setOpacity(0.3f);
        Thread.sleep(100);
        f.setOpacity(0.2f);
        Thread.sleep(100);
        f.setOpacity(0.1f);
        Thread.sleep(100);
        f.setVisible(false);
    }
    public void dismiss(){
        try {
            this.fadeOut(f);
        } catch (InterruptedException ex) {f.setVisible(false);}
        f = null;
    }
    private static void setTranslucency( Window window){
        try {
               Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
               Method mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
               mSetWindowOpacity.invoke(null, window, Float.valueOf(0.85f));
            } catch (NoSuchMethodException | SecurityException |
                    ClassNotFoundException | IllegalAccessException |
                    IllegalArgumentException | InvocationTargetException ex) {
               ex.printStackTrace();
            }
    }
    private class ML extends MouseAdapter{
        @Override
        public void mouseReleased(MouseEvent e){
            //Point p = e.getPoint();
            //double x = p.getX();
            //double y = p.getY();
            dismiss();
        }
    }
}
