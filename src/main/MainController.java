package main;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import javafx.util.converter.DefaultStringConverter;
import model.*;
import util.DBUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * This class is the conroller of viewer.fxml
 */
public class MainController {
    @FXML
    private TableColumn<Product, String> columnMainCategory;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> columnProductTitle;
    @FXML
    private TableColumn<Product, String> columnSubCategory;
    @FXML
    private TableColumn<Product, String> columnPrice;
    @FXML
    private TableColumn<Product, String> columnProductDescription;
    @FXML
    private TableColumn<Product, ImageView> columnPicture;
    @FXML
    private TextField search;
    @FXML
    public MenuItem openProductCSV;
    @FXML
    private FileChooser fileChooser;
    @FXML
    private Pane pane;
    private Path imagePath;

    /** initialize method, here the tableview is made
     *
     */
    @FXML
    public void initialize() {
        columnProductTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnSubCategory.setCellValueFactory(new PropertyValueFactory<>("subCategory"));
        columnMainCategory.setCellValueFactory(new PropertyValueFactory<>("mainCategory"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPicture.setCellValueFactory(new PropertyValueFactory<>("image"));
        productTable.getItems().setAll(generateInitialProducts());
        editableColumn();
    }

    /** This is for making the tableview editable, also to save the edits to the database.
     *
     */
    private void editableColumn() {
        ObservableList <String> dataSub  = FXCollections.observableArrayList();
        ObservableList <String> dataMain  = FXCollections.observableArrayList();

        columnProductTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        columnProductTitle.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setTitle(event.getNewValue());
            updateData("productTitle", event.getNewValue(), product.getProductId());
        });

            columnSubCategory.setOnEditStart(event -> {Product product = event.getRowValue();
            dataSub.clear();
            dataSub.addAll(CategoryDAO.getInitialSubCategory(product.getMainCategory()));
        });

        columnSubCategory.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(),dataSub));
        columnSubCategory.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setSubCategory((event.getNewValue()));
            updateData("subCategory", event.getNewValue(), product.getProductId());
        });
        dataMain.addAll(CategoryDAO.getInitialMainCategory());
        columnMainCategory.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(),dataMain));
        columnMainCategory.setOnEditCommit(event -> {
                    Product product = event.getRowValue();
                    product.setMainCategory((event.getNewValue()));
                    updateData("mainCategory", event.getNewValue(), product.getProductId());
                    columnSubCategory.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(),dataSub));
                    product.setSubCategory(CategoryDAO.getInitialSubCategory(product.getMainCategory()).get(0));
                    updateData("subCategory", CategoryDAO.getInitialSubCategory(product.getMainCategory()).get(0), product.getProductId());
        });

        columnPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        columnPrice.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setPrice(event.getNewValue());
            updateData("price", event.getNewValue(), product.getProductId());
        });

        columnProductDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        columnProductDescription.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setDescription(event.getNewValue());
            updateData("productDescription", event.getNewValue(), product.getProductId());
        });

        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJpg = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        chooser.getExtensionFilters().add(extFilterJpg);
        chooser.getExtensionFilters().add(extFilterPng);
        chooser.setTitle("Open File");

        columnPicture.setOnEditStart(event -> {Product product = event.getRowValue();
            File selectedFile = chooser.showOpenDialog(new Stage());

            if (selectedFile != null) {
                imagePath = selectedFile.toPath();
                updateDataImage(product.getProductId(), imagePath);
            }
        });
            productTable.setEditable(true);
    }

    /**update the path from the image
     * @param id give the product id
     * @param imagePath give the path for the image
     */
    private void updateDataImage(int id, Path imagePath) {
        String stringPath = imagePath.toString().replace("\\","/");
        String update = "UPDATE products SET image = ? WHERE productId = " + id + "";
        String update1 = "UPDATE products SET imagePath = '" + stringPath + "' WHERE productId = " + id + "";

        try(InputStream inputStream = Files.newInputStream(imagePath);
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setBinaryStream(1,inputStream);
            preparedStatement.executeUpdate();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        DBUtil.updateQuery(update1);
        initialize();
    }

    /** this is the query for updating the database
     *
     * @param column Enter te column name of the Database
     * @param newValue Give the new value
     * @param id Give the product id
     */
    private void updateData(String column, String newValue, int id) {
        String query = "UPDATE products SET " + column + " = '" + newValue + "' WHERE productId = " + id + "";
        DBUtil.updateQuery(query);
    }


    /** gets a list of the products in the database
     *
     * @return A list of products
     */
    private List<Product> generateInitialProducts() {
        return model.ProductDAO.getInitialProducts();
    }

    /** sets the products table with all search results
     *
     * @param actionEvent Button press search
     */
    @FXML
    private void search(ActionEvent actionEvent) {
        productTable.getItems().setAll(model.ProductDAO.search(search.getText()));
    }

    /** calls the method initialize
     *
     * @param actionEvent Button press refresh
     */
    @FXML
    private void refresh(ActionEvent actionEvent){
        initialize();
    }

    /** close viewer
     *
     * @param actionEvent when press exit.
     */
    @FXML
    private void Exit(ActionEvent actionEvent) {
        Platform.exit();

    }

    /** opening a CSV file of products.
     *
     * @param actionEvent press openProductsCSV
     * @throws ClassNotFoundException for refering to createProduct method
     * @throws SQLException because it connects with the database
     */
    @FXML
    private void openProductCSV(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(pane.getScene().getWindow());

        if (file != null && file.isFile()) {
            try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
                String line = reader.readLine();
                int lineCounter = 0;
                while (line != null) {
                    if (lineCounter == 0) {
                        line = reader.readLine();
                    } else {
                        String[] productLine = line.split(";");
                        List<String> productLineList = new LinkedList<>(Arrays.asList(productLine));
                        if (productLineList.size() >6) { // maak hier pop-up van! + zet default value van switch op dit ipv de if else
                            System.out.println("Error in the CSV file!");
                            throwErrorStatic(actionEvent, "Error in the CSV file!");
                            break;
                        }
                        switch (productLineList.size()) {
                            case 6:
                                Product product = createProduct(productLineList);
                                model.ProductDAO.addProduct(product);
                                line = reader.readLine();
                                break;
                            case 5:
                                productLineList.add("");
                                Product product1 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product1);
                                line = reader.readLine();
                                break;
                            case 4:
                                productLineList.add("");
                                productLineList.add("");
                                Product product2 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product2);
                                line = reader.readLine();
                                break;
                            case 3:
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                Product product3 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product3);
                                line = reader.readLine();
                                break;
                            case 2:
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                Product product4 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product4);
                                line = reader.readLine();
                                break;
                            case 1:
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                Product product5 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product5);
                                line = reader.readLine();
                                break;
                        }
                    }
                    lineCounter++;
                }
                initialize();



            } catch (IOException e) {
                System.out.println("Something went wrong when reading the file");
                e.printStackTrace();
                throwErrorStatic(actionEvent, "Something went wrong when reading the file");
            }
        }
    }

    /** create a product
     *
     * @param productLineList give a list of products
     * @return a new products
     * @throws SQLException because of the connection to the database
     * @throws ClassNotFoundException because of the checkForCategory method
     * @throws IOException because of the checkForCategory method
     */
    private static Product createProduct(List<String> productLineList) throws SQLException, ClassNotFoundException, IOException {
        String mainCategory;
        String subCategory;
        String productTitle = productLineList.get(0);
        if (DBUtil.checkForCategory("SELECT COUNT(*) AS total FROM category WHERE subCategory = '" + productLineList.get(1) + "'")) {
            subCategory = productLineList.get(1);
        } else {
            CategoryDAO.addCategory(productLineList.get(2), productLineList.get(1));
            subCategory = productLineList.get(1);
        }
        mainCategory = productLineList.get(2);
        String price = productLineList.get(3);
        String productDescription = productLineList.get(4);
        String imagePath = productLineList.get(5);

        return new Product.Builder()
                .withTitle(productTitle)
                .withSubCat(subCategory)
                .withMainCat(mainCategory)
                .withPrice(price)
                .withDescription(productDescription)
                .withImageView(null)
                .withId(0)
                .withPath(imagePath)
                .build();
    }

    /** opening a CSV file of Category.
     *
     * @param actionEvent when press openCaegoryCSV
     * @throws SQLException because of the connection to the database
     */
    @FXML
    public void OpenCategoryCSV(ActionEvent actionEvent) throws SQLException {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(pane.getScene().getWindow());

        if (file != null && file.isFile()) {
            try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
                String line = reader.readLine();
                int lineCounter = 0;
                while (line != null) {
                    if (lineCounter == 0) {
                        line = reader.readLine();
                    } else {
                        String[] productLine = line.split(";");
                        List<String> productLineList = new LinkedList<>(Arrays.asList(productLine));
                        if (productLineList.size() > 2) { // maak hier pop-up van! + zet default value van switch op dit ipv de if else
                            System.out.println("Error in the CSV file!");
                            throwErrorStatic("Error in the CSV file!");
                            break;
                        }
                        switch (productLineList.size()) {
                            case 2:
                                Category category = createCategory(productLineList);
                                if (category != null)
                                    model.CategoryDAO.addCategory(category);
                                line = reader.readLine();
                                break;
                            case 1:
                                line = reader.readLine();
                                break;
                        }
                    }
                    lineCounter++;
                }
                initialize();
//                throwPositiveStatic("Great success");
            } catch (IOException e) {
                System.out.println("Something went wrong when reading the file");
                e.printStackTrace();
                throwErrorStatic(actionEvent, "Something went wrong when reading the file");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
              //  throwErrorStatic(actionEvent, "Something went wrong when reading the file");
            }
        }
    }

    /**
     *
     * this method creates a category if necessary
     * @param categoryLineList a list of categories
     * @return a new sub and maincategory if needed, otherwise null
     * @throws SQLException because of the connection to the database
     * @throws IOException because of the checkForCategory method
     * @throws ClassNotFoundException because of the checkForCategory method
     */
    private Category createCategory(List<String> categoryLineList) throws SQLException, IOException, ClassNotFoundException {
        String mainCategory;
        String subCategory;
        if (!DBUtil.checkForCategory("SELECT COUNT(*) AS total FROM category WHERE subCategory = '" + categoryLineList.get(0) + "'")) {
            subCategory = categoryLineList.get(0);
            mainCategory = categoryLineList.get(1);
            return new Category(subCategory, mainCategory);
        } else {
            return null;
        }
    }

    /** Saving the list op products in to CSV file.
     *
     * @param actionEvent press saveProductCSV
     */
    @FXML
    public void saveProductCSV(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (file != null) {
            DBUtil.saveProductCSV(file, "select * from products");
          //  throwPositiveStatic("Great success");
        }
    }

    /** Save the list of category to a CSV file.
     *
     * @param actionEvent press saveCategoryCSV
     */
    @FXML
    public void saveCategoryCSV(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (file != null) {
            DBUtil.saveCategoryCSV(file, "select * from category");
//         throwPositiveStatic("Great success");

        }
    }

    /** loads PopupAddProduct.fxml
     *
     * @param actionEvent when press add product
     */
    @FXML
    private void openAddProductPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupAddProduct.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add product");
            stage.setScene(new Scene(root1, 510, 250));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when opening the add product pop-up");
            e.printStackTrace();
            throwErrorStatic(actionEvent, "Something went wrong when opening the add product pop-up");
        }
    }

    /** loads PopupAddCategory.fxml
     *
     * @param actionEvent press add category
     */
    @FXML
    private void openAddCategoryPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupAddCategory.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add category");
            stage.setScene(new Scene(root1, 400, 80));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when openening the add category popup");
            e.printStackTrace();
            throwErrorStatic(actionEvent, "Something went wrong when openening the add category popup");
        }
    }

    /** loads PopupHelp.fxml
     *
     * @param actionEvent press Help
     */
    public void openHelpPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupHelp.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1, 330, 255));
            stage.setTitle("Help");
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when opening the help popup");
            e.printStackTrace();
            throwErrorStatic(actionEvent, "Something went wrong when openening the help popup");
        }
    }

    /** Loads PopupError.fxml
     *
     * @param actionEvent When a exception is throw
     */
    @FXML
    private void openPopupError(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupError.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("ERROR!!!");
            stage.setScene(new Scene(root1, 400, 80));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when opening the Error pop-up");
            e.printStackTrace();
        }
    }


    /**method for opening an error popup
     *
     */
    @FXML
    private void openPopupError() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupError.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("ERROR!!!");
            stage.setScene(new Scene(root1, 300, 60));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when opening the Error pop-up");
            e.printStackTrace();

        }
    }

    /**
     *
     * @param actionEvent possible event we need to open the popup
     * @param errorMessage message the popup needs to show
     */
    public void throwError(ActionEvent actionEvent, String errorMessage) {

        PopupMessageClass.setErrormessage(errorMessage);
        openPopupError(actionEvent);
    }

    /**
     *
     * @param errorMessage message the popup needs to show
     */
    public void throwError(String errorMessage) {

        PopupMessageClass.setErrormessage(errorMessage);
        openPopupError();
    }

    /**this is the static method we can use in the entire project with an event and a message
     *
     * @param actionEvent possible event we need to open the popup
     * @param errorMessage message the popup needs to show
     */
    public static void throwErrorStatic(ActionEvent actionEvent, String errorMessage) {
        MainController mainController = new MainController();
        mainController.throwError(actionEvent, errorMessage);
    }

    /**this is the static method we can use in the entire project with only a message
     *
     * @param errorMessage message the popup needs to show
     */
    public static void throwErrorStatic(String errorMessage) {
        MainController mainController = new MainController();
        mainController.throwError(errorMessage);
    }


    /** for deleting a row in the viewer and database.
     *
     * @param actionEvent press button delete row
     */
    public void deleteRow(ActionEvent actionEvent) {
        Product selectedItem = productTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int productId = selectedItem.getProductId();
            String query = "delete from products WHERE productId = " + productId;
            DBUtil.executeQuery(query);
            productTable.getItems().remove(selectedItem);
        }
    }
}
