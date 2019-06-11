package model;

import javafx.scene.image.ImageView;
import java.io.*;

/** product class
 *
 */
public class Product {
    private String title;
    private String subCategory;
    private String mainCategory;
    private String price;
    private String description;
    private int productId;
    private InputStream iS;
    private ImageView image;
    private String imagePath;

    /** constructor for making a new product
     *
     * @param title title name of product
     * @param subCategory sub category name of product
     * @param mainCategory main category name of product
     * @param price price of product
     * @param description description of product
     * @param image image of product
     * @param productId product id
     * @param imagePath image path
     */
    public Product(String title, String subCategory, String mainCategory, String price, String description, ImageView image, int productId, String imagePath){
        this.title=title;
        this.subCategory=subCategory;
        this.mainCategory=mainCategory;
        this.price=price;
        this.description=description;
        this.image=image;
        this.productId=productId;
        this.imagePath=imagePath;
    }

    public static class Builder {
        private String title;
        private String subCategory;
        private String mainCategory;
        private String price;
        private String description;
        private int productId;
        private ImageView image;
        private String imagePath;

        public Builder(){}

        /**
         *
         * @param title title
         * @return title
         */
        public Builder withTitle(String title){
            this.title = title;
            return this;
        }

        /**
         *
         * @param subCategory sub category
         * @return subCategory
         */
        public Builder withSubCat(String subCategory){
            this.subCategory = subCategory;
            return this;
        }

        /**
         *
         * @param mainCategory main category
         * @return mainCategory
         */
        public Builder withMainCat(String mainCategory){
            this.mainCategory = mainCategory;
            return this;
        }

        /**
         *
         * @param price price
         * @return price
         */
        public Builder withPrice(String price){
            this.price = price;
            return this;
        }

        /**
         *
         * @param description description
         * @return description
         */
        public Builder withDescription(String description){
            this.description = description;
            return this;
        }

        /**
         *
         * @param image image
         * @return image
         */
        public Builder withImageView(ImageView image){
            this.image = image;
            return this;
        }

        /**
         *
         * @param productId id
         * @return id
         */
        public Builder withId(int productId){
            this.productId = productId;
            return this;
        }

        /**
         *
         * @param imagePath path
         * @return path
         */
        public Builder withPath(String imagePath){
            this.imagePath = imagePath;
            return this;
        }

        /**
         *
         * @return new product
         */
        public Product build(){
            return new Product(title,subCategory,mainCategory,price,description,image,productId,imagePath);
        }
    }

    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return sub category
     */
    public String getSubCategory() {
        return subCategory;
    }

    /**
     *
     * @param subCategory  sub category
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    /**
     *
     * @return main category
     */
    public String getMainCategory() {
        return mainCategory;
    }

    /**
     *
     * @param mainCategory main category
     */
    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    /**
     *
     * @return price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return id
     */
    public int getProductId() {
        return productId;
    }

    /**
     *
     * @param productId id
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     *
     * @return inputstream
     */
    public InputStream getiS() {
        return iS;
    }

    /**
     *
     * @param iS inputstream
     */
    public void setiS(InputStream iS) {
        this.iS = iS;
    }

    /**
     *
     * @return image
     */
    public ImageView getImage() {
        return image;
    }

    /**
     *
     * @param image image
     */
    public void setImage(ImageView image) {
        this.image = image;
    }

    /**
     *
     * @return image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     *
     * @param imagePath set image path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}