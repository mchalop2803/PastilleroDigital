package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Familiar;

public class DetailsFamilyActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextView tvName, tvEmail;
    private Button btnDeleteFamily;

    private Familiar familiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_family);

        familiar = (Familiar) getIntent().getSerializableExtra("family");

        loadComponents();

        imageButton.setOnClickListener(v -> finish());

        if (familiar != null) {
            tvName.setText(familiar.getName() != null ? familiar.getName() : "");
            tvEmail.setText(familiar.getEmail() != null ? familiar.getEmail() : "");
        }

        btnDeleteFamily.setOnClickListener(v -> {

            if (familiar == null) return;

            String currentUserId = com.google.firebase.auth.FirebaseAuth
                    .getInstance()
                    .getCurrentUser()
                    .getUid();

            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(currentUserId)
                    .child("family")
                    .child(familiar.getUserId())
                    .removeValue()
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                DetailsFamilyActivity.this,
                                "Familiar quitado",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                DetailsFamilyActivity.this,
                                "Error al quitar familiar",
                                Toast.LENGTH_SHORT
                        ).show();
                    });
        });
    }

    private void loadComponents() {
        imageButton = findViewById(R.id.imageButton);

        btnDeleteFamily = findViewById(R.id.btnDelete);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
    }
}