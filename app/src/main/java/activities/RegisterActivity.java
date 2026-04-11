package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import models.User;
import services.UserService;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etEmailRegister, etPasswdRegister;
    private EditText etNameRegister, etSurnameRegister;
    private EditText etNif, etPhone, etAge;
    private Button btnRegister;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initServices();
        initViews();
    }

    private void initServices() {
        userService = new UserService(this);
    }

    private void initViews() {
        TextInputLayout tilEmailRegister = findViewById(R.id.etEmailRegister);
        TextInputLayout tilPasswdRegister = findViewById(R.id.etpasswdRegister);

        etEmailRegister = (TextInputEditText) tilEmailRegister.getEditText();
        etPasswdRegister = (TextInputEditText) tilPasswdRegister.getEditText();
        etNameRegister = findViewById(R.id.etNameRegister);
        etSurnameRegister = findViewById(R.id.etSurnameRegister);
        etNif = findViewById(R.id.etNif);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        etAge = findViewById(R.id.etAge);

        btnRegister.setOnClickListener(v -> {
            String email = etEmailRegister.getText().toString().trim();
            String password = etPasswdRegister.getText().toString().trim();
            String name = etNameRegister.getText().toString().trim();
            String surname = etSurnameRegister.getText().toString().trim();
            String nif = etNif.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || nif.isEmpty() || phone.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth uAuth = FirebaseAuth.getInstance();

            uAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String uid = uAuth.getCurrentUser().getUid();

                    User user = new User();
                    user.setAge(Integer.valueOf(etAge.getText().toString()));
                    user.setPhone(etPhone.getText().toString());
                    user.setName(name);
                    user.setSurname(surname);
                    user.setId(uid);
                    user.setNif(nif);

                    userService.insertUser(user);

                    getSharedPreferences("Prefs", MODE_PRIVATE)
                            .edit()
                            .putString("id", uid)
                            .putString("name", name)
                            .putString("surname", surname)
                            .putString("nif", nif)
                            .putString("email", email)
                            .putString("password", password)
                            .putBoolean("isLogged", true)
                            .apply();

                    Toast.makeText(RegisterActivity.this, "Registro exitoso del ususario", Toast.LENGTH_SHORT).show();

                    Intent intMain = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intMain);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            });
        });

        Toast.makeText(RegisterActivity.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
    }
}