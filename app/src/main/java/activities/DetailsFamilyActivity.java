package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Familiar;
import models.Medicamento;

public class DetailsFamilyActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextView tvFamilyName, tvFamilyPhone, tvFamilyRelation;
    private Button btnDeleteFamily, btnEdit;

    private Familiar familiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_family);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        familiar = (Familiar) getIntent().getSerializableExtra("familys");

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsFamilyActivity.this, ListFamilyActivity.class);
            startActivity(intent);
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            Intent intEditFam = new Intent(DetailsFamilyActivity.this, AddFamilyActivity.class);
            intEditFam.putExtra("editMode", true);
            intEditFam.putExtra("family", familiar);
            startActivity(intEditFam);
            finish();
        });

        tvFamilyName.setText("Familiar: " + familiar.getNombre());
        tvFamilyPhone.setText("Teléfono: " + familiar.getPhone());
        tvFamilyRelation.setText("Relación: " + familiar.getRelacion());

        btnDeleteFamily.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("familys")
                    .child(familiar.getId());
            ref.removeValue()
                    .addOnSuccessListener(d -> {
                        Toast.makeText(this, "Family deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(f ->
                            Toast.makeText(this, "Family deleted failed", Toast.LENGTH_SHORT).show());

        });
    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);

        btnDeleteFamily = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        tvFamilyName = findViewById(R.id.tvName);
        tvFamilyPhone = findViewById(R.id.tvPhone);
        tvFamilyRelation = findViewById(R.id.tvRelation);
    }
}