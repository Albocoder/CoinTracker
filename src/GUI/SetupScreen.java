package GUI;

import javax.swing.*;

public class SetupScreen extends JFrame{
    GraphPanel gp;
    
    JButton settings;
    UIManager theBoss;
    
    SetupScreen(UIManager b){
        theBoss = b;
        settings = new JButton("set");
        pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //b.terminate(0);
    }
}
