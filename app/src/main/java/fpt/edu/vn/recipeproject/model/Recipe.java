//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String cuisine;
    private String cookingTime;
    private String ingredients;


    public Recipe(String name, String cuisine, String cookingTime, String ingredients) {
        this.name = name;
        this.cuisine = cuisine;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
    }


    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }


    public String getCookingTime() { return cookingTime; }
    public void setCookingTime(String cookingTime) { this.cookingTime = cookingTime; }


    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
}