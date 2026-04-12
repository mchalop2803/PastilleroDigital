package activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DetailsProfileActivity extends AppCompatActivity {

    private MaterialToolbar imageButton;
    private ImageView profilePhoto;
    private TextView tvEmail, tvName, tvSurname, tvNif;

    private TextInputLayout tilName, tilSurname, tilNif;
    private TextInputEditText etName, etSurname, etNif;

    private FloatingActionButton editBtn, saveBtn;
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

        editBtn.setOnClickListener(v -> {
            editprofile();
        });
    }

    private void loadComponents() {

        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        tvEmail = findViewById(R.id.tvEmail);

        imageButton = findViewById(R.id.toolbar);
        tvName = findViewById(R.id.tvName);
        tvSurname = findViewById(R.id.tvSurname);
        tvNif = findViewById(R.id.tvNif);

        tilName = findViewById(R.id.tilName);
        tilSurname = findViewById(R.id.tilSurname);
        tilNif = findViewById(R.id.tilNif);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNif = findViewById(R.id.etNif);

        editBtn = findViewById(R.id.fltbtnEdit);
        saveBtn = findViewById(R.id.fltbtnSave);

        profilePhoto = findViewById(R.id.profilePhoto);

        loadData();


    }
    private void loadData(){
        String email = sharedPreferences.getString("email", "");
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String nif = sharedPreferences.getString("nif", "");

        tvEmail.setText(email);
        tvName.setText(name);
        tvSurname.setText(surname);
        tvNif.setText(nif);

        etName.setText(name);
        etSurname.setText(surname);
        etNif.setText(nif);
    }

    private void editprofile(){

        editBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);

        tvName.setVisibility(View.GONE);
        tvSurname.setVisibility(View.GONE);
        tvNif.setVisibility(View.GONE);

        tilName.setVisibility(View.VISIBLE);
        tilSurname.setVisibility(View.VISIBLE);
        tilNif.setVisibility(View.VISIBLE);

        saveBtn.setOnClickListener(v -> {
            saveData();
        });
    }

    private void saveData(){

        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String nif = etNif.getText().toString();

        tvName.setText(name);
        tvSurname.setText(surname);
        tvNif.setText(nif);

        sharedPreferences.edit()
                .putString("name", name)
                .putString("surname", surname)
                .putString("nif", nif)
                .apply();

        tilName.setVisibility(View.GONE);
        tilSurname.setVisibility(View.GONE);
        tilNif.setVisibility(View.GONE);

        tvName.setVisibility(View.VISIBLE);
        tvSurname.setVisibility(View.VISIBLE);
        tvNif.setVisibility(View.VISIBLE);

        saveBtn.setVisibility(View.GONE);
        editBtn.setVisibility(View.VISIBLE);

    }

    private void cancelEdit() {
        // editmode.setText("Edit Profile");
        saveBtn.setVisibility(View.GONE);

        tvName.setEnabled(false);
        tvSurname.setEnabled(false);
        tvNif.setEnabled(false);

        loadData();
    }
}