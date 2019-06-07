package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Category;

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
        model.CategoryDAO.addCategory(category.getMainCategory(), category.getSubCategory());
    }
}

