package activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DetailsProfileActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ImageView profilePhoto;
    private ShapeableImageView editmode, saveProfile;
    private TextInputLayout nameLayout, passwordLayout, surnameLayout, nifLayout;
    private TextInputEditText nameInput, passwordInput, surnameInput, nifInput;
    private TextView email;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void loadComponents() {

        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        editmode = findViewById(R.id.btnEditProfile);
        saveProfile = findViewById(R.id.btnSaveProfile);
        imageButton = findViewById(R.id.imageButton);

        nameLayout = findViewById(R.id.etNameProfile);
        passwordLayout = findViewById(R.id.etPasswordProfile);
        surnameLayout = findViewById(R.id.etSurnameProfile);
        nifLayout = findViewById(R.id.etNifProfile);

        nameInput = findViewById(R.id.inputNameProfile);
        passwordInput = findViewById(R.id.inputPasswordProfile);
        surnameInput = findViewById(R.id.inputSurnameProfile);
        nifInput = findViewById(R.id.inputNifProfile);

        profilePhoto = findViewById(R.id.profilePhoto);
        email = findViewById(R.id.tvEmailProfile);

        loadData();

        editmode.setOnClickListener(v -> editprofile());

        saveProfile.setOnClickListener(v -> saveData());

    }
    private void loadData(){
        nameInput.setText(sharedPreferences.getString("name", ""));
        surnameInput.setText(sharedPreferences.getString("surname", ""));
        nifInput.setText(sharedPreferences.getString("nif", ""));
        email.setText(sharedPreferences.getString("email", ""));
        passwordInput.setText(sharedPreferences.getString("password", ""));
    }

    private void editprofile(){

        saveProfile.setVisibility(View.VISIBLE);
        editmode.setVisibility(View.GONE);
        nameInput.setEnabled(true);
        surnameInput.setEnabled(true);
        nifInput.setEnabled(true);
        passwordInput.setEnabled(true);
    }

    private void saveData(){

        nameInput.setEnabled(false);
        surnameInput.setEnabled(false);
        nifInput.setEnabled(false);
        email.setEnabled(false);
        passwordInput.setEnabled(false);

    }

    private void cancelEdit() {
        // editmode.setText("Edit Profile");
        saveProfile.setVisibility(View.GONE);

        nameInput.setEnabled(false);
        surnameInput.setEnabled(false);
        nifInput.setEnabled(false);
        email.setEnabled(false);
        passwordInput.setEnabled(false);

        loadData();
    }
}