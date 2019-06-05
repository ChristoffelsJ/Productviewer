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
import java.sql.SQLException;

public class PopupAddProductController {
    @FXML private Button addProductButton;
    @FXML private TextField productTitle;
    @FXML private ComboBox <String> category;
    @FXML private TextField price;
    @FXML private TextField description;
    @FXML private Button addPictureButton;
    @FXML private ImageView imageView;
    @FXML private FileInputStream fis;

    private Path imagePath;

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
        model.ProductDAO.addProduct(productTitle.getText(), category.getValue(), price.getText(), description.getText(), imagePath,0);
        Stage stage = (Stage) addProductButton.getScene().getWindow();
        stage.close();

    }
}

