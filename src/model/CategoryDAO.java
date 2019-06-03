package model;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAO {

    public static List<String> getInitialCategory() throws SQLException, ClassNotFoundException {
        String query = "select subCategory FROM category";
        try {
            return util.DBUtil.fillListWithCategory(query);
        } catch (SQLException ex) {
            System.out.println("Error while getting initial products");
            throw ex;
        }
    }
}
