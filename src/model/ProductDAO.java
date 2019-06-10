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
/** Data Access Object for product
 *
 */
public class ProductDAO {
    /** query for adding a product to database
     *
     * @param productTitle title
     * @param subCategory sub category
     * @param mainCategory main category
     * @param price price
     * @param description description
     * @param imagePath image path
     * @param productId id
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void addProduct(String productTitle, String subCategory, String mainCategory, String price, String description, Path imagePath, int productId) throws SQLException, ClassNotFoundException, IOException {
        String stringPath = imagePath.toString().replace("\\","/");
        String update = "INSERT INTO products (productTitle, subCategory, mainCategory, price, productDescription, image, imagePath)" +
                " VALUES ('" + productTitle + "','" + subCategory + "', '" + mainCategory + "','" + price + "','" + description + "',?, '" + stringPath + "')";
        try(InputStream inputStream = Files.newInputStream(imagePath);
            Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(update)){
            pstmt.setBinaryStream(1, inputStream);
            pstmt.executeUpdate();
//            throwPositiveStatic("Great success");


        } catch (SQLException ex) {
            System.out.println("Error when implementing data in database");
            throwErrorStatic("Error when implementing data in database");
            throw ex;


        }

    }

    /** query for adding a product
     *
     * @param product product
     * @throws SQLException
     */
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

    /** query for changing a product
     *
     * @param productTitle title
     * @param category category
     * @param price  price
     * @param description description
     * @param productID id
     */
    public static void changeProducts(String productTitle, String category, String price, String description, int productID){

        String update = "UPDATE products SET productTitle = '" + productTitle + "', price= '" + price + "',productDescription='" + description + "', subCategory=" + category + " where productId=" + productID;
        DBUtil.updateQuery(update);
    }

    /** query for getting a list of all products
     *
     * @return list of products
     */
    public static List<Product> getInitialProducts(){
        String query = "select *  from products";
        return DBUtil.fillListWithProducts(query);
    }

    /** query for getting a list of products with search
     *
     * @param search search word
     * @return list of products
     */
    public static List<Product> search(String search){
        String query = "SELECT * FROM products WHERE productTitle LIKE '" + "%" + search + "%" + "' " +
                "OR subCategory LIKE '" + "%" + search + "%" + "'" +
                "OR mainCategory LIKE '" + "%" + search + "%" + "'" +
                "OR productID LIKE '" + "%" + search + "%" + "'";;
        return DBUtil.fillListWithProducts(query);
    }

    /** query for getting a list of products
     *
     * @return list of product
     */
    public static List<Product> getProduct(){
        String query = "select * FROM products";
        return DBUtil.fillListWithProducts(query);
    }


}
