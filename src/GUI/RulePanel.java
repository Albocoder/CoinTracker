package GUI;

import Exceptions.MalformedRuleStringException;
import GUI.RuleUI.DeleteHandler;
import cointracker.Rule;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.*;

class RulePanel extends JPanel{
    public static final String CURR_DIR = System.getProperty("user.dir");
    public static final ImageIcon 
    REFRESH = new ImageIcon(CURR_DIR+"/img/refresh.png"),
    REFRESH_PRESSED = new ImageIcon(CURR_DIR+"/img/refresh_pressed.png"),
    PLAYIMG = new ImageIcon(CURR_DIR+"/img/play.png"),
    PLAYIMG_GRAY = new ImageIcon(CURR_DIR+"/img/play_grayscale.png"),
    PAUSEIMG = new ImageIcon(CURR_DIR+"/img/pause.png"),
    PAUSEIMG_GRAY = new ImageIcon(CURR_DIR+"/img/pause_grayscale.png"),
    CLOSEIMG = new ImageIcon(CURR_DIR+"/img/delete.png"),
    CLOSEIMG_PRESSED = new ImageIcon(CURR_DIR+"/img/delete_pressed.png");

    private JCheckBox ruleCheckox;
    private JButton deleteBtn,refrBtn;
    private JTextField ruleDescription;
    private JToggleButton ruleToggleActivation;
    private JPanel wrapper;
    private Rule r;
    private ScheduledExecutorService scheduler;

    private RulePanel() {
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
        ruleCheckox.addActionListener(this::ruleCheckoxActionPerformed);
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
        populateComponents();
        scheduler = Executors.newScheduledThreadPool(1);
        this.start();
    }

    private void populateComponents(){
        if (r == null){  //this will populate the textarea with data of the rule
            ruleDescription.setEditable(false); //not really this
            ruleDescription.setText("NO-RULE");
            
        }
    }
    
    // Toggle button enable/disable
    public void disableToggleButton(){
        if (ruleToggleActivation.isSelected())
            ruleToggleActivation.setIcon(PAUSEIMG_GRAY);
        else
            ruleToggleActivation.setIcon(PLAYIMG_GRAY);
        ruleToggleActivation.setEnabled(false);
    }
    public void enableToggleButton(){
        if (ruleToggleActivation.isSelected())
            ruleToggleActivation.setIcon(PAUSEIMG);
        else
            ruleToggleActivation.setIcon(PLAYIMG);
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
    public void pause(){
        if(scheduler == null)
            return;
        scheduler.shutdown();
    }
    public void stop(){
        pause();
    }
    public void checkRule(){
        boolean check = r.checkRule();
        if (check){
            // notify 
        }
    }

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
            ruleDescription.setEditable(false);
            if(!r.toString().equals(ruleDescription.getText())){
                disableToggleButton();
                this.stop();
                try{
                    r = new Rule(ruleDescription.getText());
                    enableToggleButton();
                    if(ruleToggleActivation.isSelected())
                        this.start();
                }catch(MalformedRuleStringException mrse){}
            }
        }
    }
    private void ruleCheckoxActionPerformed(ActionEvent evt) {
        
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
