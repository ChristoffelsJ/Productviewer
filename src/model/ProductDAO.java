package model;

import main.MainController;
import util.DBUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static main.MainController.throwErrorStatic;
import static main.MainController.throwPositiveStatic;

public class ProductDAO {

    public static void addProduct(String productTitle, String subCategory, String mainCategory, String price, String description, Path imagePath, int productId) throws SQLException, ClassNotFoundException, IOException {
        String stringPath = imagePath.toString().replace("\\","/");
        String update = "INSERT INTO products (productTitle, subCategory, mainCategory, price, productDescription, image, imagePath)" +
                " VALUES ('" + productTitle + "','" + subCategory + "', '" + mainCategory + "','" + price + "','" + description + "',?, '" + stringPath + "')";
        try(InputStream inputStream = Files.newInputStream(imagePath);
            Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(update)){
            pstmt.setBinaryStream(1, inputStream);
            pstmt.executeUpdate();
            throwPositiveStatic("Great success");


        } catch (SQLException ex) {
            System.out.println("Error when implementing data in database");
            throwErrorStatic("Error when implementing data in database");
            throw ex;


        }

    }

    public static void addProduct(Product product) throws SQLException {

        String update = "INSERT INTO products (productTitle, subCategory, mainCategory, price, productDescription, image, imagePath) " +
                "VALUES ('" + product.getTitle() + "','" + product.getSubCategory() + "','" + product.getMainCategory() + "','" + product.getPrice() + "','" + product.getDescription() + "',?, '" + product.getImagePath() + "')";
        if(!product.getImagePath().equals("null") || product.getImagePath() != null) {
            try (InputStream inputStream = Files.newInputStream(Paths.get(product.getImagePath()));
                 Connection con = DBUtil.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(update)) {
                pstmt.setBinaryStream(1, inputStream);
                pstmt.executeUpdate();
                //throwPositiveStatic("Great success");


            } catch (SQLException ex) {
                System.out.println("Error when implementing data in database");
                throwErrorStatic("Error when implementing data in database");

                throw ex;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else{
            try (InputStream inputStream = Files.newInputStream(Paths.get("standardImage.jpg"));
                 Connection con = DBUtil.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(update)) {
                pstmt.setBinaryStream(1, inputStream);
                pstmt.executeUpdate();
                throwPositiveStatic("Great success");


            } catch (SQLException ex) {
                System.out.println("Error when implementing data in database");
                throwErrorStatic("Error when implementing data in database");

                throw ex;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void changeProducts(String productTitle, String category, String price, String description, int productID){

        String update = "UPDATE products SET productTitle = '" + productTitle + "', price= '" + price + "',productDescription='" + description + "', subCategory=" + category + " where productId=" + productID;
        DBUtil.updateQuery(update);
    }

    public static List<Product> getInitialProducts(){
        String query = "select *  from products";
        return DBUtil.fillListWithProducts(query);
    }

    public static List<Product> search(String search){
        String query = "SELECT * FROM products WHERE productTitle LIKE '" + "%" + search + "%" + "' " +
                "OR subCategory LIKE '" + "%" + search + "%" + "'" +
                "OR mainCategory LIKE '" + "%" + search + "%" + "'" +
                "OR productID LIKE '" + "%" + search + "%" + "'";;
        return DBUtil.fillListWithProducts(query);
    }

    public static List<Product> getProduct(){
        String query = "select * FROM products";
        return DBUtil.fillListWithProducts(query);
    }


}
