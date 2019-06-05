package model;

public class Category {
    private String subCategory;
    private String mainCategory;

    public Category(String subCategory, String mainCategory) {
        this.subCategory = subCategory;
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }
}
