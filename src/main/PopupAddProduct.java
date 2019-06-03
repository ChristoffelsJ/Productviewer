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
import java.sql.SQLException;

public class PopupAddProduct {
    @FXML private Button addProductButton;
    @FXML private TextField productTitle;
    @FXML private ComboBox <String> category;
    @FXML private TextField price;
    @FXML private TextField description;
    @FXML private Button addPictureButton;
    @FXML private File selectedFile;
    @FXML private Image imageFile;
    @FXML private ImageView imageView;
    @FXML private FileInputStream fis;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        category.setItems(generateInitialCategory());
    }

    private ObservableList<String> generateInitialCategory() throws SQLException, ClassNotFoundException {
        ObservableList<String> oListCategory = FXCollections.observableArrayList(model.CategoryDAO.getInitialCategory());
        return oListCategory;
    }

    @FXML
    public void PictureButtonAction(ActionEvent actionEvent) throws MalformedURLException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        selectedFile = chooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String imagepath = selectedFile.toURI().toURL().toString();
            imageFile = new Image(imagepath);
            imageView.setImage(imageFile);
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

        }
    }
    @FXML
    private void addProduct(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        model.ProductDAO.addProduct(productTitle.getText(), category.getValue(), price.getText(), description.getText(), imageView,0);
        Stage stage = (Stage) addProductButton.getScene().getWindow();
        stage.close();
    }
}

