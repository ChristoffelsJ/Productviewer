package model;


import javafx.scene.image.ImageView;
import util.DBUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class ProductDAO {

    public static void addProduct(String productTitle, int subCategory, int mainCategory, String price, String description) throws SQLException, ClassNotFoundException {
        String update = "INSERT INTO products VALUES ('" + productTitle + "','" + subCategory + "', '" + mainCategory + "','" + price + "','" + description + "')";
        try {
            util.DBUtil.updateQuery(update);
        } catch (SQLException ex) {
            System.out.println("Error when implementing data in database");
            throw ex;
        }
    }

    public static void addProduct(String productTitle, String subCategory, String mainCategory, String price, String description, Path imagePath, int productId) throws SQLException, ClassNotFoundException, IOException {
        String update = "INSERT INTO products (productTitle, subCategory, mainCategory, price, productDescription, image) VALUES ('" + productTitle + "','" + subCategory + "', '" + mainCategory + "','" + price + "','" + description + "',?)";


        try(InputStream inputStream = Files.newInputStream(imagePath);
            Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(update)){
            pstmt.setBinaryStream(1, inputStream);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error when implementing data in database");
            throw ex;
        }

    }

    public static void addProduct(Product product) throws SQLException, ClassNotFoundException {

        String update = "INSERT INTO products (productTitle, subCategory, mainCategory, price, productDescription, image) " +
                "VALUES ('" + product.getTitle() + "','" + product.getSubCategory() + "','" + product.getMainCategory() + "','" + product.getPrice() + "','" + product.getDescription() + "','" + null + "' )";
        try {
            util.DBUtil.updateQuery(update);
        } catch (SQLException ex) {
            System.out.println("Error when implementing data in database");
            throw ex;
        }
    }

    public static void changeProducts(String productTitle, String category, String price, String description, int productID) throws SQLException, ClassNotFoundException {

        String update = "UPDATE products SET productTitle = '" + productTitle + "', price= '" + price + "',productDescription='" + description + "', subCategory=" + category + " where productId=" + productID;
        try {
            util.DBUtil.updateQuery(update);
        } catch (SQLException ex) {
            System.out.println("Error when changing data in database");
            throw ex;
        }
    }

    public static List<Product> getInitialProducts() throws SQLException, ClassNotFoundException {
        String query = "select *  from products";
        try {
            return util.DBUtil.fillListWithProducts(query);
        } catch (SQLException ex) {
            System.out.println("Error while getting initial products");
            throw ex;
        }
    }

    public static List<Product> search(String search) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM products WHERE productTitle LIKE '" + "%" + search + "%" + "' " +
                "OR subCategory LIKE '" + "%" + search + "%" + "'";
        try {
            return util.DBUtil.fillListWithProducts(query);
        } catch (SQLException ex) {
            System.out.println("Error when searching data in database");
            throw ex;
        }
    }

    public static List<Product> getProduct() throws SQLException, ClassNotFoundException {
        String query = "select * FROM products";
        try {
            return util.DBUtil.fillListWithProducts(query);
        } catch (SQLException ex) {
            System.out.println("Error while getting products");
            throw ex;
        }
    }

    public static void addCategory(String category) throws SQLException, ClassNotFoundException {

        String update = "INSERT INTO category VALUES  (0,'" + category + "')";
        try {
            util.DBUtil.updateQuery(update);
        } catch (SQLException ex) {
            System.out.println("Error when implementing categories in database");
            throw ex;
        }
    }
}
