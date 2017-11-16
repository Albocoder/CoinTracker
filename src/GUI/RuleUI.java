package GUI;

import static GUI.RulePanel.CURR_DIR;
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
    
    public RuleUI() {
        rh = new RuleHandler();
        fullWrapper = new JPanel();
        terminator = new DeleteHandler();
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
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                minimizeToTray();
            }
        });
    }
    private void minimizeToTray(){
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported"); // do smth different like nitify
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(
                Toolkit.getDefaultToolkit().createImage(CURR_DIR+"/img/icon.png"));
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
       
        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
        exitItem.addActionListener((ActionEvent e) -> {
            System.exit(0); //not really this to be done, first save the rules
        });
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
        rh.addRule(target.getRule());
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