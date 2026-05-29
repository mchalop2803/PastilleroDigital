package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pastillerodigital.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import models.Alerta;
import models.Medicamento;
import services.AlertService;
import services.MedicamentoService;

public class DetailsMedicamentActivity extends AppCompatActivity {

    private Medicamento medicamento;

    private ImageButton imageButton;
    private TextView tvMedicamentName, tvDescription;
    private Button btnAddAlert, btnDeleteMedicament, btnEdit;
    private ImageView imgMedicament;

    public static final String EXTRA_MEDICAMENTO = "medicaments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_medicament);

        medicamento = (Medicamento)
                getIntent().getSerializableExtra(EXTRA_MEDICAMENTO);

        if (medicamento == null) {
            Toast.makeText(this,
                    "Error cargando medicamento",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadComponents();

        tvMedicamentName.setText(medicamento.getNombre());
        tvDescription.setText(medicamento.getDescription());

        if (medicamento.getImageUrl() != null) {
            Glide.with(this)
                    .load(medicamento.getImageUrl())
                    .into(imgMedicament);
        }

        imageButton.setOnClickListener(v -> finish());

        btnAddAlert.setOnClickListener(v -> {
            Intent i = new Intent(this, AddAlertaActivity.class);
            i.putExtra(EXTRA_MEDICAMENTO, medicamento);
            startActivity(i);
        });

        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, AddMedicamentActivity.class);
            i.putExtra(EXTRA_MEDICAMENTO, medicamento);
            i.putExtra("editMode", true);
            startActivity(i);
        });

        btnDeleteMedicament.setOnClickListener(v -> {

            if (medicamento == null || medicamento.getId() == null) {
                return;
            }

            long now = System.currentTimeMillis();

            AlertService alertService =
                    new AlertService(getApplicationContext());

            MedicamentoService medicamentoService =
                    new MedicamentoService(getApplicationContext());

            alertService.getAlertsByMedicamentoId(
                    medicamento.getId(),
                    alerts -> {

                        for (Alerta a : alerts) {

                            if (a.getHora() >= now) {
                                alertService.deleteAlert(a.getId());
                            }
                        }

                        medicamentoService.deleteMedicament(medicamento.getId());

                        finish();
                    }
            );
        });
    }

    private void loadComponents() {
        imageButton = findViewById(R.id.imageButton);
        tvMedicamentName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        btnAddAlert = findViewById(R.id.btnAddAlert);
        btnDeleteMedicament = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        imgMedicament = findViewById(R.id.imgMedicament);
    }
}