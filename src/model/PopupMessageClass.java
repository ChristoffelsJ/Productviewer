package model;

import javafx.event.ActionEvent;
import main.MainController;

public class PopupMessageClass {
    /**
     *
     */
    private static String Errormessage;

    /**
     *
     * @return
     */
    public static String getErrormessage() {
        return Errormessage;
    }

    /**
     *
     * @param errormessage
     */
    public static void setErrormessage(String errormessage) {
        Errormessage = errormessage;
    }


}
