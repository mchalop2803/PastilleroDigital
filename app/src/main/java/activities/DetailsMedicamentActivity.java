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

import models.Alerta;
import models.Medicamento;

public class DetailsMedicamentActivity extends AppCompatActivity {

    private ImageButton imageButton, imgBtnMedicament;
    private TextView tvMedicamentName, tvMedicamentAmount, tvMedicamentTime;
    private Button btnAddAlert, btnDeleteMedicament;

    private Medicamento medicamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_medicament);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        medicamento = (Medicamento) getIntent().getSerializableExtra("medicament");

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsMedicamentActivity.this, ListMedicamentActivity.class);
            startActivity(intent);
        });


        btnAddAlert.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsMedicamentActivity.this, AddAlertaActivity.class);
            startActivity(intent);
        });

        tvMedicamentName.setText("Medicamento: " + medicamento.getNombre());
        tvMedicamentAmount.setText("Dosis: " + medicamento.getDosis());
        tvMedicamentTime.setText("Hora: " + medicamento.getHorario());

        btnDeleteMedicament.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("medicament")
                    .child(medicamento.getId());
            ref.removeValue()
                    .addOnSuccessListener(d -> {
                        Toast.makeText(this, "Medicament deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(f ->
                            Toast.makeText(this, "Medicament deleted failed", Toast.LENGTH_SHORT).show());

        });
    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);
        imgBtnMedicament = findViewById(R.id.imgBtnMedicament);

        btnAddAlert = findViewById(R.id.btnAddAlert);
        btnDeleteMedicament = findViewById(R.id.btnDeleteMedicament);

        tvMedicamentName = findViewById(R.id.tvMedicamentName);
        tvMedicamentAmount = findViewById(R.id.tvMedicamentAmount);
        tvMedicamentTime = findViewById(R.id.tvMedicamentTime);
    }
}