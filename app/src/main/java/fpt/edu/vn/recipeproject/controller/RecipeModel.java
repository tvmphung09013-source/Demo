package fpt.edu.vn.recipeproject.controller;

import android.content.Context;

import java.util.List;

import fpt.edu.vn.recipeproject.database.AppDatabase;
import fpt.edu.vn.recipeproject.model.Recipe;

public class RecipeModel {
    private final AppDatabase db;

    public RecipeModel(Context ctx) {
        this.db = AppDatabase.getInstance(ctx);
    }

    public List<Recipe> getAll() { return db.recipeDao().getAll(); }
    public Recipe getById(int id) { return db.recipeDao().getById(id); }
    public List<Recipe> searchByName(String q) { return db.recipeDao().searchByName("%" + q + "%"); }
    public void insert(Recipe r) { db.recipeDao().insert(r); }
    public void update(Recipe r) { db.recipeDao().update(r); }
    public void delete(Recipe r) { db.recipeDao().delete(r); }

    // ====== BỔ SUNG: check trùng ======
    public boolean existsNameCuisine(String name, String cuisine) {
        return db.recipeDao().countByNameCuisine(name, cuisine) > 0;
    }
    public boolean existsNameCuisineExceptId(String name, String cuisine, int excludeId) {
        return db.recipeDao().countByNameCuisineExcludingId(name, cuisine, excludeId) > 0;
    }
    // ==================================
}
