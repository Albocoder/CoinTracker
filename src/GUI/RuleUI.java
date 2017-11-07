package GUI;

import cointracker.Rule;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class RuleUI extends JPanel {            
    private JButton ruleAdd,ruleDelete,ruleDisable;
    private RulePanel rulePanel;
    private JPanel upperButtonsPanel;
    private JScrollPane allRulesPane;
    private final ImageIcon trashimg,editimg,playimg,pauseimg;
    
    public RuleUI() {
        trashimg = new ImageIcon(System.getProperty("user.dir")+"/img/trashbin.png");
        editimg = new ImageIcon(System.getProperty("user.dir")+"/img/edit.png");
        playimg = new ImageIcon(System.getProperty("user.dir")+"/img/play.png");
        pauseimg = new ImageIcon(System.getProperty("user.dir")+"/img/pause.png");
        rulePanel = new RulePanel();
        initComponents();
    }

    @SuppressWarnings("unchecked")                    
    private void initComponents() {
        allRulesPane = new JScrollPane();
        rulePanel = new RulePanel();
        upperButtonsPanel = new JPanel();
        ruleDelete = new JButton();
        ruleDisable = new JButton();
        ruleAdd = new JButton();
        
        GroupLayout rulePanelLayout = new GroupLayout(rulePanel);
        rulePanel.setLayout(rulePanelLayout);
        rulePanelLayout.setHorizontalGroup(
            rulePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(rulePanelLayout.createSequentialGroup()
                .addComponent(rulePanel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        rulePanelLayout.setVerticalGroup(
            rulePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(rulePanel)
        );

        allRulesPane.setViewportView(rulePanel);

        ruleDelete.setText("Delete Rule");

        ruleDisable.setText("Disable Rule");

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

        ruleAdd.setText("Add Rule");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
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
        
    }
    private class RulePanel extends JPanel{
        private JCheckBox ruleCheckox;
        private JButton deleteBtn,editBtn;
        private JTextField ruleDescription;
        private JToggleButton ruleToggleActivation;

        private RulePanel() {
            ruleCheckox = new JCheckBox();
            ruleDescription = new JTextField();
            editBtn = new JButton();
            deleteBtn = new JButton();
            ruleToggleActivation = new JToggleButton();
            editBtn.setBorder(null);
            deleteBtn.setBorder(null);
            editBtn.setBackground(Color.WHITE);
            deleteBtn.setBackground(Color.WHITE);
            editBtn.setIcon(editimg);
            deleteBtn.setIcon(trashimg);
            ruleToggleActivation.setBorder(null);
            ruleToggleActivation.setBackground(new Color(0,0,0,1));
            ruleToggleActivation.setIcon(playimg);
            ruleToggleActivation.setToolTipText("Disable the rule");
            ruleCheckox.addActionListener(this::ruleCheckoxActionPerformed);
            ruleDescription.addActionListener(this::ruleDescriptionActionPerformed);
            ruleToggleActivation.addActionListener(this::ruleToggleActionPerformed);
            GroupLayout rulePanelLayout = new GroupLayout(rulePanel);
            rulePanel.setLayout(rulePanelLayout);
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
                    .addComponent(editBtn, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(editBtn, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(10, Short.MAX_VALUE))
            );
        }
        
        ///////////////////////// ACTION EVENT FUNCTIONS ///////////////////////
        private void ruleToggleActionPerformed(ActionEvent evt) {                                            
            if (ruleToggleActivation.isSelected()){
                ruleToggleActivation.setIcon(pauseimg);
                ruleToggleActivation.setToolTipText("Disable the rule");
            }
            else{
                ruleToggleActivation.setIcon(playimg);
                ruleToggleActivation.setToolTipText("Enable the rule");
            }
        }
        private void ruleCheckoxActionPerformed(ActionEvent evt) {                                           
            // TODO add your handling code here:
        }                                          

        private void ruleDescriptionActionPerformed(ActionEvent evt) {                                            
            // TODO add your handling code here:
        }
    }
}
