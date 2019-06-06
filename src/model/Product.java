package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.*;

public class Product {
    private String title;
    private String subCategory;
    private String mainCategory;
    private String price;
    private String description;
    private int productId;
    private InputStream iS;
    private ImageView image;


    public Product(String title, String subCategory, String mainCategory, String price, String description, ImageView image, int productId) {
        this.title = title;
        this.subCategory = subCategory;
        this.mainCategory = mainCategory;
        this.price = price;
        this.description = description;
        this.image = image;
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public InputStream getiS() {
        return iS;
    }

    public void setiS(InputStream iS) {
        this.iS = iS;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

}