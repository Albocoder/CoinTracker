package GUI;

import GUI.RuleUI.DeleteHandler;
import cointracker.Rule;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;

class RulePanel extends JPanel{
    public static final String CURR_DIR = System.getProperty("user.dir");
    public static final ImageIcon 
    editimg = new ImageIcon(CURR_DIR+"/img/edit.png"),
    editimg_pressed = new ImageIcon(CURR_DIR+"/img/edit_pressed.png"),
    playimg = new ImageIcon(CURR_DIR+"/img/play.png"),
    pauseimg = new ImageIcon(CURR_DIR+"/img/pause.png"),
    closeimg = new ImageIcon(CURR_DIR+"/img/delete.png"),
    closeimg_pressed = new ImageIcon(CURR_DIR+"/img/delete_pressed.png");

    private JCheckBox ruleCheckox;
    private JButton deleteBtn,editBtn;
    private JTextField ruleDescription;
    private JToggleButton ruleToggleActivation;
    private JPanel wrapper;
    private Rule r;
    private boolean selected, running, killed;

    private RulePanel() {
        wrapper = new JPanel();
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
        deleteBtn.setIcon(closeimg);
        editBtn.addMouseListener(new EditHandler());
        

        ruleToggleActivation.setBorder(null);
        ruleToggleActivation.setIcon(pauseimg);
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
        add(wrapper);
    }

    public RulePanel(DeleteHandler h,Rule r) {
        this();
        deleteBtn.addMouseListener(h);
        this.r = r;
        populateComponents();
    }

    private void populateComponents(){
        if (r == null)  //this will populate the textarea with data of the rule
            ruleDescription.setEditable(false);
        // for example...
        selected = false;
        running = true;
    }

    ///////////////////////// ACTION EVENT FUNCTIONS ///////////////////////
    private void ruleToggleActionPerformed(ActionEvent evt) {                                            
        if (ruleToggleActivation.isSelected()){
            ruleToggleActivation.setIcon(playimg);
            ruleToggleActivation.setToolTipText("Run the rule");
        }
        else{
            ruleToggleActivation.setIcon(pauseimg);
            ruleToggleActivation.setToolTipText("Pause the rule");
        }
    }
    private void ruleDescriptionActionPerformed(ActionEvent evt) {
        
    }
    private void ruleCheckoxActionPerformed(ActionEvent evt) {
        
    }
    private class EditHandler extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            JButton b = (JButton)e.getComponent();
            b.setIcon(editimg_pressed);
        }
        @Override
        public void mouseReleased(MouseEvent e){
            JButton b = (JButton)e.getComponent();
            if (ruleDescription.isEditable()){
                b.setIcon(editimg);
                ruleDescription.setEditable(false);
                // set up the new rule to run
            }
            else{
                b.setIcon(editimg_pressed);
                ruleDescription.setEditable(true);
            }
        }
    }
}
