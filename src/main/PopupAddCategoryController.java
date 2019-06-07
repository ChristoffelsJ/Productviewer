package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Category;

import static main.MainController.throwErrorStatic;
import static main.MainController.throwPositiveStatic;

public class PopupAddCategoryController {
    @FXML
    private TextField mainCategory;
    @FXML
    private TextField subCategory;
    @FXML
    private Button categoryAddButton;

    public void addCategory(ActionEvent actionEvent) throws Exception {

        Category category = new Category();
        category.setMainCategory(mainCategory.getText());
        category.setSubCategory(subCategory.getText());
        Stage stage = (Stage) categoryAddButton.getScene().getWindow();

        if (category.getMainCategory().equals("")) {
            category.setMainCategory("Other...");
        }
        if (category.getSubCategory().equals("")) {
            throwErrorStatic(actionEvent, "You must fill in a subcategory");
        } else {
            model.CategoryDAO.addCategory(category.getMainCategory(), category.getSubCategory());
            stage.close();
            throwPositiveStatic("Great success");


        }
    }
}

