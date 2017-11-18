package cointracker;

import GUI.UIManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CoinTracker {
    public static final String SINGLE_RUN_STRING = "Cointracker single run server!\n";
    private static APIwrapper w;

    public static void main(String[] args) {
        try {
            Socket cs = new Socket("127.0.0.1", 54312);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()))) {
                String line = br.readLine()+"\n";
                if (line.equals(SINGLE_RUN_STRING))
                    notifyOnError("Running", "Program already Running!");
            }
            //cs.getOutputStream() //find if this is the server
        } catch (UnknownHostException ex) {
            notifyOnError("Port error", "Can't find host(report bug)!");
        } catch (IOException ioe) {
            new UIManager();
        }
    }

    public static void notifyOnError(String title, String msg) {
        JOptionPane.showMessageDialog(new JFrame(),
        msg,title,JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    
}
