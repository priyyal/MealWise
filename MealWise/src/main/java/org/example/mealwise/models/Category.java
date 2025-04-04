package org.example.mealwise.models;

public class Category {
    private int category_id;
    private String categoryName;
    private int categoryShelfLife;

    public Category(String categoryName, int categoryShelfLife) {
        this.categoryName = categoryName;
        this.categoryShelfLife = categoryShelfLife;
    }

    public Category(int category_id, String categoryName, int categoryShelfLife) {
        this.category_id = category_id;
        this.categoryName = categoryName;
        this.categoryShelfLife = categoryShelfLife;
    }

    public int getCategoryShelfLife() {
        return categoryShelfLife;
    }

    public void setCategoryShelfLife(int categoryShelfLife) {
        this.categoryShelfLife = categoryShelfLife;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getcategoryId() {
        return category_id;
    }

    public void setcategoryId(int category_id) {
        this.category_id = category_id;
    }
}
