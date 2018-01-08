package GUI;

import cointracker.Rule;
import cointracker.RuleHandler;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RuleUI extends JFrame {
    // Rule Handler
    RuleHandler rh;
    // stuff in the panel
    private JButton ruleAdd,ruleDelete,ruleDisable;
    private ArrayList<RulePanel> rulePanels;
    private JPanel upperButtonsPanel,allRulesPanel;
    private JScrollPane allRulesPane;
    // THE PANEL
    private final JPanel fullWrapper;
    private final DeleteHandler terminator;
    // for messages
    private boolean notShownMinimizationWarning = true;
    
    public RuleUI() {
        super("Cointracker");
        rh = new RuleHandler();
        fullWrapper = new JPanel();
        terminator = new DeleteHandler();
        this.setResizable(false);
        initComponents();
        add(fullWrapper);
        pack();
    }

    @SuppressWarnings("unchecked")                    
    private void initComponents() {
        allRulesPane = new JScrollPane();
        allRulesPanel = new JPanel();
        upperButtonsPanel = new JPanel();
        rulePanels = new ArrayList();
        ruleDelete = new JButton();
        ruleDisable = new JButton();
        ruleAdd = new JButton();
        
        for(Rule r: rh.getRules())
            rulePanels.add(new RulePanel(terminator,r));
        
        for(RulePanel rpan: rulePanels)
            allRulesPanel.add(rpan);
        resizeRulesPanel();
        
        allRulesPane.setBorder(null); 
        allRulesPane.setPreferredSize(new Dimension(440,250));
        allRulesPane.getVerticalScrollBar().setUnitIncrement(10);
        allRulesPane.setViewportView(allRulesPanel);
        
        upperButtonsPanel.setOpaque(false);
        
        ruleAdd.setText("Add Rule");
        ruleAdd.setBackground(Color.GREEN);
        ruleAdd.setFocusPainted(false);
        ruleAdd.addActionListener(this::ruleDescriptionActionPerformed);
        
        ruleDelete.setText("Delete Rule");
        ruleDelete.setBackground(Color.RED);
        ruleDelete.setFocusPainted(false);
        ruleDelete.addActionListener(this::deleteSelected);
        
        ruleDisable.setText("Disable Rule");
        ruleDisable.setBackground(Color.ORANGE);
        ruleDisable.setFocusPainted(false);
        ruleDisable.addActionListener(this::disableSelected);
                
        fullWrapper.setBackground(Color.BLUE);
        fullWrapper.setPreferredSize(new Dimension(465,300));
        
        GroupLayout upperButtonsPanelLayout = new GroupLayout(upperButtonsPanel);
        upperButtonsPanel.setLayout(upperButtonsPanelLayout);
        upperButtonsPanelLayout.setHorizontalGroup(
            upperButtonsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(upperButtonsPanelLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(ruleDelete)
                .addGap(30, 30, 30)
                .addComponent(ruleDisable))
        );
        upperButtonsPanelLayout.setVerticalGroup(
            upperButtonsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(upperButtonsPanelLayout.createSequentialGroup()
                .addGroup(upperButtonsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(ruleDelete)
                    .addComponent(ruleDisable))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        
        GroupLayout layout = new GroupLayout(fullWrapper);
        fullWrapper.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(allRulesPane)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(ruleAdd, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upperButtonsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(upperButtonsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ruleAdd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(allRulesPane, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addContainerGap())
        );
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        if (!SystemTray.isSupported()) {
            this.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (notShownMinimizationWarning){
                        new SingletonNotificationManager().nm.notify("warning","Warning!","Can\'t minimize, click to close.");
                        ((JFrame)e.getComponent()).setVisible(true);
                        notShownMinimizationWarning = false;
                    }
                    else{
                        rh.shutdown();
                        System.exit(0);
                    }
                }
            });
        }
        else{
            this.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent e) {
                    ((JFrame)e.getComponent()).setVisible(false);
                    minimizeToTray();
                }
            });
        }
    }
    private void minimizeToTray(){
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(
                Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("icon.png")));
        final SystemTray tray = SystemTray.getSystemTray();
        trayIcon.setToolTip("Cointracker");
        // Create a pop-up menu components
        MenuItem showItem = new MenuItem("Show");
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Disable Notifications");
        Menu displayMenu = new Menu("Action");
        MenuItem pauseItem = new MenuItem("Pause All");
        MenuItem runItem = new MenuItem("Run All");
        MenuItem reportBugItem = new MenuItem("Report Bug");
        MenuItem exitItem = new MenuItem("Exit");
       
        //Add components to pop-up menu
        popup.add(showItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(pauseItem);
        displayMenu.add(runItem);
        popup.addSeparator();
        popup.add(reportBugItem);
        popup.add(aboutItem);
        popup.add(exitItem);
        
        pauseItem.addActionListener((ActionEvent e)->{
            for(RulePanel rpan: rulePanels)
                rpan.disableRule();
        });
        pauseItem.addActionListener((ActionEvent e)->{
            for(RulePanel rpan: rulePanels)
                rpan.enableRule();
        });
        showItem.addActionListener((ActionEvent e)->{
            this.setVisible(true);
            tray.remove(trayIcon);
        });
        cb1.addItemListener((ItemEvent e) -> {
            if(cb1.getState())
                for(RulePanel rpan: rulePanels)
                    rpan.setNotification(false);
            else
                for(RulePanel rpan: rulePanels)
                    rpan.setNotification(true);
        });
        exitItem.addActionListener((ActionEvent e) -> {
            rh.shutdown();
            System.exit(0);
        });
        //TODO: reportBugItem,aboutItem
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {}
    }
    private void ruleDescriptionActionPerformed(ActionEvent evt) {
        addRulePanel(null);//somehow like this although it must be first not last.
    }
    private void disableSelected(ActionEvent e){
        for (RulePanel rpan:rulePanels){
            if(rpan.isChecked()){
                rpan.disableRule();
                rpan.setChecked(false);
            }
        }
    }   
    private void deleteSelected(ActionEvent e){
        for (RulePanel rpan:rulePanels)
            if (rpan.isChecked()){
                deleteRulePanel(rpan);
                break;
            }
    }
    private void deleteRulePanel(RulePanel target){
        target.setVisible(false);
        target.disableRule();
        rh.deleteRule(target.getRule());
        rulePanels.remove(target);
        allRulesPanel.remove(target);
        resizeRulesPanel();
        revalidate();
    }
    private void addRulePanel(Rule ru){
        RulePanel target = new RulePanel(terminator,ru);
        rh.addRule(ru);
        rulePanels.add(target);
        allRulesPanel.add(target);
        resizeRulesPanel();
        revalidate();
    }
    private void resizeRulesPanel(){
        allRulesPanel.setPreferredSize(new Dimension(400,65*rh.getRules().size()));
    }
    // PRIVATE CLASSES
    public class DeleteHandler extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            JButton b = (JButton)e.getComponent();
            b.setIcon(RulePanel.CLOSEIMG_PRESSED);
        }
        @Override
        public void mouseReleased(MouseEvent e){
            JButton b = (JButton)e.getComponent();
            b.setIcon(RulePanel.CLOSEIMG);
            RulePanel target = (RulePanel)(b.getParent().getParent());
            deleteRulePanel(target);
        }
    }
}