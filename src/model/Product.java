package model;

import javax.swing.text.html.ImageView;
import java.io.FileInputStream;
import java.io.InputStream;

public class Product {
    private String title;
    private String category;
    private String price;
    private String description;
    private int productId;
    private InputStream fis;


    public Product(String title, String category, String price, String description, InputStream fis, int productId) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.description = description;
        this.fis = fis;
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public InputStream getFis() {
        return fis;
    }

    public void setFis(InputStream fis) {
        this.fis = fis;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}