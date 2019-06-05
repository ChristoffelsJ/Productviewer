package util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Product;
import model.ProductDAO;

import java.io.*;
import java.sql.*;
import java.util.*;


public class DBUtil {
    private static String url = "jdbc:mysql://localhost/productViewer?serverTimezone=UTC";
    private static String userName = "root";
    private static String password = "MySQLJava2019!";
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static Connection connection = null;

    //connectie methode
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

    //voert een query uit die meegegeven wordt
    public static void executeQuery(String query) throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println("Error when executing the querry");
            throw ex;
        }
    }

    //voert een update uit die meegegeven wordt
    public static void updateQuery(String query) throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error");
            throw ex;
        }
    }

    public static List<Product> fillListWithProducts(String query) throws SQLException, ClassNotFoundException {
        List<Product> productlist = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {


                InputStream is= resultSet.getBinaryStream("image");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] contents = new byte[1024];
                int size;
                while ((size = is.read(contents)) != -1){
                    os.write(contents,0,size);
                }
                Image image = new Image("file:photo.jpg",100,80,true,true);
                ImageView imageView = new ImageView();
                imageView.setImage(image);


                Product product = new Product(resultSet.getString("productTitle"), resultSet.getString("subCategory"), resultSet.getString("mainCategory")
                        ,resultSet.getString("price"), resultSet.getString("productDescription"), imageView, resultSet.getInt("productId"));

                /* product.setProductId(ProductDAO.getProductId(product));*/
                productlist.add(product);
            }
        } catch (SQLException ex) {
            System.out.println("Error while filling the productsList");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productlist;
    }

    // main category uit database halen en in een List zetten
    public static List<String> fillListWithMainCategory(String query) throws SQLException, ClassNotFoundException {
        List<String> mainCategoryList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String category = resultSet.getString("mainCategory");
                mainCategoryList.add(category);
            }
        } catch (SQLException ex) {
            System.out.println("Error while filling the mainCategory List");
        }
        return mainCategoryList;
    }
    // sub category uit database halen en in een List zetten
    public static List<String> fillListWithSubCategory(String query) throws SQLException, ClassNotFoundException {
        List<String> subCategoryList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String category = resultSet.getString("subCategory");
                subCategoryList.add(category);
            }
        } catch (SQLException ex) {
            System.out.println("Error while filling the subCategory List");
        }
        return subCategoryList;
    }

    public static void saveProductCSV(File file) throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String query = "select * from products";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            int count = 0;
            while (resultSet.next()) {
                if (count == 0) {
                    sb.append("productTitle");
                    sb.append(";");
                    sb.append("subCategory");
                    sb.append(";");
                    sb.append("price");
                    sb.append(";");
                    sb.append("productDescription");
                    sb.append("\r\n");
                    count++;
                } else {
                    sb.append(resultSet.getString("productTitle"));
                    sb.append(";");
                    sb.append(resultSet.getString("subCategory"));
                    sb.append(";");
                    sb.append(resultSet.getString("price"));
                    sb.append(";");
                    sb.append(resultSet.getString("productDescription"));
                    sb.append("\r\n");
                    count++;
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(sb.toString());
            fileWriter.close();
            System.out.println("CSV created");

        } catch (Exception e) {
            System.out.println("error when saving the database to CSV file");
            e.printStackTrace();
        }
    }

    public static void saveCategoryCSV(File file) throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String query = "select * from category";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            int count = 0;
            while (resultSet.next()) {
                if (count == 0) {
                    sb.append("subCategory");
                    sb.append(";");
                    sb.append("mainCategory");
                    sb.append(";");
                    sb.append("\r\n");
                    count++;
                } else {
                    sb.append(resultSet.getString("subCategory"));
                    sb.append(";");
                    sb.append(resultSet.getString("mainCategory"));
                    sb.append(";");
                    sb.append("\r\n");
                    count++;
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(sb.toString());
            fileWriter.close();
            System.out.println("CSV created");

        } catch (Exception e) {
            System.out.println("error when saving the database to CSV file");
            e.printStackTrace();
        }
    }
}


//        public static Set<String> fillListWithPrice (String query) throws SQLException, ClassNotFoundException {
//            Statement statement = null;
//            ResultSet resultSet = null;
//            Set<String> pricelist = new TreeSet<>();
//            try {
//                connectDatabase();
//                statement = connection.createStatement();
//                resultSet = statement.executeQuery(query);
//                while (resultSet.next()) {
//                    Product product = new Product(resultSet.getString("productTitle"), resultSet.getString("category")
//                            , resultSet.getString("price"), resultSet.getString("productDescription"));
//                    while (resultSet.next()) {
//                        Product product = new Product(resultSet.getString("productTitle"), resultSet.getInt("catId")
//                                , resultSet.getString("price"), resultSet.getString("productDescription"), resultSet.getInt("productId"));
//                        pricelist.add(product.getPrice());
//                    }
//                } catch(SQLException ex){
//                    System.out.println("Error while filling the pricelist");
//                } finally{
//                    if (resultSet != null) {
//                        resultSet.close();
//                    }
//                    if (statement != null) {
//                        statement.close();
//                    }
//                    closeDataBase();
//                }
//                return pricelist;
//            }
//        }

//            public static Set<String> fillListWithDescription (String query) throws SQLException, ClassNotFoundException
//            {
//                Statement statement = null;
//                ResultSet resultSet = null;
//                Set<String> descriptionList = new TreeSet<>();
//                try {
//                    connectDatabase();
//                    statement = connection.createStatement();
//                    resultSet = statement.executeQuery(query);
//                    while (resultSet.next()) {
//                        Product product = new Product(resultSet.getString("productTitle"), resultSet.getString("category")
//                                , resultSet.getString("price"), resultSet.getString("productDescription"));
//                        while (resultSet.next()) {
//                            Product product = new Product(resultSet.getString("productTitle"), resultSet.getInt("catId")
//                                    , resultSet.getString("price"), resultSet.getString("productDescription"), resultSet.getInt("productId"));
//                            descriptionList.add(product.getDescription());
//                        }
//                    } catch(SQLException ex){
//                        System.out.println("Error while filling the descriptionlist");
//                    } finally{
//                        if (resultSet != null) {
//                            resultSet.close();
//                        }
//                        if (statement != null) {
//                            statement.close();
//                        }
//                        closeDataBase();
//                    }
//                    return descriptionList;
//                }
//            }



