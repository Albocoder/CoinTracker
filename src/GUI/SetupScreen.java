package GUI;

import javax.swing.*;

public class SetupScreen extends JFrame{
    GraphPanel gp;
    JButton settings;
    UIManager theBoss;
    
    SetupScreen(UIManager b){
        theBoss = b;
        settings = new JButton("set");
        gp = new GraphPanel();
        add(gp);
        pack();
        //b.terminate(0);
    }
    
}
