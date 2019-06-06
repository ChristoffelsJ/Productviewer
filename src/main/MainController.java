package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Product;
import model.ProductDAO;
import util.DBUtil;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainController {
    @FXML
    private TableColumn<Product, String> columnMainCategory;
    @FXML
    private MenuItem saveCategoryCSV;
    @FXML
    private MenuItem saveProductCSV;
    @FXML
    private MenuItem openCategoryCSV;
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
    private TableColumn<Product, Integer> columnProductId;

    @FXML
    private TextField search;
    @FXML
    public MenuItem openProductCSV;
    @FXML
    private FileChooser fileChooser;
    @FXML
    private Pane pane;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        columnProductTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnSubCategory.setCellValueFactory(new PropertyValueFactory<>("subCategory"));
        columnMainCategory.setCellValueFactory(new PropertyValueFactory<>("mainCategory"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPicture.setCellValueFactory(new PropertyValueFactory<>("image"));
        productTable.getItems().setAll(generateInitialProducts());


        editableColumn();
        loadDate();
        }

    private void loadDate() throws SQLException, ClassNotFoundException {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        productObservableList.addAll(ProductDAO.getProduct());
        productTable.setItems(productObservableList);
    }

    //tableview editable maken en er voor zorgen dat deze zijn gegevens opslaat in de database
    private void editableColumn() {
        //deze werkt, afblijven
        columnProductTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        columnProductTitle.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setTitle(e.getNewValue()));
        columnProductTitle.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setTitle(event.getNewValue());
            updateData("productTitle", event.getNewValue(), product.getProductId());
        });
        //met de category moeten we andere dingen gaan doen, moet gelinkt zijn aan de category db
        columnSubCategory.setCellFactory(TextFieldTableCell.forTableColumn());
        columnSubCategory.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setSubCategory((e.getNewValue())));
        columnSubCategory.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setSubCategory((event.getNewValue()));
            updateData("subCategory", event.getNewValue(), product.getProductId());
        });
        //met de category moeten we andere dingen gaan doen, moet gelinkt zijn aan de category db
        columnMainCategory.setCellFactory(TextFieldTableCell.forTableColumn());
        columnMainCategory.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setSubCategory((e.getNewValue())));
        columnMainCategory.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setMainCategory((event.getNewValue()));
            updateData("mainCategory", event.getNewValue(), product.getProductId());
        });
        //deze werkt, afblijven
        columnPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        columnPrice.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setPrice(e.getNewValue()));
        columnPrice.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setPrice(event.getNewValue());
            updateData("price", event.getNewValue(), product.getProductId());
        });
        //deze werkt, afblijven
        columnProductDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        columnProductDescription.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setDescription(e.getNewValue()));
        columnProductDescription.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setDescription(event.getNewValue());
            updateData("productDescription", event.getNewValue(), product.getProductId());
        });
        productTable.setEditable(true);
    }


    private void updateData(String column, String newValue, int id) {
        String query = "UPDATE products SET " + column + " = '" + newValue +  "' WHERE productId = " + id +"";
        try {
            DBUtil.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error in updating the data in database");
            e.printStackTrace(System.err);
        }
    }

    private List<Product> generateInitialProducts() throws SQLException, ClassNotFoundException {
        return model.ProductDAO.getInitialProducts();
    }

    @FXML
    private void search(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        productTable.getItems().setAll(model.ProductDAO.search(search.getText()));
    }

    @FXML
    private void refresh(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        initialize();
    }

    @FXML
    private void Exit(ActionEvent actionEvent) {
        Platform.exit();

    }

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
                        if (productLineList.size() > 5) { // maak hier pop-up van! + zet default value van switch op dit ipv de if else
                            System.out.println("Error in the CSV file!");
                            break;
                        }
                        switch (productLineList.size()) {
                            case 5:
                                Product product = createProduct(productLineList);
                                model.ProductDAO.addProduct(product);
                                line = reader.readLine();
                                break;
                            case 4:
                                productLineList.add("");
                                Product product1 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product1);
                                line = reader.readLine();
                                break;
                            case 3:
                                productLineList.add("");
                                productLineList.add("");
                                Product product2 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product2);
                                line = reader.readLine();
                                break;
                            case 2:
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                Product product3 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product3);
                                line = reader.readLine();
                                break;
                            case 1:
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                Product product4 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product4);
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
            }
        }

        //pop up met error maken
        else {
            System.out.println("you have to load a file with the CSV extension");
        }
    }

    private static Product createProduct(List<String> productLineList) throws SQLException, ClassNotFoundException {
        String mainCategory = "";
        String subCategory = "";
        String productTitle = productLineList.get(0);
        if(DBUtil.checkForCategory("SELECT COUNT(*) AS total FROM category WHERE mainCategory = '"  + productLineList.get(1) + "'")) {
            mainCategory = productLineList.get(1);
        }
        else{
            //jonas gooi hier uw fout pop-up
            System.out.println("error because mainCategory is not yet defined");
        }
        if(DBUtil.checkForCategory("SELECT COUNT(*) As total FROM category WHERE subCategory = '"  + productLineList.get(2) + "'")) {
            subCategory = productLineList.get(2);
        }
        else{
            //jonas gooi hier uw fout pop-up
            System.out.println("error because subCategory is not yet defined");
        }
        String price = productLineList.get(3);
        String productDescription = productLineList.get(4);
        return new Product(productTitle, subCategory, mainCategory, price, productDescription,null,0);

    }

    @FXML
    public void OpenCategoryCSV(ActionEvent actionEvent) throws SQLException {
    }


    @FXML
    public void saveProductCSV(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (file != null) {
            DBUtil.saveProductCSV(file);
        }
    }

    @FXML
    public void saveCategoryCSV(ActionEvent actionEvent) throws SQLException {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (file != null) {
            try {
                DBUtil.saveCategoryCSV(file);
            } catch (ClassNotFoundException e) {
                System.out.println("Something went wrong with saving the file");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void openAddProductPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupAddProduct.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add product");
            stage.setScene(new Scene(root1, 520, 120));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when opening the add product pop-up");
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddCategoryPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupAddCategory.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add category");
            stage.setScene(new Scene(root1, 300, 80));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when openening the add category popup");
            e.printStackTrace();
        }
    }


    public void openHelpPopup(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopupHelp.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1, 600, 400));
            stage.setTitle("Help");
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong when openening the help popup");
            e.printStackTrace();
        }
    }
//werkt
    public void deleteRow(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Product selectedItem = productTable.getSelectionModel().getSelectedItem();
        int productId = selectedItem.getProductId();
        String query = "delete from products WHERE productId = " + productId;
        try{
        DBUtil.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println("Error in deleting product from DB");
            ex.printStackTrace(System.err);
        }
        productTable.getItems().remove(selectedItem);
    }
}
