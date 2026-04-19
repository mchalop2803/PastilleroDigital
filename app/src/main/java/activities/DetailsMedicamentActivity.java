package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pastillerodigital.R;

import services.AlertService;
import services.MedicamentoService;
import models.Medicamento;

public class DetailsMedicamentActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextView tvMedicamentName, tvMedicamentAmount, tvMedicamentTime;
    private Button btnAddAlert, btnDeleteMedicament, btnEdit;

    private Medicamento medicamento;

    public static final String EXTRA_MEDICAMENTO = "medicaments";

    private ImageView imgMedicament;

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

        medicamento = (Medicamento) getIntent().getSerializableExtra(EXTRA_MEDICAMENTO);

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsMedicamentActivity.this, ListMedicamentActivity.class);
            startActivity(intent);
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            Intent intEditMed = new Intent(DetailsMedicamentActivity.this, AddMedicamentActivity.class);
            intEditMed.putExtra("editMode", true);
            intEditMed.putExtra(EXTRA_MEDICAMENTO, medicamento);
            startActivity(intEditMed);
            finish();
        });


        btnAddAlert.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsMedicamentActivity.this, AddAlertaActivity.class);
            intent.putExtra(EXTRA_MEDICAMENTO, medicamento);
            startActivity(intent);
            finish();
        });

        tvMedicamentName.setText("Medicamento: " + medicamento.getNombre());
        tvMedicamentAmount.setText("Dosis: " + medicamento.getDosis());
        tvMedicamentTime.setText("Hora: " + medicamento.getHorario());
        if (medicamento.getImageUrl() != null) {
            Glide.with(this)
                    .load(medicamento.getImageUrl())
                    .placeholder(R.drawable.ic_pastillero)
                    .into(imgMedicament);
        }

        btnDeleteMedicament.setOnClickListener(v -> {

            if (medicamento == null || medicamento.getId() == null) {
                Toast.makeText(this, "Error: medicamento sin ID", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertService alertService = new AlertService(getApplicationContext());
            MedicamentoService medicamentoService = new MedicamentoService(getApplicationContext());

            alertService.deleteAlertsByMedicamentoId(medicamento.getId(), () -> {

                medicamentoService.deleteMedicament(medicamento.getId());

                Toast.makeText(this, "Medicament and alerts deleted", Toast.LENGTH_SHORT).show();
                finish();
            });

        });
    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);
        imgMedicament = findViewById(R.id.imgMedicament);

        btnAddAlert = findViewById(R.id.btnAddAlert);
        btnDeleteMedicament = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        tvMedicamentName = findViewById(R.id.tvName);
        tvMedicamentAmount = findViewById(R.id.tvDose);
        tvMedicamentTime = findViewById(R.id.tvTime);
    }
}