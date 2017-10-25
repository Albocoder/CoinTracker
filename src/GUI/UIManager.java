package GUI;

public class UIManager {
    SetupScreen ss;
    NotificationManager nm;
    public UIManager(){
        //ss = new SetupScreen(this);
        //ss.setVisible(true);
        nm = new NotificationManager("topright");
        nm.notifyNoAutokill("success","New Coin!","Bitcons Gold is out!");
        nm.notifyNoAutokill("warning", "Start early!", "Please start mining BTG asap!");
        nm.notifyNoAutokill("danger","BTG falling!","BTG has fallen by 70%!");
        nm = new NotificationManager("topleft");
        nm.notifyNoAutokill("success","New Coin!","Bitcons Gold is out!");
        nm.notifyNoAutokill("warning", "Start early!", "Please start mining BTG asap!");
        nm.notifyNoAutokill("danger","BTG falling!","BTG has fallen by 70%!");
        nm = new NotificationManager("bottomleft");
        nm.notifyNoAutokill("success","New Coin!","Bitcons Gold is out!");
        nm.notifyNoAutokill("warning", "Start early!", "Please start mining BTG asap!");
        nm.notifyNoAutokill("danger","BTG falling!","BTG has fallen by 70%!");
        nm = new NotificationManager("bottomright");
        nm.notifyNoAutokill("success","New Coin!","Bitcons Gold is out!");
        nm.notifyNoAutokill("warning", "Start early!", "Please start mining BTG asap!");
        nm.notifyNoAutokill("danger","BTG falling!","BTG has fallen by 70%!");
    }
    
    public void terminate(int code){
        System.exit(code);
    }
    public void Notify(){
        
    }
}
