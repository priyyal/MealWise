//package org.example.mealwise.api;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class ApiController {
//    String apiKey = "36d374de9d0042228d8ae753cd7bce8c";
//    String url = String.format("https://api.spoonacular.com/recipes/findByIngredients?ingredients=chicken,+Basil,+Honey,+Pineapple&number=10&apiKey=%s",apiKey);
//    String responseBody;
//    HttpClient client = HttpClient.newHttpClient();
//
//    //Get the raw recipe JSON data from the API
//    public String fetchRecipe(){
//        try {
//            // Create the request
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .GET()
//                    .build();
//
//            // Send the request and get the response
//            HttpResponse<String> response = null;
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Check the status code and print the response body
//            if (response.statusCode() == 200) {
//                responseBody = response.body();
//                System.out.println("Response: " + response.body());
//            } else {
//                System.out.println("Failed to fetch data: " + response.statusCode());
//            }
//        }catch (IOException  | InterruptedException e){
//            e.printStackTrace();
//        }
//        return responseBody;
//    }
//
//    //Format the raw recipe data into usable java classes
//    //Return the title and id of the recipe
//    public RecipeDetails parseRecipe() throws JsonProcessingException {
//        String body = fetchRecipe();
//        ObjectMapper mapper = new ObjectMapper();
//        List<Recipe> recipes = mapper.readValue(body, mapper.getTypeFactory().constructCollectionType(ArrayList.class, Recipe.class));
//        String title = recipes.get(0).getTitle();
//        int id = recipes.get(0).getId();
//        return new RecipeDetails(title, id);
//    }
//
//    //Calls the parseInstructions method
//    public String fetchInstructions() throws JsonProcessingException {
//        return parseInstructions(parseRecipe());
//    }
//
//    //Calls the getInstructions() method to get recipe instruction data.
//    // Cleans up and parses the recipe instruction.
//    private String parseInstructions(RecipeDetails recipe) throws JsonProcessingException {
//        String rawinstructions = getInstructions(recipe.getId());
//        String parsedInstructions = recipe.getTitle();
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode rootNode = mapper.readTree(rawinstructions);
//        for (JsonNode node : rootNode) {
//            if (node.has("steps")){
//                for (JsonNode stepNode : node.get("steps")){
//                    parsedInstructions += String.format("%n%d %s", stepNode.get("number").asInt(),stepNode.get("step").asText());
//                }
//            }
//        }
//        return parsedInstructions;
//    }
//
//    //Gets the recipe instructions data from the API given the recipe id
//    private String getInstructions(int id){
//        String url = String.format("https://api.spoonacular.com/recipes/%d/analyzedInstructions?stepBreakdown=true&apiKey=%s", id, apiKey);
//        String responseBody;
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .GET()
//                .build();
//
//        try {
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            if (response.statusCode() == 200) {
//                responseBody = response.body();
//            }else{
//                responseBody = String.valueOf(response.statusCode());
//            }
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return responseBody;
//    }
//}