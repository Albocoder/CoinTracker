package GUI;

import Exceptions.MalformedRuleStringException;
import GUI.RuleUI.DeleteHandler;
import albocoder.Notification;
import cointracker.Rule;
import cointracker.RuleHandler;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.*;

class RulePanel extends JPanel{
    public static final int COOLDOWN_EMPTY_RUNS = 5;
    public static final String CURR_DIR = System.getProperty("user.dir");
    public static final ImageIcon 
    REFRESH = new ImageIcon(ClassLoader.getSystemResource("refresh.png")),
    REFRESH_PRESSED = new ImageIcon(ClassLoader.getSystemResource("refresh_pressed.png")),
    PLAYIMG = new ImageIcon(ClassLoader.getSystemResource("play.png")),
    PLAYIMG_GRAY = new ImageIcon(ClassLoader.getSystemResource("play_grayscale.png")),
    PAUSEIMG = new ImageIcon(ClassLoader.getSystemResource("pause.png")),
    PAUSEIMG_GRAY = new ImageIcon(ClassLoader.getSystemResource("pause_grayscale.png")),
    CLOSEIMG = new ImageIcon(ClassLoader.getSystemResource("delete.png")),
    CLOSEIMG_PRESSED = new ImageIcon(ClassLoader.getSystemResource("delete_pressed.png"));

    private JCheckBox ruleCheckox;
    private JButton deleteBtn,refrBtn;
    private JTextField ruleDescription;
    private JToggleButton ruleToggleActivation;
    private JPanel wrapper;
    private Rule r;
    private ScheduledExecutorService scheduler;
    
    //about notifications 
    private boolean notifyEnabled;
    private int cooldownRuns;

