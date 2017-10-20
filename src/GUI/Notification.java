package GUI;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;


public class Notification extends JPanel implements ActionListener{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private final Date now = new Date();
    private final Timer timer = new Timer(1000, this);
    private final JLabel text = new JLabel();

    public Notification() {
        super(true);
        add(new JButton("my button"));
        timer.start();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        now.setTime(System.currentTimeMillis());
        text.setText(String.format("<html><body><font size='50'>%s</font></body></html>",sdf.format(now)));
    }

    public void run() {
        JFrame f = new JFrame("TranslucentWindow");
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
            setTranslucency( f );
        }
        Rectangle rect = gd.getDefaultConfiguration().getBounds();
        
        f.setUndecorated( true );
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBackground(new Color(0f, 0f, 0f, 1f / 3f));
        f.add(new Notification());
        f.pack();
        int x = (int)rect.getMaxX()-f.getWidth()+5;
        int y = (int)rect.getMaxY()-f.getHeight()+5;
        f.setLocation(x,y);
        try{
            fadeIn(f);
        } catch(InterruptedException e){
            f.setOpacity(1);
            f.setVisible(true);
        }
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

}
