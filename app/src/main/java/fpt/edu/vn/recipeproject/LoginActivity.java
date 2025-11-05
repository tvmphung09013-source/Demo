//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.recipeproject.database.AppDatabase;
import fpt.edu.vn.recipeproject.model.User;
import fpt.edu.vn.recipeproject.model.UserDao;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = AppDatabase.getInstance(this).userDao();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegisterLink = findViewById(R.id.tvRegisterLink);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegisterLink.setOnClickListener(v -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showResultDialog("Login Failed", "Username and password are required.");
            return;
        }

        User user = userDao.findByUsernameAndPassword(username, password);

        if (user != null) {
            // Login successful
            showResultDialog("Login Successful", "Welcome, " + user.getUsername() + "!");
            // Navigate to MainActivity after a successful login
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish LoginActivity to prevent user from going back
        } else {
            // Login failed
            showResultDialog("Login Failed", "Invalid username or password.");
        }
    }

    private void showResultDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
