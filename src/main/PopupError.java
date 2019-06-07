package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.ErrorClass;

public class PopupError {
    @FXML
    private Label errorMessage;


    @FXML
    public void initialize() {
        errorMessage.setText(ErrorClass.getErrormessage());
    }

}