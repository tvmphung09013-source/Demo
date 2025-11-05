package fpt.edu.vn.recipeproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fpt.edu.vn.recipeproject.model.Recipe;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    List<Recipe> getAll();

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    Recipe getById(int id);

    @Query("SELECT * FROM recipes WHERE name LIKE :name ORDER BY name ASC")
    List<Recipe> searchByName(String name);

    // ====== BỔ SUNG: kiểm tra trùng Name + Cuisine ======
    @Query("SELECT COUNT(*) FROM recipes WHERE LOWER(name)=LOWER(:name) AND LOWER(cuisine)=LOWER(:cuisine)")
    int countByNameCuisine(String name, String cuisine);

    @Query("SELECT COUNT(*) FROM recipes WHERE LOWER(name)=LOWER(:name) AND LOWER(cuisine)=LOWER(:cuisine) AND id != :excludeId")
    int countByNameCuisineExcludingId(String name, String cuisine, int excludeId);
    // ====================================================

    @Insert
    void insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);
}
