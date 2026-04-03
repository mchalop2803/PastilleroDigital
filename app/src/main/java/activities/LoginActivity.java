package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;

    private SharedPreferences preferences;

    @Override
    protected void onStart() {
        super.onStart();

        preferences = getSharedPreferences("Prefs", MODE_PRIVATE);

        if (FirebaseAuth.getInstance().getCurrentUser() != null && preferences.getBoolean("isLogged", false)) {
            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); // Evita que el usuario vuelva a la pantalla de login con el botón atrás
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadingComponents() {
        Button loginButton = findViewById(R.id.login_button);
        Button googleButton = findViewById(R.id.google_button);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        TextView register = findViewById(R.id.register_text);


        loginButton.setOnClickListener(v -> validateLogin());
        // googleButton.setOnClickListener(v -> signInWithGoogle());

        register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));


    }

    /**
     * Método para validar el inicio de sesión
     */
    private void validateLogin() {
        String email = Objects.requireNonNull(emailEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();
        FirebaseAuth instanceAuth = FirebaseAuth.getInstance();
        // Validación de campos
        if (email.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.password, Toast.LENGTH_SHORT).show();
            return;
        }

        instanceAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = instanceAuth.getCurrentUser().getUid();

                com.google.firebase.database.FirebaseDatabase.getInstance().getReference("users")
                        .child(uid)
                        .get()
                        .addOnSuccessListener(dataSnapshot -> {
                            if (dataSnapshot.exists()) {
                                String name = dataSnapshot.child("name").getValue(String.class);
                                String surname = dataSnapshot.child("surname").getValue(String.class);
                                String nif = dataSnapshot.child("nif").getValue(String.class);

                                // Guardar role e isLogged en SharedPreferences
                                SharedPreferences.Editor editor = getSharedPreferences("Prefs", MODE_PRIVATE).edit();
                                editor.putString("name", name);
                                editor.putString("surname", surname);
                                editor.putString("nif", nif);
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.putBoolean("isLogged", true);
                                editor.putString("id", uid);
                                editor.apply();

                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        });

            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}