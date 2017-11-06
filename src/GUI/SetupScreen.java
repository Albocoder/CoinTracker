package GUI;

import javax.swing.*;

public class SetupScreen extends JFrame{
    GraphPanel gp;
    RulePanel rp;
    JButton settings;
    UIManager theBoss;
    
    SetupScreen(UIManager b){
        theBoss = b;
        settings = new JButton("set");
        rp = new RulePanel();
        add(rp);
        pack();
        setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //b.terminate(0);
    }
}
