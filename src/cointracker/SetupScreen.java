package cointracker;

import javax.swing.*;

public class SetupScreen extends JFrame{
    GraphPanel gp;
    JButton settings;
    SetupScreen(){
        settings = new JButton("set");
        add(settings);
    }
}
