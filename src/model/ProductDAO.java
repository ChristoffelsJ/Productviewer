package model;


import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class ProductDAO {


    public static void addProduct(String productTitle, String category, String price, String description, ImageView file, int productId) throws SQLException, ClassNotFoundException {

        String update = "INSERT INTO products (productTitle, subCategory, price, productDescription, image) VALUES ('" + productTitle + "','" + category + "','" + price + "','" + description + "','" + file + "')";
        try {
            util.DBUtil.updateQuery(update);

        } catch (SQLException ex) {
            System.out.println("Error when implementing data in database");
            throw ex;
        }

    }
/*
    public static int getProductId(Product product){
        String getIdQuerry= "select productId from products as p where p.productTitle = '"+product.getTitle()+
                "' and p.subCategory='"+product.getCategory()+"' and p.price='"+product.getPrice()+
                "' and p.productDescription='"+product.getDescription()+"')";
        try {
            Optional firstInput= util.DBUtil.fillListWithProducts(getIdQuerry).stream().findFirst();
            return firstInput;


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error in getProductId");
            e.printStackTrace();
        }
    }
*/

    public static void addProduct(Product product) throws SQLException, ClassNotFoundException {

        String update = "INSERT INTO products (productTitle, subCategory, price, productDescription, image) VALUES ('" + product.getTitle() + "','" + product.getCategory() + "','" + product.getPrice() + "','" + product.getDescription() + "','" + null + "' )";
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
