package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.PopupMessageClass;

public class Popup {
    @FXML
    private Label errorMessage;


    @FXML
    public void initialize() {
        errorMessage.setText(PopupMessageClass.getErrormessage());
    }

}