//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import fpt.edu.vn.recipeproject.controller.RecipeModel;
import fpt.edu.vn.recipeproject.databinding.ActivityMainBinding;
import fpt.edu.vn.recipeproject.model.Recipe;
import fpt.edu.vn.recipeproject.view.RecipeAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecipeModel model;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new RecipeModel(this);

        // RecyclerView + Adapter
        adapter = new RecipeAdapter(model.getAll(), new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Intent i = new Intent(MainActivity.this, RecipeDetailActivity.class);
                i.putExtra("recipe_id", recipe.getId());
                startActivity(i);
            }

            @Override
            public void onEditClick(Recipe recipe) {
                Intent i = new Intent(MainActivity.this, RecipeFormActivity.class);
                i.putExtra("recipe_id", recipe.getId());
                startActivity(i);
            }

            @Override
            public void onDeleteClick(Recipe recipe) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Recipe")
                        .setMessage("Are you sure you want to delete this recipe?")
                        .setPositiveButton("Yes", (d, w) -> {
                            model.delete(recipe);
                            adapter.updateData(model.getAll());
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        binding.recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recipeRecyclerView.setAdapter(adapter);

        // Add button → Add Recipe screen
        binding.addButton.setOnClickListener(v ->
                startActivity(new Intent(this, RecipeFormActivity.class)));

        // Live search by name
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Recipe> results = model.searchByName(s.toString());
                adapter.updateData(results);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list when returning from Add/Update/Detail
        adapter.updateData(model.getAll());
    }
}
