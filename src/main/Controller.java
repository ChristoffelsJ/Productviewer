package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> columnProductTitle;
    @FXML
    private TableColumn<Product, String> columnCategory;
    @FXML
    private TableColumn<Product, String> columnPrice;
    @FXML
    private TableColumn<Product, String> columnProductDescription;

    @FXML
    private TextField search;


    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        columnProductTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        productTable.getItems().setAll(generateInitialProducts());
    }

    private List<Product> generateInitialProducts() throws SQLException, ClassNotFoundException {
        return model.ProductDAO.getInitialProducts();
    }

    @FXML
    private void search(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        productTable.getItems().setAll(model.ProductDAO.search(search.getText()));
    }

    @FXML
    private void reset(ActionEvent actionEvent) throws ClassNotFoundException, SQLException{
        initialize();
    }
    @FXML
    private void Exit(ActionEvent actionEvent)throws  ClassNotFoundException,SQLException{
        Platform.exit ();

    }

    @FXML
    private void openAddProductPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupAddProduct.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1, 400, 80));
            stage.show();
        } catch (Exception E) {
            System.out.println("Something went wrong");
        }
    }

    @FXML
    private void openAddCategoryPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupAddCategory.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1, 300, 60));
            stage.show();
        } catch (Exception E) {
            System.out.println("Something went wrong");
        }

    }


}
