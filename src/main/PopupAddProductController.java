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
import model.ProductDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/** Controller class of PopupAddProduct.fxml
 *
 */
public class PopupAddProductController {

    @FXML private Button addProductButton;
    @FXML private TextField productTitle;
    @FXML private ComboBox <String> mainCategory;
    @FXML private ComboBox<String> subCategory;
    @FXML private TextField price;
    @FXML private TextField description;
    @FXML private ImageView imageView;
    private Path imagePath;

    /** initialize the maincategory combobox
     *
     */
    @FXML
    public void initialize(){
        mainCategory.setItems(generateInitialMainCategory());
    }

    /** gets all the main category's
     *
     * @return a list of all the Maincategory
     */
    private ObservableList<String> generateInitialMainCategory(){
        return FXCollections.observableArrayList(model.CategoryDAO.getInitialMainCategory());
    }

    /** gets all the sub category's
     *
     * @return list of all the subCategory
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private ObservableList<String> generateInitialSubCategory() throws SQLException, ClassNotFoundException {
        return FXCollections.observableArrayList(model.CategoryDAO.getInitialSubCategory(mainCategory.getValue()));
    }

    /** refresh the list off subs
     *
     * @param actionEvent when men press the combox of subCathegory
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    private void refreshSubList (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       subCategory.setItems(generateInitialSubCategory());
    }

    /** add a picture
     *
     * @param actionEvent button press add picture
     */
    @FXML
    private void PictureButtonAction(ActionEvent actionEvent){
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJpg = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        chooser.getExtensionFilters().add(extFilterJpg);
        chooser.getExtensionFilters().add(extFilterPng);
        chooser.setTitle("Open File");
        File selectedFile = chooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            imagePath = selectedFile.toPath();
            try(InputStream is = Files.newInputStream(imagePath)){
                Image imageFile = new Image(is);
                imageView.setImage(imageFile);
                imageView.setFitWidth(100);
                imageView.setFitHeight(120);
                imageView.setPreserveRatio(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** add a product to the viewlist
     *
     * @param actionEvent button add product pressed
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    private void addProduct(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, IOException {
        if (imagePath == null){
            imagePath = Paths.get("standardImage.jpg");
        }
        model.ProductDAO.addProduct(productTitle.getText(),subCategory.getValue(), mainCategory.getValue(), price.getText(), description.getText(), imagePath,0);
       Stage stage = (Stage) addProductButton.getScene().getWindow();
        stage.close();
    }
}

