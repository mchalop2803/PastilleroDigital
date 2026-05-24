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

import android.util.Patterns;

import java.util.regex.Pattern;

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

        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswdRegister = findViewById(R.id.etpasswdRegister);

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

            if (!validateFields()) {
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

                    Intent intMain = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intMain);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean validateFields() {

        String name = etNameRegister.getText().toString().trim();

        String surname = etSurnameRegister.getText().toString().trim();

        String email = etEmailRegister.getText().toString().trim();

        String password = etPasswdRegister.getText().toString().trim();

        String nif = etNif.getText().toString().trim();

        String phone = etPhone.getText().toString().trim();

        String age = etAge.getText().toString().trim();


        if (name.isEmpty()) {

            etNameRegister.setError("El nombre es obligatorio");

            etNameRegister.requestFocus();

            return false;
        }

        if (!Pattern.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$", name)) {

            etNameRegister.setError(
                    "El nombre solo puede contener letras"
            );

            etNameRegister.requestFocus();

            return false;
        }

        if (name.length() > 30) {

            etNameRegister.setError(
                    "Máximo 30 caracteres"
            );

            etNameRegister.requestFocus();

            return false;
        }

        if (surname.isEmpty()) {

            etSurnameRegister.setError(
                    "Los apellidos son obligatorios"
            );

            etSurnameRegister.requestFocus();

            return false;
        }

        if (!Pattern.matches(
                "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
                surname
        )) {

            etSurnameRegister.setError(
                    "Los apellidos solo pueden contener letras"
            );

            etSurnameRegister.requestFocus();

            return false;
        }


        if (email.isEmpty()) {

            etEmailRegister.setError(
                    "El email es obligatorio"
            );

            etEmailRegister.requestFocus();

            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmailRegister.setError(
                    "Introduce un email válido"
            );

            etEmailRegister.requestFocus();

            return false;
        }


        if (password.isEmpty()) {

            etPasswdRegister.setError(
                    "La contraseña es obligatoria"
            );

            etPasswdRegister.requestFocus();

            return false;
        }

        if (password.length() < 6) {

            etPasswdRegister.setError(
                    "Mínimo 6 caracteres"
            );

            etPasswdRegister.requestFocus();

            return false;
        }

        if (!password.matches(".*[A-Za-z].*") ||
                !password.matches(".*\\d.*")) {

            etPasswdRegister.setError(
                    "Debe contener letras y números"
            );

            etPasswdRegister.requestFocus();

            return false;
        }


        if (nif.isEmpty()) {

            etNif.setError("El DNI es obligatorio");

            etNif.requestFocus();

            return false;
        }

        if (!Pattern.matches(
                "^[0-9]{8}[A-Za-z]$",
                nif
        )) {

            etNif.setError(
                    "Formato DNI incorrecto (12345678A)"
            );

            etNif.requestFocus();

            return false;
        }


        if (phone.isEmpty()) {

            etPhone.setError(
                    "El teléfono es obligatorio"
            );

            etPhone.requestFocus();

            return false;
        }

        if (!Pattern.matches(
                "^[0-9]{9}$",
                phone
        )) {

            etPhone.setError(
                    "El teléfono debe tener 9 dígitos"
            );

            etPhone.requestFocus();

            return false;
        }


        if (age.isEmpty()) {

            etAge.setError("La edad es obligatoria");

            etAge.requestFocus();

            return false;
        }

        int ageValue = Integer.parseInt(age);

        if (ageValue < 1 || ageValue > 120) {

            etAge.setError(
                    "Introduce una edad válida"
            );

            etAge.requestFocus();

            return false;
        }

        return true;
    }
}