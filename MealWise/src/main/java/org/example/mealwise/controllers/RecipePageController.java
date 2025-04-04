package org.example.mealwise.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.example.mealwise.models.Inventory;
import org.example.mealwise.models.Recipe;
import org.example.mealwise.services.ApiService;
import org.example.mealwise.services.InventoryManager;
import org.example.mealwise.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RecipePageController implements Initializable {

    @FXML
    private FlowPane recipeContainer;

    @FXML
    private Pane darkOverlay;

    @FXML
    private TextField searchField;

    private List<Recipe> allRecipes = new ArrayList<>();


    InventoryManager manager = new InventoryManager();
    ApiService api = new ApiService();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int userId = SessionManager.getUserId();
        List<Inventory> expiringItems = manager.getSoonToExpireItems(userId);
        List<Inventory> items = createInventory();

        try {
            //This is the main one   List<Recipe> recipess = api.getRecipeWithDetails(expiringItems);
            List<Recipe> recipes = api.getRecipeWithDetails(items);
            for (Recipe recipe : recipes) {
                api.parseImages(recipe);
            }
            allRecipes = recipes;
            displayRecipes(recipes);

            searchField.textProperty().addListener((obs, oldText, newText) -> {
                try {
                    filterRecipes(newText.toLowerCase());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        darkOverlay.setVisible(false);
    }



    private void displayRecipes(List<Recipe> recipes) throws IOException {
        recipeContainer.getChildren().clear();
        for (Recipe recipe : recipes) {
            Node card = loadRecipeCard(recipe);
            recipeContainer.getChildren().add(card);
        }
    }


    private Node loadRecipeCard(Recipe recipe) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/RecipeCard.fxml"));
        Node card = loader.load();

        RecipeCardController controller = loader.getController();
        controller.setRecipe(recipe);
        controller.setParentController(this);
        return card;
    }

    public List<Recipe> getMockRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Cheese and beans"));
        recipes.add(new Recipe(2, "Rice and eggs"));
        recipes.add(new Recipe(3, "Beef wellington"));
        recipes.add(new Recipe(4, "Milk and cheese"));
        recipes.add(new Recipe(5, "Ice cream"));
        List<String> images = new ArrayList<>();
        images.add("https://images.pexels.com/photos/2422293/pexels-photo-2422293.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        for (Recipe recipe : recipes) {
            recipe.setImageUrl(images);
        }
        return recipes;
    }

    public Pane getDarkOverlay() {
        return darkOverlay;
    }

    public List<Inventory> createInventory(){
        List<Inventory> inventoryList = new ArrayList<>();

        inventoryList.add(new Inventory(1, "Milk", 1, 1, LocalDate.now().plusDays(5)));
        inventoryList.add(new Inventory(2, "Eggs", 1, 12, LocalDate.now().plusDays(10)));
        inventoryList.add(new Inventory(3, "Bread", 1, 1, LocalDate.now().plusDays(3)));
        inventoryList.add(new Inventory(4, "Cheddar Cheese", 1, 1, LocalDate.now().plusDays(7)));
        inventoryList.add(new Inventory(5, "Yogurt", 1, 4, LocalDate.now().plusDays(6)));
        inventoryList.add(new Inventory(6, "Butter", 1, 1, LocalDate.now().plusDays(20)));
        inventoryList.add(new Inventory(7, "Spinach", 1, 1, LocalDate.now().plusDays(2)));
        inventoryList.add(new Inventory(8, "Chicken Breast", 1, 2, LocalDate.now().plusDays(2)));
        inventoryList.add(new Inventory(9, "Apples", 1, 6, LocalDate.now().plusDays(8)));
        inventoryList.add(new Inventory(10, "Tomatoes", 1, 4, LocalDate.now().plusDays(5)));

        return inventoryList;
    }

    private void filterRecipes(String query) throws IOException {
        if (query == null || query.isEmpty()) {
            displayRecipes(allRecipes);
            return;
        }

        List<Recipe> filtered = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getTitle().toLowerCase().contains(query)) {
                filtered.add(recipe);
            }
        }

        displayRecipes(filtered);
    }
}