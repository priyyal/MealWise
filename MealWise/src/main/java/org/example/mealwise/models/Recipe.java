package org.example.mealwise.models;

import java.util.HashMap;
import java.util.List;

public class Recipe {
    private int id;
    private String title;
    private String summary;
    private String instructions;
    private String cookingTime;
    private List<String> imageUrl;
    private HashMap<String, Integer> nutrition;

    public Recipe(int id, String title){
        this.id = id;
        this.title = title;
    }

    public Recipe(int id, String title, String summary, String description, List<String> imageUrl, String cookingTime, HashMap<String, Integer> calories) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.instructions = description;
        this.imageUrl = imageUrl;
        this.cookingTime = cookingTime;
        this.nutrition = calories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public HashMap<String, Integer> getNutrition() {
        return nutrition;
    }

    public void setNutrition(HashMap<String, Integer> calories) {
        this.nutrition = calories;
    }

    @Override
    public String toString(){
        return id + title + "/n" + "/nSummary" + summary + "Instructions" + instructions ;
    }
}
