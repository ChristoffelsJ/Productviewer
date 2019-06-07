package model;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAO {



    public static List<String> getInitialMainCategory() throws SQLException, ClassNotFoundException {
        String query = "select distinct mainCategory FROM category";
        try {
            return util.DBUtil.fillListWithMainCategory(query);
        } catch (SQLException ex) {
            System.out.println("Error while getting initial mainCategories");
            throw ex;
        }
    }
    public static List<String> getInitialSubCategory(String mainCategory) throws SQLException, ClassNotFoundException {
//        String query = "select subCategory FROM category";
        String query = "select subCategory FROM category WHERE mainCategory = '"+mainCategory+"'";
        try {
            return util.DBUtil.fillListWithSubCategory(query);
        } catch (SQLException ex) {
            System.out.println("Error while getting initial subCategories");
            throw ex;
        }
    }

    public static void addCategory(String mainCategory, String subCategory) throws SQLException, ClassNotFoundException {

        String update = "INSERT INTO category VALUES  ('"+subCategory+"','" + mainCategory + "')";
        try {
            util.DBUtil.updateQuery(update);
        } catch (SQLException ex) {
            System.out.println("Error when implementing categories in database");
            throw ex;
        }
    }
}
