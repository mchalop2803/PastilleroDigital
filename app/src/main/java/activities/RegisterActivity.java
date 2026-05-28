package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

import models.User;
import services.UserService;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etEmailRegister, etPasswdRegister;
    private TextInputEditText etNameRegister, etSurnameRegister;
    private TextInputEditText etNif, etPhone, etAge;

    private TextInputLayout tilEmail, tilPassword,
            tilName, tilSurname,
            tilAge, tilNif, tilPhone;

    private Button btnRegister;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_register);

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
        etAge = findViewById(R.id.etAge);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        tilName = findViewById(R.id.tilName);
        tilSurname = findViewById(R.id.tilSurname);

        tilAge = findViewById(R.id.tilAge);
        tilNif = findViewById(R.id.tilNif);
        tilPhone = findViewById(R.id.tilPhone);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {

            String email = etEmailRegister.getText().toString().trim();
            String password = etPasswdRegister.getText().toString().trim();
            String name = etNameRegister.getText().toString().trim();
            String surname = etSurnameRegister.getText().toString().trim();
            String nif = etNif.getText().toString().trim();

            if (!validateFields()) {
                return;
            }

            FirebaseAuth uAuth = FirebaseAuth.getInstance();

            uAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            String uid = uAuth.getCurrentUser().getUid();

                            User user = new User();

                            user.setAge(
                                    Integer.valueOf(
                                            etAge.getText().toString()
                                    )
                            );

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

                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Registro exitoso del usuario",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intMain = new Intent(
                                    RegisterActivity.this,
                                    MainActivity.class
                            );

                            startActivity(intMain);

                            finish();

                        } else {

                            try {

                                throw task.getException();

                            } catch (FirebaseAuthUserCollisionException e) {

                                showError(
                                        tilEmail,
                                        "El email ya está en uso"
                                );

                                etEmailRegister.requestFocus();

                            } catch (Exception e) {

                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Error al registrar usuario",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });
        });
    }

    private void showError(TextInputLayout layout, String message) {

        layout.setError(message);

        if (layout == tilPassword) {

            layout.setEndIconMode(
                    TextInputLayout.END_ICON_NONE
            );
        }
    }

    private void clearError(TextInputLayout layout) {

        layout.setError(null);

        if (layout == tilPassword) {

            layout.setEndIconMode(
                    TextInputLayout.END_ICON_PASSWORD_TOGGLE
            );
        }
    }

    private boolean validateFields() {

        clearError(tilName);
        clearError(tilSurname);
        clearError(tilEmail);
        clearError(tilPassword);
        clearError(tilNif);
        clearError(tilPhone);
        clearError(tilAge);

        String name = etNameRegister.getText().toString().trim();

        String surname = etSurnameRegister.getText().toString().trim();

        String email = etEmailRegister.getText().toString().trim();

        String password = etPasswdRegister.getText().toString().trim();

        String nif = etNif.getText().toString().trim();

        String phone = etPhone.getText().toString().trim();

        String age = etAge.getText().toString().trim();

        if (name.isEmpty()) {

            showError(
                    tilName,
                    "El nombre es obligatorio"
            );

            etNameRegister.requestFocus();

            return false;
        }

        if (!Pattern.matches(
                "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
                name
        )) {

            showError(
                    tilName,
                    "El nombre solo puede contener letras"
            );

            etNameRegister.requestFocus();

            return false;
        }

        if (name.length() > 30) {

            showError(
                    tilName,
                    "Máximo 30 caracteres"
            );

            etNameRegister.requestFocus();

            return false;
        }

        if (surname.isEmpty()) {

            showError(
                    tilSurname,
                    "Los apellidos son obligatorios"
            );

            etSurnameRegister.requestFocus();

            return false;
        }

        if (!Pattern.matches(
                "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
                surname
        )) {

            showError(
                    tilSurname,
                    "Los apellidos solo pueden contener letras"
            );

            etSurnameRegister.requestFocus();

            return false;
        }

        if (email.isEmpty()) {

            showError(
                    tilEmail,
                    "El email es obligatorio"
            );

            etEmailRegister.requestFocus();

            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            showError(
                    tilEmail,
                    "Introduce un email válido"
            );

            etEmailRegister.requestFocus();

            return false;
        }

        if (password.isEmpty()) {

            showError(
                    tilPassword,
                    "La contraseña es obligatoria"
            );

            etPasswdRegister.requestFocus();

            return false;
        }

        if (password.length() < 6) {

            showError(
                    tilPassword,
                    "Mínimo 6 caracteres"
            );

            etPasswdRegister.requestFocus();

            return false;
        }

        if (!password.matches(".*[A-Za-z].*") ||
                !password.matches(".*\\d.*")) {

            showError(
                    tilPassword,
                    "Debe contener letras y números"
            );

            etPasswdRegister.requestFocus();

            return false;
        }

        if (nif.isEmpty()) {

            showError(
                    tilNif,
                    "El DNI es obligatorio"
            );

            etNif.requestFocus();

            return false;
        }

        if (!Pattern.matches(
                "^[0-9]{8}[A-Za-z]$",
                nif
        )) {

            showError(
                    tilNif,
                    "Formato DNI incorrecto (12345678A)"
            );

            etNif.requestFocus();

            return false;
        }

        if (phone.isEmpty()) {

            showError(
                    tilPhone,
                    "El teléfono es obligatorio"
            );

            etPhone.requestFocus();

            return false;
        }

        if (!Pattern.matches(
                "^[0-9]{9}$",
                phone
        )) {

            showError(
                    tilPhone,
                    "El teléfono debe tener 9 dígitos"
            );

            etPhone.requestFocus();

            return false;
        }

        if (age.isEmpty()) {

            showError(
                    tilAge,
                    "La edad es obligatoria"
            );

            etAge.requestFocus();

            return false;
        }

        int ageValue = Integer.parseInt(age);

        if (ageValue < 1 || ageValue > 120) {

            showError(
                    tilAge,
                    "Introduce una edad válida"
            );

            etAge.requestFocus();

            return false;
        }

        return true;
    }
}
