//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.vn.recipeproject.R;
import fpt.edu.vn.recipeproject.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
        void onEditClick(Recipe recipe);
        void onDeleteClick(Recipe recipe);
    }

    private final List<Recipe> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public RecipeAdapter(List<Recipe> initial, OnItemClickListener listener) {
        if (initial != null) items.addAll(initial);
        this.listener = listener;
    }

    /** Cập nhật dữ liệu và refresh list */
    public void updateData(List<Recipe> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder h, int position) {
        Recipe r = items.get(position);
        h.name.setText(r.getName());
        h.cuisine.setText(r.getCuisine());
        h.cookingTime.setText(r.getCookingTime());

        h.itemView.setOnClickListener(v -> listener.onItemClick(r));
        h.editBtn.setOnClickListener(v -> listener.onEditClick(r));
        h.deleteBtn.setOnClickListener(v -> listener.onDeleteClick(r));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView name, cuisine, cookingTime;
        Button editBtn, deleteBtn;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            cuisine = itemView.findViewById(R.id.itemCuisine);
            cookingTime = itemView.findViewById(R.id.itemCookingTime);
            editBtn = itemView.findViewById(R.id.editButton);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
        }
    }
}
