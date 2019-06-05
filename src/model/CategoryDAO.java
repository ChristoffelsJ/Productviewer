package model;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAO {

    public static List<String> getInitialMainCategory() throws SQLException, ClassNotFoundException {
        String query = "select mainCategory FROM category";
        try {
            return util.DBUtil.fillListWithMainCategory(query);
        } catch (SQLException ex) {
            System.out.println("Error while getting initial mainCategories");
            throw ex;
        }
    }
    public static List<String> getInitialSubCategory() throws SQLException, ClassNotFoundException {
        String query = "select subCategory FROM category";
        try {
            return util.DBUtil.fillListWithSubCategory(query);
        } catch (SQLException ex) {
            System.out.println("Error while getting initial subCategories");
            throw ex;
        }
    }
}
