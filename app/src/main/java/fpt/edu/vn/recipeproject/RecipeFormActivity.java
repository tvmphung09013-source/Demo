package fpt.edu.vn.recipeproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.recipeproject.controller.RecipeModel;
import fpt.edu.vn.recipeproject.databinding.ActivityRecipeFormBinding;
import fpt.edu.vn.recipeproject.model.Recipe;

public class RecipeFormActivity extends AppCompatActivity {

    private ActivityRecipeFormBinding binding;
    private RecipeModel model;
    private Recipe editing; // null = add

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new RecipeModel(this);

        // Nếu có id → load sang chế độ update
        int id = getIntent().getIntExtra("recipe_id", -1);
        if (id != -1) {
            editing = model.getById(id);
            if (editing != null) {
                binding.nameInput.setText(editing.getName());
                binding.cuisineInput.setText(editing.getCuisine());

                // Hiển thị lại thời lượng theo format đẹp
                Integer mins = parseDurationToMinutes(editing.getCookingTime());
                if (mins != null) {
                    binding.cookingTimeInput.setText(formatDuration(mins));
                } else {
                    binding.cookingTimeInput.setText(extractMinutes(editing.getCookingTime()));
                }

                binding.ingredientsInput.setText(editing.getIngredients());
            }
        }

        // Bắt lỗi theo thời gian thực
        addLiveValidation();

        // Save / Back
        binding.saveButton.setOnClickListener(v -> save());
        binding.backButton.setOnClickListener(v -> finish());

