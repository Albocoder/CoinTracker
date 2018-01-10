package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.json.JSONObject;

public class SendBugReport extends JFrame{
    private URL target = null;
    private JTextArea email;
    private JTextArea msg;
    
    public SendBugReport(){
        JButton b1 = new JButton("Send");
        b1.addActionListener(this::sendReport);
        
        JLabel l1 = new JLabel("  Email: ");
        JLabel l2 = new JLabel("  Message: ");
        email = new JTextArea();
        msg = new JTextArea(11,40);
        
        JPanel ptop = new JPanel();
        JPanel pmid = new JPanel();
        JPanel pbot = new JPanel();
        
        ptop.setLayout(new BorderLayout());
        ptop.add(l1,BorderLayout.WEST);
        ptop.add(email,BorderLayout.CENTER);
        ptop.add(l2,BorderLayout.SOUTH);
        ptop.add(new JLabel(" "),BorderLayout.EAST);
        pmid.add(msg);
        pbot.setLayout(new BorderLayout());
        pbot.add(b1,BorderLayout.EAST);
        
        setLayout(new BorderLayout());
        add(ptop,BorderLayout.NORTH);
        add(pmid,BorderLayout.CENTER);
        add(pbot,BorderLayout.SOUTH);
        
        pack();
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public void sendReport(ActionEvent e){
        try {
            target = new URL("erin.avllazagaj.ug.bilkent.edu.tr/BugMailer/reportBug.php");
            URLConnection con = target.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.disconnect();
            http.setRequestMethod("GET");
            http.setRequestProperty("softname", "CoinTracker");
            http.setRequestProperty("email", email.getText());
            http.setRequestProperty("message", msg.getText());
            http.connect();
            BufferedReader br = new BufferedReader( new InputStreamReader(http.getInputStream()));
            String result = br.readLine();
            JSONObject resp = new JSONObject(result);
            int err = resp.getInt("error");
            if(err == 0)
                JOptionPane.showMessageDialog(this,"Report sent! Thank you!","Success!",JOptionPane.PLAIN_MESSAGE);
            else{
                String msg = resp.getString("msg");
                JOptionPane.showMessageDialog(this,msg,"Failed!",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            Logger.getLogger(SendBugReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
