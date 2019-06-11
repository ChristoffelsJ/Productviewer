package model;

import java.util.List;

/** Data Access Object for category
 *
 */
public class CategoryDAO {
    /** query for getting all the main category
     *
     * @return list of all the main category
     */
    public static List<String> getInitialMainCategory() {
        String query = "select distinct mainCategory FROM category";
        return util.DBUtil.fillListWithMainCategory(query);
    }

    /** query for getting all the sub category
     *
     * @param mainCategory give the main category
     * @return list of all the sub category
     */
    public static List<String> getInitialSubCategory(String mainCategory){
        String query = "select subCategory FROM category WHERE mainCategory = '" + mainCategory + "'";
        return util.DBUtil.fillListWithSubCategory(query);
    }

    /** query for adding main and sub to database
     *
     * @param mainCategory give the main category
     * @param subCategory give the sub category
     */
    public static void addCategory(String mainCategory, String subCategory) {

        String update = "INSERT INTO category VALUES  ('" + subCategory + "','" + mainCategory + "')";
        util.DBUtil.updateQuery(update);

    }

    /** query for adding main and sub to database
     *
     * @param category give a category
     */
    public static void addCategory(Category category) {

        String update = "INSERT INTO category (subCategory, mainCategory) VALUES " +
                "('" + category.getSubCategory() + "','" + category.getMainCategory() + "')";
        util.DBUtil.updateQuery(update);
    }
}
