//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject.database;


import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import fpt.edu.vn.recipeproject.dao.RecipeDao;
import fpt.edu.vn.recipeproject.model.Recipe;


@Database(entities = {Recipe.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();


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