package util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Product;

import java.io.*;
import java.sql.*;
import java.util.*;

import static main.MainController.throwErrorStatic;


/** database utility class
 *
 */
public class DBUtil {
    /** connection method
     *
     * @return a connection
     * @throws SQLException
     * @throws IOException
     */
    public static Connection getConnection() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("MYSQLconnection.properties"));
        String userName = properties.getProperty("userName");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        return DriverManager.getConnection(url, userName, password);
    }

    /** execute a query
     *
     * @param query query
     */
    //voert een query uit die meegegeven wordt
    public static void executeQuery(String query){
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println("Error when executing the querry");
            throwErrorStatic("Error when executing the querry");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param query query
     * @return int
     * @throws SQLException
     * @throws IOException
     */
    private static int executeCountQuery(String query) throws SQLException, IOException {
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
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /** execute a query for update database
     *
     * @param query query
     */
    //voert een update uit die meegegeven wordt
    public static void updateQuery(String query){
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error when updating the query");
            throwErrorStatic("Error when updating the query");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** get a list back of products
     *
     * @param query query
     * @return list of products
     */
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
            ex.printStackTrace();
        }
        return productlist;
    }

    /** get a list off main category from database
     *
     * @param query query
     * @return lsit of main category
     */
    // main category uit database halen en in een List zetten
    public static List<String> fillListWithMainCategory(String query){
        List<String> mainCategoryList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String category = resultSet.getString("mainCategory");
                mainCategoryList.add(category);
            }
        } catch (SQLException | IOException ex) {
            System.out.println("Error while filling the mainCategory List");
            throwErrorStatic("Error while filling the mainCategory List");
        }
        return mainCategoryList;
    }

    /** get a list off sub category from database
     *
     * @param query query
     * @return list of sub category
     */
    // sub category uit database halen en in een List zetten
    public static List<String> fillListWithSubCategory(String query) {
        List<String> subCategoryList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String category = resultSet.getString("subCategory");
                subCategoryList.add(category);
            }
        } catch (SQLException | IOException ex) {
            System.out.println("Error while filling the subCategory List");
            throwErrorStatic("Error while filling the subCategory List");

        }
        return subCategoryList;
    }

    /** save product to CSV from database
     *
     * @param file file
     * @param query query
     */
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


        } catch (
                Exception e) {
            System.out.println("Error when saving the database to CSV file");
            throwErrorStatic("Error when saving the database to CSV file");

            e.printStackTrace();
        }

    }

    /** save category to CSV from database
     *
     * @param file
     * @param query
     */
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


        } catch (Exception e) {
            System.out.println("error when saving the database to CSV file");
            throwErrorStatic("error when saving the database to CSV file");
            e.printStackTrace();
        }
    }

    /** Check if there is category
     *
     * @param query query
     * @return boolean
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static boolean checkForCategory(String query) throws SQLException, ClassNotFoundException, IOException {
        return executeCountQuery(query) > 0;
    }

}