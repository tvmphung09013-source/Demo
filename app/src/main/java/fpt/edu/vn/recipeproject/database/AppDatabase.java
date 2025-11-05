//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Corrected import path for RecipeDao
import fpt.edu.vn.recipeproject.dao.RecipeDao;
import fpt.edu.vn.recipeproject.model.Recipe;
import fpt.edu.vn.recipeproject.model.User;
import fpt.edu.vn.recipeproject.model.UserDao;

@Database(entities = {Recipe.class, User.class}, version = 2) // Incremented version number
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract UserDao userDao(); // Added UserDao abstract method

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "recipe_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // for lab simplicity
                            .build();
                }
            }
        }
        return instance;
    }
}
