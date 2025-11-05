//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject;


import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import fpt.edu.vn.recipeproject.controller.RecipeModel;
import fpt.edu.vn.recipeproject.databinding.ActivityRecipeDetailBinding;
import fpt.edu.vn.recipeproject.model.Recipe;


public class RecipeDetailActivity extends AppCompatActivity {


    private ActivityRecipeDetailBinding binding;
    private RecipeModel model;
    private int recipeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        model = new RecipeModel(this);
        recipeId = getIntent().getIntExtra("recipe_id", -1);
        load();


        binding.backButton.setOnClickListener(v -> finish());
        binding.editButton.setOnClickListener(v -> {
            Intent i = new Intent(this, RecipeFormActivity.class);
            i.putExtra("recipe_id", recipeId);
            startActivity(i);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        load();
    }


    private void load() {
        if (recipeId == -1) return;
        Recipe r = model.getById(recipeId);
        if (r == null) return;
        binding.detailName.setText("Name: " + r.getName());
        binding.detailCuisine.setText("Cuisine: " + r.getCuisine());
        binding.detailCookingTime.setText("Cooking Time: " + r.getCookingTime());
        binding.detailIngredients.setText("Ingredients: " + r.getIngredients());
    }
}