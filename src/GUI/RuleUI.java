package GUI;

import cointracker.Rule;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RuleUI extends JFrame {
    // stuff in the panel
    private JButton ruleAdd,ruleDelete,ruleDisable;
    private ArrayList<RulePanel> rulePanels;
    private JPanel upperButtonsPanel,allRulesPanel;
    private JScrollPane allRulesPane;
    // THE PANEL
    private final JPanel fullWrapper;
    private final DeleteHandler terminator;
    
    public RuleUI() {
        fullWrapper = new JPanel();
        terminator = new DeleteHandler();
        initComponents();
        add(fullWrapper);
        pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // temporary
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
        
        allRulesPanel.setPreferredSize(new Dimension(400,500));//must be dynamic
        allRulesPanel.add(new RulePanel(terminator,null));
        allRulesPanel.add(new RulePanel(terminator,null));
        allRulesPanel.add(new RulePanel(terminator,null));
        allRulesPanel.add(new RulePanel(terminator,null));
        
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
        
        ruleDisable.setText("Disable Rule");
        ruleDisable.setBackground(Color.ORANGE);
        ruleDisable.setFocusPainted(false);
        
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
    }
    
    public void showRule(Rule r){
        allRulesPanel.add(new RulePanel(terminator,r));
        revalidate();
    }
    private void ruleDescriptionActionPerformed(ActionEvent evt) {
        showRule(null);//somehow like this although it must be first not last.
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
            target.setVisible(false);
            //find it in the arraylist and remove it from there 
            //and stop it from running
        }
    }
}