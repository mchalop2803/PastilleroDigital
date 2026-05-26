package activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pastillerodigital.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailsProfileActivity extends AppCompatActivity {

    private MaterialToolbar imageButton;
    private ImageView profilePhoto;
    private TextView tvEmail, tvName, tvSurname, tvNif;

    FloatingActionButton btnSettings;

    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

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

        imageButton.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(DetailsProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(this, v);

            popupMenu.getMenu().add("Añadir familiar");
            popupMenu.getMenu().add("Ver familiares");

            popupMenu.setOnMenuItemClickListener(item -> {

                if (item.getTitle().equals("Añadir familiar")) {

                    startActivity(
                            new Intent(this, AddFamilyActivity.class)
                    );

                    return true;
                }

                if (item.getTitle().equals("Ver familiares")) {

                    startActivity(
                            new Intent(this, ListFamilyActivity.class)
                    );

                    return true;
                }

                return false;
            });

            popupMenu.show();
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

        btnSettings = findViewById(R.id.btnSettings);

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, FamilyMenuActivity.class));
        });

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

        profilePhoto.setOnClickListener(v -> {
            if (saveBtn.getVisibility() == View.VISIBLE) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePhoto.setImageURI(imageUri);
        }
    }

    private void loadData(){
        String email = sharedPreferences.getString("email", "");
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String nif = sharedPreferences.getString("nif", "");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("profileImage")
                .get()
                .addOnSuccessListener(snapshot -> {
                    String url = snapshot.getValue(String.class);

                    if (url != null) {
                        Glide.with(this).load(url).into(profilePhoto);
                    }
                });

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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance()
                    .getReference("profile_images/" + uid + ".jpg");

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot ->
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                                String imageUrl = uri.toString();
                                saveUserData(uid, name, surname, nif, imageUrl);

                            }))
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error subiendo imagen", Toast.LENGTH_SHORT).show());

        } else {
            saveUserData(uid, name, surname, nif, null);
        }
    }

    private void saveUserData(String uid, String name, String surname, String nif, String imageUrl){

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid);

        ref.child("name").setValue(name);
        ref.child("surname").setValue(surname);
        ref.child("nif").setValue(nif);

        if (imageUrl != null) {
            ref.child("profileImage").setValue(imageUrl);
        }

        Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();

        tvName.setText(name);
        tvSurname.setText(surname);
        tvNif.setText(nif);

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