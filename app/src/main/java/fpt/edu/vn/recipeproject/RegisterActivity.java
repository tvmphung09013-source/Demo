//@Author: Tran Van Minh Phung CE171682
package fpt.edu.vn.recipeproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.recipeproject.database.AppDatabase;
import fpt.edu.vn.recipeproject.model.User;
import fpt.edu.vn.recipeproject.model.UserDao;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword, etEmail, etPhoneNumber;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDao = AppDatabase.getInstance(this).userDao();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (!validateInput(username, password, confirmPassword, email, phoneNumber)) {
            return;
        }

        if (userDao.findByUsername(username) != null) {
            showResultDialog("Registration Failed", "Username already exists. Please choose another one.", null);
            return;
        }

        try {
            User user = new User(username, password, email, phoneNumber);
            userDao.insert(user);
            // Show success dialog and navigate to Login on OK click
            showResultDialog("Registration Successful", "Your account has been created.", () -> {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        } catch (Exception e) {
            showResultDialog("Registration Failed", "An error occurred: " + e.getMessage(), null);
        }
    }

    private boolean validateInput(String username, String password, String confirmPassword, String email, String phoneNumber) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber)) {
            showResultDialog("Validation Error", "All fields are required.", null);
            return false;
        }

        if (username.length() < 5) {
            showResultDialog("Validation Error", "Username must be at least 5 characters long.", null);
            return false;
        }

        if (password.length() < 6) {
            showResultDialog("Validation Error", "Password must be at least 6 characters long.", null);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showResultDialog("Validation Error", "Passwords do not match.", null);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showResultDialog("Validation Error", "Please enter a valid email address.", null);
            return false;
        }

        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            showResultDialog("Validation Error", "Please enter a valid phone number.", null);
            return false;
        }

        return true;
    }

    private void showResultDialog(String title, String message, Runnable onOk) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (onOk != null) {
                        onOk.run();
                    } else {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
}
