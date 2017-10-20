package GUI;

public class UIManager {
    SetupScreen ss;
    public UIManager(){
        //ss = new SetupScreen(this);
        //ss.setVisible(true);
        Notify();
    }
    
    public void terminate(int code){
        System.exit(code);
    }
    public void Notify(){
        new Notification().run();
    }
}
