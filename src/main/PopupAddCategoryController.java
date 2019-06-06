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

public class PopupAddCategoryController {
    @FXML
    private TextField mainCategory;
    @FXML
    private TextField subCategory;
    @FXML
    private Button categoryAddButton;

    public void addCategory(ActionEvent actionEvent) {

        Category category = new Category();
        category.setMainCategory(mainCategory.getText());
        category.setSubCategory(subCategory.getText());
        Stage stage = (Stage) categoryAddButton.getScene().getWindow();
        stage.close();
        if (category.getMainCategory().equals("")) {
            category.setMainCategory("No main category");
        }
        try {
            model.CategoryDAO.addCategory(category.getMainCategory(), category.getSubCategory());
        } catch (SQLException e) {
            System.out.println("This sub category is already entered");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

