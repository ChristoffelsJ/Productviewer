package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class PopupAddProductController {

    @FXML private Button addProductButton;
    @FXML private TextField productTitle;
    @FXML private ComboBox <String> mainCategory;
    @FXML private ComboBox<String> subCategory;
    @FXML private TextField price;
    @FXML private TextField description;
    @FXML private ImageView imageView;

    private Path imagePath;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        mainCategory.setItems(generateInitialMainCategory());
        subCategory.setItems(generateInitialSubCategory());
    }

    private ObservableList<String> generateInitialMainCategory() throws SQLException, ClassNotFoundException {
        return FXCollections.observableArrayList(model.CategoryDAO.getInitialMainCategory());
    }

    private ObservableList<String> generateInitialSubCategory() throws SQLException, ClassNotFoundException {
        return FXCollections.observableArrayList(model.CategoryDAO.getInitialSubCategory());
    }

    @FXML
    public void PictureButtonAction(ActionEvent actionEvent) throws MalformedURLException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File selectedFile = chooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            imagePath = selectedFile.toPath();
            try(InputStream is = Files.newInputStream(imagePath)){
                Image imageFile = new Image(is);
                imageView.setImage(imageFile);
                imageView.setFitWidth(100);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addProduct(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, IOException {
        model.ProductDAO.addProduct(productTitle.getText(),Integer.parseInt(subCategory.getValue()), Integer.parseInt(mainCategory.getValue()), price.getText(), description.getText(), imagePath,0);
        Stage stage = (Stage) addProductButton.getScene().getWindow();
        stage.close();

    }
}

