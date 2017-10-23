package GUI;

public class UIManager {
    SetupScreen ss;
    NotificationManager nm;
    public UIManager(){
        //ss = new SetupScreen(this);
        //ss.setVisible(true);
        nm = new NotificationManager();
        nm.notify("danger","Sorry!","I made you lose money xD");
        nm.notify("warning", "Warning", "Bitcoin is not moving!");
        nm.notify("success","Bought!","Bitcons bought at high price!");
        
    }
    
    public void terminate(int code){
        System.exit(code);
    }
    public void Notify(){
        
    }
}
