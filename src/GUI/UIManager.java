package GUI;

public class UIManager {
    SetupScreen ss;
    NotificationManager nm;
    public UIManager(){
        //ss = new SetupScreen(this);
        //ss.setVisible(true);
        nm = new NotificationManager("topleft");
        nm.notify("danger","Sorry!","0123456789012345678901234567890123456789");
        nm.notify("warning", "Warning", "Bitcoin is not moving!");
        nm.notify("success","Bought!","Bitcons bought at high price!");
        
    }
    
    public void terminate(int code){
        System.exit(code);
    }
    public void Notify(){
        
    }
}
