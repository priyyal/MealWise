package org.example.mealwise.models;

public class GroceryItem {
    private int id;
    private String name;
    private int category_id;

    public GroceryItem(int id, String name, int category_id) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
    }

    public GroceryItem(String name, int category_id) {
        this.name = name;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("%d: %s", id, name);
    }

    public int getCategoryId() {
        return category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }
}
