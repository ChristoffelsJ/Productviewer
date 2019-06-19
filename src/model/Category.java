package model;

/** Category class
 *
 */
public class Category {

    private String subCategory;
    private String mainCategory;

    public Category() {
    }

    /** This constructor creates a category with a main and a sub category
     *
     * @param subCategory add subCategory name
     * @param mainCategory add mainCategory name
     */
    public Category(String subCategory, String mainCategory) {
        this.subCategory = subCategory;
        this.mainCategory = mainCategory;
    }

    /**
     *
     * @return subCategory
     */
    public String getSubCategory() {
        return subCategory;
    }

    /**
     *
     * @param subCategory add subcategory name
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    /**
     *
     * @return MainCategory
     */
    public String getMainCategory() {
        return mainCategory;
    }

    /**
     *
     * @param mainCategory add maincategory name
     */
    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }
}
