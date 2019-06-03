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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Product;
import model.ProductDAO;
import util.DBUtil;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainController {

    public MenuItem saveCSV;
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
    private TableColumn<Product, ImageView> columnImageView;
    @FXML
    private TableColumn<Product, Integer> columnProductId;

    @FXML
    private TextField search;
    @FXML
    public MenuItem openCSV;
    @FXML
    private FileChooser fileChooser;
    @FXML
    private Pane pane;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        columnProductTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
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
        columnCategory.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCategory.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setCategory(e.getNewValue()));
        columnCategory.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setCategory(event.getNewValue());
            updateData("subCategory", event.getNewValue(), product.getProductId());
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
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/productViewer?serverTimezone=UTC", "root", "blablabla");
        ) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE products SET " + column + " = ? WHERE productId =? " );
            stmt.setString(1, newValue);
            stmt.setInt(2, id);
            stmt.execute();
        } catch (SQLException ex) {
            System.err.println("Error filling in database from tableview");

            ex.printStackTrace(System.err);
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
    private void Exit(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Platform.exit();

    }

    @FXML
    private void openCSV(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
        if (file != null && file.isFile() && file.getName().endsWith(".csv")) {
            try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
                String line = reader.readLine();
                int lineCounter = 0;
                while (line != null) {
                    if (lineCounter == 0) {
                        line = reader.readLine();
                    } else {
                        String[] productLine = line.split(";");
                        List<String> productLineList = new LinkedList<String>(Arrays.asList(productLine));
                        if (productLineList.size() > 4) { // maak hier pop-up van!
                            System.out.println("Error in the CSV file!");
                            break;
                        }
                        switch (productLineList.size()) {
                            case 4:
                                Product product = createProduct(productLineList);
                                model.ProductDAO.addProduct(product);
                                line = reader.readLine();
                                break;
                            case 3:
                                productLineList.add("");
                                Product product1 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product1);
                                line = reader.readLine();
                                break;
                            case 2:
                                productLineList.add("");
                                productLineList.add("");
                                Product product2 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product2);
                                line = reader.readLine();
                                break;
                            case 1:
                                productLineList.add("");
                                productLineList.add("");
                                productLineList.add("");
                                Product product3 = createProduct(productLineList);
                                model.ProductDAO.addProduct(product3);
                                line = reader.readLine();
                                break;
                        }
                    }
                    lineCounter++;
                }
                initialize();
            } catch (IOException e) {
                System.out.println("Something went wrong when reading the file");
            }
        }

        //pop up met error maken
        else {
            System.out.println("you have to load a file with the CSV extension");
        }
    }

    private static Product createProduct(List<String> productLineList) {
        String productTitle = productLineList.get(0);
        String category = productLineList.get(1);
        String price = productLineList.get(2);
        String productDescription = productLineList.get(3);
        //hier de 0 nog vervangen door opgehaalde data uit database
        return new Product(productTitle, category, price, productDescription, null, 0);

    }

    @FXML
    public void saveCSV(ActionEvent actionEvent) throws IOException, SQLException {
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (file != null) {
            try {
                DBUtil.saveCSV(file);
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
        } catch (Exception E) {
            System.out.println("Something went wrong when opening the add product pop-up");
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
        } catch (Exception E) {
            System.out.println("Something went wrong");
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
        } catch (Exception E) {
            System.out.println("Something went wrong");
        }
    }

    public void deleteRow(ActionEvent actionEvent) {

        Product selectedItem = productTable.getSelectionModel().getSelectedItem();
        int productId = selectedItem.getProductId();
               try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/productViewer?serverTimezone=UTC", "root", "blablabla");
        ) {
            PreparedStatement stmt = connection.prepareStatement("delete from products WHERE productId = ? ");
           stmt.setInt(1, productId);
            stmt.execute();
        } catch (SQLException ex) {
            System.err.println("Error filling in database from tableview");

            ex.printStackTrace(System.err);
        }
        productTable.getItems().remove(selectedItem);
    }
}
