package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.PopupMessageClass;

public class errorPopupController {
    @FXML
    private Label errorMessage;

    /** this method puts the message we give on the popup screen
     *
     */
    @FXML
    public void initialize() {
        errorMessage.setText(PopupMessageClass.getErrormessage());
    }

}