    private RulePanel() {
        notifyEnabled = true;
        wrapper = new JPanel();
        ruleCheckox = new JCheckBox();
        ruleDescription = new JTextField();
        refrBtn = new JButton();
        deleteBtn = new JButton();
        ruleToggleActivation = new JToggleButton();
        refrBtn.setBorder(null);
        deleteBtn.setBorder(null);
        refrBtn.setBackground(Color.WHITE);
        deleteBtn.setBackground(Color.WHITE);
        refrBtn.setIcon(REFRESH);
        deleteBtn.setIcon(CLOSEIMG);
        refrBtn.addMouseListener(new RefreshHandler());
        

        ruleToggleActivation.setBorder(null);
        ruleToggleActivation.setIcon(PAUSEIMG);
        ruleToggleActivation.setToolTipText("Pause the rule");
        ruleDescription.addActionListener(this::ruleDescriptionActionPerformed);
        ruleToggleActivation.addActionListener(this::ruleToggleActionPerformed);

        GroupLayout rulePanelLayout = new GroupLayout(wrapper);
        wrapper.setLayout(rulePanelLayout);
        rulePanelLayout.setHorizontalGroup(
            rulePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(rulePanelLayout.createSequentialGroup()
                .addComponent(ruleCheckox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ruleDescription, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ruleToggleActivation, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteBtn, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refrBtn, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        rulePanelLayout.setVerticalGroup(
            rulePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(rulePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rulePanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(ruleCheckox)
                    .addGroup(rulePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ruleDescription, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                        .addComponent(ruleToggleActivation, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteBtn, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                        .addComponent(refrBtn, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        add(wrapper);
    }
    public RulePanel(DeleteHandler h,Rule r) {
        this();
        deleteBtn.addMouseListener(h);
        this.r = r;
        cooldownRuns = 0;
        scheduler = Executors.newScheduledThreadPool(1);
        populateComponents();
        this.start();
    }
    private void populateComponents(){
        ruleDescription.setEditable(false);
        if (r == null)
            ruleDescription.setText("<check_type>:<cmp_type>:<amount>:<COIN>:<CURR>:<rate_type>:<refr_interval>");
        else
            ruleDescription.setText(r.toString());
    }
    // Toggle button enable/disable
    private void disableToggleButton(){
        if (ruleToggleActivation.isSelected())
            ruleToggleActivation.setIcon(PLAYIMG_GRAY);
        else
            ruleToggleActivation.setIcon(PAUSEIMG_GRAY);
        ruleToggleActivation.setEnabled(false);
    }
    private void enableToggleButton(){
        if (ruleToggleActivation.isSelected())
            ruleToggleActivation.setIcon(PLAYIMG);
        else
            ruleToggleActivation.setIcon(PAUSEIMG);
        ruleToggleActivation.setEnabled(true);
    }
    // RULE Routines
    private void start(){
        if(scheduler != null && !scheduler.isShutdown())
            scheduler.shutdown();
        scheduler = Executors.newScheduledThreadPool(1);
        if (r != null)
            scheduler.scheduleAtFixedRate(new PeriodicChecker(),r.getRefreshInterval(),
                r.getRefreshInterval(),TimeUnit.SECONDS);
    }
    private void pause(){
        if(scheduler == null)
            return;
        scheduler.shutdown();
    }
    private void stop(){
        pause();
    }
    private void switchRule() throws MalformedRuleStringException{
        this.stop();
        Rule newr = new Rule(ruleDescription.getText());
        new RuleHandler().editRule(r, newr);
        r = newr;
    }
    
    // Notification Mesage composer(s)
    private String composeRuleCheckMessage(){
        return r.getCoin()+" "+r.getRtype()+" price is "+r.getCmpType()+r.getAmount()+" "+r.getCurr();
    }
    
    // Runs all the time 
    private void checkRule(){
        boolean check = r.checkRule();
        if (check && notifyEnabled){
            if(cooldownRuns > 0){
                cooldownRuns--;
                return;
            }
            SingletonNotificationManager.nm.
                    notify(Notification.TYPE_SUCCESS,"Rule match!",
                            composeRuleCheckMessage());
            cooldownRuns = COOLDOWN_EMPTY_RUNS;
        }
    }
    
    // PUBLIC METHODS
    //disabler
    public void disableRule(){
        if (!ruleToggleActivation.isSelected()){
            this.stop();
            ruleToggleActivation.setIcon(PLAYIMG);
            ruleToggleActivation.setToolTipText("Run the rule");
        }
    }
    public void enableRule(){
        if (ruleToggleActivation.isSelected()){
            this.start();
            ruleToggleActivation.setIcon(PAUSEIMG);
            ruleToggleActivation.setToolTipText("Pause the rule");
        }
    }
    public void disableRuleIfSelected(){
        if (this.isChecked())
            this.disableRule();
    }
    
    // accessors and modifiers
    public Rule getRule(){return r;}
    public boolean getNotification(){return notifyEnabled;}
    public boolean isChecked(){return ruleCheckox.isSelected();}
    public void setChecked(boolean c){ruleCheckox.setSelected(c);}
    public void setNotification(boolean toSet){notifyEnabled = toSet;}
    
    ///////////////////////// ACTION EVENT FUNCTIONS ///////////////////////
    private void ruleToggleActionPerformed(ActionEvent evt) {                                            
        if (ruleToggleActivation.isSelected()){
            this.pause();
            ruleToggleActivation.setIcon(PLAYIMG);
            ruleToggleActivation.setToolTipText("Run the rule");
        }
        else{
            this.start();
            ruleToggleActivation.setIcon(PAUSEIMG);
            ruleToggleActivation.setToolTipText("Pause the rule");
        }
    }
    private void ruleDescriptionActionPerformed(ActionEvent e) {
        if (ruleDescription.isEditable()){
            try{
                if (r == null)
                    switchRule();
                else if(!r.toString().equals(ruleDescription.getText()))
                    switchRule();
            } catch (MalformedRuleStringException ex) { // ERROR saving null with no change
                SingletonNotificationManager.nm.
                    notify(Notification.TYPE_DANGER,"Error!",ex.getMessage());
                return;
            }
            if(!ruleToggleActivation.isSelected())
                this.start();
            enableToggleButton();
            ruleDescription.setEditable(false);
        }
        else{
            ruleDescription.setEditable(true);
            disableToggleButton();
            this.stop();
        }
    }
    private class RefreshHandler extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            refrBtn.setIcon(REFRESH_PRESSED);
        }
        @Override
        public void mouseReleased(MouseEvent e){
            refrBtn.setIcon(REFRESH);
            checkRule();
        }
    }
    private class PeriodicChecker implements Runnable{
        @Override
        public void run() {
            checkRule();
        }
    }
}
/* TODO
Exception in thread "AWT-EventQueue-1" java.lang.NullPointerException
	at albocoder.Notification.<init>(Notification.java:55)
	at albocoder.NotificationManager.notify(NotificationManager.java:38)
	at albocoder.NotificationManager.notify(NotificationManager.java:46)
	at albocoder.NotificationManager.notify(NotificationManager.java:49)
	at GUI.RulePanel.ruleDescriptionActionPerformed(RulePanel.java:217)
	at javax.swing.JTextField.fireActionPerformed(JTextField.java:508)
	at javax.swing.JTextField.postActionEvent(JTextField.java:721)
	at javax.swing.JTextField$NotifyAction.actionPerformed(JTextField.java:836)
	at javax.swing.SwingUtilities.notifyAction(SwingUtilities.java:1663)
	at javax.swing.JComponent.processKeyBinding(JComponent.java:2882)
	at javax.swing.JComponent.processKeyBindings(JComponent.java:2929)
	at javax.swing.JComponent.processKeyEvent(JComponent.java:2845)
	at java.awt.Component.processEvent(Component.java:6310)
	at java.awt.Container.processEvent(Container.java:2236)
	at java.awt.Component.dispatchEventImpl(Component.java:4889)
	at java.awt.Container.dispatchEventImpl(Container.java:2294)
	at java.awt.Component.dispatchEvent(Component.java:4711)
	at java.awt.KeyboardFocusManager.redispatchEvent(KeyboardFocusManager.java:1954)
	at java.awt.DefaultKeyboardFocusManager.dispatchKeyEvent(DefaultKeyboardFocusManager.java:806)
	at java.awt.DefaultKeyboardFocusManager.preDispatchKeyEvent(DefaultKeyboardFocusManager.java:1074)
	at java.awt.DefaultKeyboardFocusManager.typeAheadAssertions(DefaultKeyboardFocusManager.java:945)
	at java.awt.DefaultKeyboardFocusManager.dispatchEvent(DefaultKeyboardFocusManager.java:771)
	at java.awt.Component.dispatchEventImpl(Component.java:4760)
	at java.awt.Container.dispatchEventImpl(Container.java:2294)
	at java.awt.Window.dispatchEventImpl(Window.java:2746)
	at java.awt.Component.dispatchEvent(Component.java:4711)
	at java.awt.EventQueue.dispatchEventImpl(EventQueue.java:758)
	at java.awt.EventQueue.access$500(EventQueue.java:97)
	at java.awt.EventQueue$3.run(EventQueue.java:709)
	at java.awt.EventQueue$3.run(EventQueue.java:703)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:80)
	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:90)
	at java.awt.EventQueue$4.run(EventQueue.java:731)
	at java.awt.EventQueue$4.run(EventQueue.java:729)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:80)
	at java.awt.EventQueue.dispatchEvent(EventQueue.java:728)
	at org.GNOME.Accessibility.AtkWrapper$5.dispatchEvent(AtkWrapper.java:700)
	at java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:201)
	at java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:116)
	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:105)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:93)
	at java.awt.EventDispatchThread.run(EventDispatchThread.java:82)
*/