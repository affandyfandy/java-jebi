package jebi.hendardi;

import jebi.hendardi.control.AppManager;

public class App {
    public static void main(String[] args) {
        AppManager appManager = AppManager.getInstance();
        appManager.start();
    }
}
