package model;

import javafx.event.ActionEvent;
import main.MainController;

public class PopupMessageClass {
    private static String Errormessage;

    public static String getErrormessage() {
        return Errormessage;
    }

    public static void setErrormessage(String errormessage) {
        Errormessage = errormessage;
    }


}
