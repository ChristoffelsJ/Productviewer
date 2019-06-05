package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Category;
import model.Product;

import java.sql.SQLException;

public class PopupAddCategory {
    @FXML
    private TextField category;
    @FXML
    private Button categoryAddButton;

    public void addCategory(ActionEvent actionEvent) {
///*        Category categoryke=new Category(category.getText());*/
//        Stage stage = (Stage) categoryAddButton.getScene().getWindow();
//        stage.close();
//        try {
//            model.ProductDAO.addCategory(category.getText());
//        } catch (SQLException e) {
//            e.printStackTrace();
//       Product product=new Product("null",category.getText(),"null","null");
//        Stage stage = (Stage) categoryAddButton.getScene().getWindow();
//        stage.close();
//        try {
//      model.ProductDAO.addProduct("null",category.getText(),"null","null",null);
//       } catch (SQLException e) {
//           e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}

