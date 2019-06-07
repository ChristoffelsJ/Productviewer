package util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainController;
import model.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.*;
import java.util.*;

import static main.MainController.throwErrorStatic;
import static main.MainController.throwPositiveStatic;


public class DBUtil {
    private static String url = "jdbc:mysql://localhost/productViewer?serverTimezone=UTC";
    private static String userName = "root";
    private static String password = "blablabla";

    //connectie methode
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

    //voert een query uit die meegegeven wordt
    public static void executeQuery(String query){
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println("Error when executing the querry");
            throwErrorStatic("Error when executing the querry");
            ex.printStackTrace();

        }
    }

    private static int executeCountQuery(String query) throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error when executing the count querry");
            throwErrorStatic("Error when executing the querry");
            throw ex;
        }
    }

    //voert een update uit die meegegeven wordt
    public static void updateQuery(String query){
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error when updating the query");
            throwErrorStatic("Error when updating the query");
            ex.printStackTrace();
        }
    }

    public static List<Product> fillListWithProducts(String query) {
        List<Product> productlist = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {

                ImageView imageView = new ImageView();
                try (InputStream is = resultSet.getBinaryStream("image"); OutputStream os = new FileOutputStream(new File("photo.jpg"));) {

                    byte[] contents = new byte[1024];
                    int size;
                    while ((size = is.read(contents)) != -1) {
                        os.write(contents, 0, size);
                    }
                    Image image = new Image("file:photo.jpg", 100, 80, true, true);
                    imageView.setImage(image);

                    Product product = new Product.Builder()
                            .withTitle(resultSet.getString("productTitle"))
                            .withSubCat(resultSet.getString("subCategory"))
                            .withMainCat(resultSet.getString("mainCategory"))
                            .withPrice(resultSet.getString("price"))
                            .withDescription(resultSet.getString("productDescription"))
                            .withImageView(imageView)
                            .withId(resultSet.getInt("productId"))
                            .withPath(resultSet.getString("imagePath"))
                            .build();

                    productlist.add(product);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error while filling the productsList");
            throwErrorStatic("Error while filling the productsList");
        }
        return productlist;
    }

    // main category uit database halen en in een List zetten
    public static List<String> fillListWithMainCategory(String query){
        List<String> mainCategoryList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String category = resultSet.getString("mainCategory");
                mainCategoryList.add(category);
            }
        } catch (SQLException ex) {
            System.out.println("Error while filling the mainCategory List");
            throwErrorStatic("Error while filling the mainCategory List");
        }
        return mainCategoryList;
    }

    // sub category uit database halen en in een List zetten
    public static List<String> fillListWithSubCategory(String query) {
        List<String> subCategoryList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String category = resultSet.getString("subCategory");
                subCategoryList.add(category);
            }
        } catch (SQLException ex) {
            System.out.println("Error while filling the subCategory List");
            throwErrorStatic("Error while filling the subCategory List");

        }
        return subCategoryList;
    }

    public static void saveProductCSV(File file, String query){
        StringBuilder sb = new StringBuilder();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            sb.append("productTitle");
            sb.append(";");
            sb.append("subCategory");
            sb.append(";");
            sb.append("mainCategory");
            sb.append(";");
            sb.append("price");
            sb.append(";");
            sb.append("productDescription");
            sb.append(";");
            sb.append("imagePath");
            sb.append("\r\n");
            while (resultSet.next()) {
                sb.append(resultSet.getString("productTitle"));
                sb.append(";");
                sb.append(resultSet.getString("subCategory"));
                sb.append(";");
                sb.append(resultSet.getString("mainCategory"));
                sb.append(";");
                sb.append(resultSet.getString("price"));
                sb.append(";");
                sb.append(resultSet.getString("productDescription"));
                sb.append(";");
                sb.append(resultSet.getString("imagePath"));
                sb.append("\r\n");
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(sb.toString());
            fileWriter.close();
            System.out.println("CSV created");
            throwPositiveStatic("Great success");


        } catch (
                Exception e) {
            System.out.println("Error when saving the database to CSV file");
            throwErrorStatic("Error when saving the database to CSV file");

            e.printStackTrace();
        }

    }

    public static void saveCategoryCSV(File file,String query){
        StringBuilder sb = new StringBuilder();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            sb.append("subCategory");
            sb.append(";");
            sb.append("mainCategory");
            sb.append(";");
            sb.append("\r\n");
            while (resultSet.next()) {
                sb.append(resultSet.getString("subCategory"));
                sb.append(";");
                sb.append(resultSet.getString("mainCategory"));
                sb.append(";");
                sb.append("\r\n");
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(sb.toString());
            fileWriter.close();
            System.out.println("CSV created");
            throwPositiveStatic("Great success");

        } catch (Exception e) {
            System.out.println("error when saving the database to CSV file");
            throwErrorStatic("error when saving the database to CSV file");
            e.printStackTrace();
        }
    }

    public static boolean checkForCategory(String query) throws SQLException, ClassNotFoundException {
        return executeCountQuery(query) > 0;
    }

}