package model;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAO {

    public static List<String> getInitialMainCategory(){
        String query = "select distinct mainCategory FROM category";
        return util.DBUtil.fillListWithMainCategory(query);
    }
    public static List<String> getInitialSubCategory(String mainCategory) throws SQLException, ClassNotFoundException {
        String query = "select subCategory FROM category WHERE mainCategory = '"+mainCategory+"'";
        return util.DBUtil.fillListWithSubCategory(query);
    }

    public static void addCategory(String mainCategory, String subCategory){

        String update = "INSERT INTO category VALUES  ('"+subCategory+"','" + mainCategory + "')";
        util.DBUtil.updateQuery(update);
    }
    public static void addCategory(Category category){

        String update = "INSERT INTO category (subCategory, mainCategory) VALUES " +
                "('"+category.getSubCategory()+"','" + category.getMainCategory() + "')";
        util.DBUtil.updateQuery(update);
    }
}