        // Ban đầu kiểm tra luôn để set trạng thái nút Save
        updateSaveEnabled();
    }

    /** Thêm TextWatcher cho 4 ô để validate realtime */
    private void addLiveValidation() {
        TextWatcher tw = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { validateAll(); }
            @Override public void afterTextChanged(Editable s) {}
        };
        binding.nameInput.addTextChangedListener(tw);
        binding.cuisineInput.addTextChangedListener(tw);
        binding.cookingTimeInput.addTextChangedListener(tw);
        binding.ingredientsInput.addTextChangedListener(tw);
    }

    /** Validate toàn bộ fields + set error cho từng ô */
    private boolean validateAll() {
        boolean ok = true;

        String name = binding.nameInput.getText().toString().trim();
        String cuisine = binding.cuisineInput.getText().toString().trim();
        String timeStr = binding.cookingTimeInput.getText().toString().trim();
        String ing = binding.ingredientsInput.getText().toString().trim();

        // Name: 3..50
        if (name.isEmpty()) {
            binding.nameInput.setError("Name is required");
            ok = false;
        } else if (name.length() < 3) {
            binding.nameInput.setError("Name must be at least 3 characters");
            ok = false;
        } else if (name.length() > 50) {
            binding.nameInput.setError("Name must be ≤ 50 characters");
            ok = false;
        } else {
            binding.nameInput.setError(null);
        }

        // Cuisine: 3..30
        if (cuisine.isEmpty()) {
            binding.cuisineInput.setError("Cuisine is required");
            ok = false;
        } else if (cuisine.length() < 3) {
            binding.cuisineInput.setError("Cuisine must be at least 3 characters");
            ok = false;
        } else if (cuisine.length() > 30) {
            binding.cuisineInput.setError("Cuisine must be ≤ 30 characters");
            ok = false;
        } else {
            binding.cuisineInput.setError(null);
        }

        // Cooking time
        Integer minutes = parseDurationToMinutes(timeStr);
        if (timeStr.isEmpty()) {
            binding.cookingTimeInput.setError("Cooking time is required (e.g., 45, 1h, 1 hour 30 minutes)");
            ok = false;
        } else if (minutes == null) {
            binding.cookingTimeInput.setError("Invalid format. Try: 45, 1h, 1 hour, 1h 30m");
            ok = false;
        } else if (minutes < 1 || minutes > 600) {
            binding.cookingTimeInput.setError("Must be between 1 minute and 10 hours (≤ 600 minutes)");
            ok = false;
        } else {
            binding.cookingTimeInput.setError(null);
        }

        // Ingredients: 5..500
        if (ing.isEmpty()) {
            binding.ingredientsInput.setError("Ingredients is required");
            ok = false;
        } else if (ing.length() < 5) {
            binding.ingredientsInput.setError("Please describe ingredients more clearly");
            ok = false;
        } else if (ing.length() > 500) {
            binding.ingredientsInput.setError("Ingredients must be ≤ 500 characters");
            ok = false;
        } else {
            binding.ingredientsInput.setError(null);
        }

        updateSaveEnabled();
        return ok;
    }

    /** Cho phép / khóa nút Save khi còn lỗi */
    private void updateSaveEnabled() {
        Button btn = binding.saveButton;
        boolean enable = binding.nameInput.getError() == null
                && binding.cuisineInput.getError() == null
                && binding.cookingTimeInput.getError() == null
                && binding.ingredientsInput.getError() == null
                && !binding.nameInput.getText().toString().trim().isEmpty()
                && !binding.cuisineInput.getText().toString().trim().isEmpty()
                && !binding.cookingTimeInput.getText().toString().trim().isEmpty()
                && !binding.ingredientsInput.getText().toString().trim().isEmpty();
        btn.setEnabled(enable);
        btn.setAlpha(enable ? 1f : 0.5f);
    }

    /** Parse thời gian thành phút: hỗ trợ nhiều định dạng linh hoạt */
    private Integer parseDurationToMinutes(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toLowerCase();
        if (s.isEmpty()) return null;

        // chỉ số: "45"
        if (s.matches("^\\d+$")) {
            try { return Integer.parseInt(s); } catch (Exception ignored) {}
        }

        // "45m", "45 min", "45 minutes"
        if (s.matches("^\\d+\\s*(m|min|mins|minute|minutes)\\s*$")) {
            String num = s.replaceAll("\\D+", "");
            try { return Integer.parseInt(num); } catch (Exception ignored) {}
        }

        // "1h", "1 hour"
        if (s.matches("^\\d+\\s*(h|hr|hrs|hour|hours)\\s*$")) {
            String num = s.replaceAll("\\D+", "");
            try { return Integer.parseInt(num) * 60; } catch (Exception ignored) {}
        }

        // "1h 30m", "1 hour 30 minutes"
        if (s.matches("^\\d+\\s*(h|hr|hrs|hour|hours)\\s+\\d+\\s*(m|min|mins|minute|minutes)\\s*$")) {
            try {
                String hourStr = s.replaceAll("^(\\d+)\\D+.*$", "$1");
                String minStr = s.replaceAll("^.*?(\\d+)\\D*$", "$1");
                int hours = Integer.parseInt(hourStr);
                int mins = Integer.parseInt(minStr);
                return hours * 60 + mins;
            } catch (Exception ignored) {}
        }

        return null;
    }

    /** Chuẩn hoá minutes -> "X hour(s) Y minutes" / "X hour(s)" / "NN minutes" */
    private String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int mins  = totalMinutes % 60;
        if (hours == 0) {
            return mins + " minutes";
        } else if (mins == 0) {
            return hours + (hours == 1 ? " hour" : " hours");
        } else {
            return hours + (hours == 1 ? " hour " : " hours ") + mins + " minutes";
        }
    }

    /** Khi load từ DB (đang lưu dạng 'XX minutes' hoặc 'X hours Y minutes'), fallback tách số phút */
    private String extractMinutes(String cookingTimeStored) {
        if (cookingTimeStored == null) return "";
        Integer mins = parseDurationToMinutes(cookingTimeStored);
        return mins == null ? "" : formatDuration(mins);
    }

    private void save() {
        if (!validateAll()) {
            Toast.makeText(this, "Please fix validation errors", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = binding.nameInput.getText().toString().trim();
        String cuisine = binding.cuisineInput.getText().toString().trim();
        int minutes = parseDurationToMinutes(binding.cookingTimeInput.getText().toString().trim());
        String ing = binding.ingredientsInput.getText().toString().trim();

        // Chuẩn hóa hiển thị lưu trong DB
        String displayTime = formatDuration(minutes);

        // Chống trùng Name + Cuisine
        if (editing == null) {
            if (model.existsNameCuisine(name, cuisine)) {
                binding.nameInput.setError("This recipe already exists for this cuisine");
                Toast.makeText(this, "Duplicate recipe", Toast.LENGTH_SHORT).show();
                updateSaveEnabled();
                return;
            }
            model.insert(new Recipe(name, cuisine, displayTime, ing));
            Toast.makeText(this, "Recipe added", Toast.LENGTH_SHORT).show();
        } else {
            if (model.existsNameCuisineExceptId(name, cuisine, editing.getId())) {
                binding.nameInput.setError("This recipe already exists for this cuisine");
                Toast.makeText(this, "Duplicate recipe", Toast.LENGTH_SHORT).show();
                updateSaveEnabled();
                return;
            }
            editing.setName(name);
            editing.setCuisine(cuisine);
            editing.setCookingTime(displayTime);
            editing.setIngredients(ing);
            model.update(editing);
            Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
