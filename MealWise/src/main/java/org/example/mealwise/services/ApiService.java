package org.example.mealwise.services;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.example.mealwise.dao.CategoryDAO;
import org.example.mealwise.models.Category;
import org.example.mealwise.models.GroceryItem;
import org.example.mealwise.models.Inventory;
import org.example.mealwise.models.Recipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiService {
    private static final String API_HOST = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
    private static final String API_KEY = System.getenv("MEALWISE_API_KEY"); // 👈 from env
    private static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/ingredients/search";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int BATCH_SIZE = 100;
    private static final CategoryDAO CATEGORY_DAO = new CategoryDAO();

    public static List<GroceryItem> fetchIngredients(String query) throws IOException, InterruptedException {
        int offset = 0;
        boolean hasMoreResults = true;
        List<GroceryItem> groceryItemList = new ArrayList<>();

        while(hasMoreResults){
            URI uri = URI.create(BASE_URL + "?query=" + query + "&number=" + BATCH_SIZE + "&offset=" + offset);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", API_HOST)
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                JsonNode rootNode = OBJECT_MAPPER.readTree(response.body());
                JsonNode ingredients = rootNode.get("results");

                if (ingredients == null || ingredients.isEmpty()){
                    hasMoreResults = false;
                    break;
                }

                for (JsonNode ingredient : ingredients) {
                    int id = ingredient.path("id").asInt();
                    String name = ingredient.path("name").asText();
                    Category category = CATEGORY_DAO.getByName(getItemAisle(id));
                    int categoryId = 0;
                    if (category != null) {
                        categoryId = category.getcategoryId();
                    } else {
                        continue;
                    }
                    GroceryItem newGroceryItem = new GroceryItem(name, categoryId);
                    groceryItemList.add(newGroceryItem);
                    Thread.sleep(1000);
                }
                offset += BATCH_SIZE;
            } else {
                System.out.println("Failed to fetch data: " + response.statusCode());
                break;
            }
        }
        return groceryItemList;
    }

    private static String getItemAisle(int itemID) throws IOException, InterruptedException {
        String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/ingredients/";
        URI uri = URI.create(BASE_URL + itemID + "/information");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();

        if (response.statusCode() == 200){
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode aisle = rootNode.get("aisle");
            return aisle.asText();
        }
        return null;
    }

    public String getRecipes(List<Inventory> expiringItems) throws IOException, InterruptedException {
        String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients";

        List<String> itemNames = new ArrayList<>();
        for (Inventory inventory : expiringItems) {
            itemNames.add(inventory.getGroceryName());
        }

        String joinedList = String.join(",", itemNames);
        String url = BASE_URL + "?ingredients=" + URLEncoder.encode(joinedList, "UTF-8") + "&number=10";

        HttpRequest request = createRequest(url);

        return getResponse(request);
    }

    public List<Recipe> parseRecipe(List<Inventory> expiringItems) throws IOException, InterruptedException {
        String json = getRecipes(expiringItems);
        List<Recipe> recipes = new ArrayList<>();

        if (json != null) {
            JsonNode rootNode = OBJECT_MAPPER.readTree(json);

            for (JsonNode node : rootNode) {
                int id = node.get("id").asInt();
                String title = node.get("title").asText();
                recipes.add(new Recipe(id, title));
            }
        }
        return recipes;
    }

    public List<Recipe> getRecipeWithDetails(List<Inventory> expiringItems) throws IOException, InterruptedException {
        String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/";
        List<Recipe> recipes = parseRecipe(expiringItems);

        for (Recipe r : recipes){
            String url = BASE_URL + r.getId() + "/information?includeNutrition=true";
            HttpRequest request = createRequest(url);
            String response = getResponse(request);
            JsonNode rootNode = OBJECT_MAPPER.readTree(response);

            r.setCookingTime(rootNode.path("readyInMinutes").asText());
            r.setNutrition(extractNutrition(rootNode));
            String summary = rootNode.path("summary").asText();
            r.setSummary(stripHtml(summary));
            r.setInstructions(extractInstructions(rootNode));
        }
        return recipes;
    }

    private HashMap<String, Integer> extractNutrition(JsonNode node){
        HashMap<String, Integer> nutrition = new HashMap<>();
        JsonNode nutrients = node.path("nutrition").path("nutrients");

        if (!nutrients.isMissingNode() && nutrients.isArray()){
            for (JsonNode n : nutrients){
                String name = n.get("name").asText();
                int amount = n.get("amount").asInt();

                switch (name) {
                    case "Calories":
                    case "Fat":
                    case "Protein":
                    case "Carbohydrates":
                        nutrition.put(name, amount);
                        break;
                }
            }
        }
        return nutrition;
    }

    private String extractInstructions(JsonNode rootNode){
        JsonNode instructionsArray = rootNode.path("analyzedInstructions");
        if (instructionsArray.isArray() && !instructionsArray.isEmpty()){
            JsonNode stepsArray = instructionsArray.get(0).path("steps");
            StringBuilder descriptionBuilder = new StringBuilder();

            for (JsonNode step : stepsArray){
                int number = step.get("number").asInt();
                String text = step.get("step").asText();
                descriptionBuilder.append(String.format("%n Step%d: %s", number, text));
            }
            return descriptionBuilder.toString();
        } else {
            return rootNode.path("instructions").asText();
        }
    }

    public String getImages(Recipe recipe) throws IOException, InterruptedException {
        String BASE_URL= "https://api.pexels.com/v1//search";
        String query = BASE_URL + "?query=" + URLEncoder.encode(recipe.getTitle(), "UTF-8") + "&per_page=1";
        String apiKey = "9mpIsMVWKBvLrd8jQFlDRwLnXKL7uLOCtacP5gEEQ4jqJk0JpdXvluhc"; // 👉 Optional: make this env var too

        HttpRequest request = createImageRequest(query, apiKey);
        return getResponse(request);
    }

    public void parseImages(Recipe recipe) throws IOException, InterruptedException {
        String json = getImages(recipe);

        JsonNode rootNode = OBJECT_MAPPER.readTree(json);
        JsonNode photos = rootNode.get("photos");

        List<String> imageUrls = new ArrayList<>();
        if (photos != null && photos.size() > 0){
            for (int i = 0; i < Math.min(5, photos.size()); i++) {
                String imageUrl = photos.get(i).get("src").get("original").asText();
                imageUrls.add(imageUrl);
            }
            recipe.setImageUrl(imageUrls);
        }
    }

    public static HttpRequest createRequest(String url){
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();
    }

    public static HttpRequest createImageRequest(String url, String apiKey){
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", apiKey)
                .GET()
                .build();
    }

    public static String getResponse(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            return response.body();
        }
        return response.statusCode() + "";
    }

    private static String stripHtml(String html){
        return html.replaceAll("<[^>]*>", "").replaceAll("\\s{2,}"," ");
    }
}
