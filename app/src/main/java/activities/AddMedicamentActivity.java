package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;

import models.Familiar;
import models.Medicamento;
import services.MedicamentoService;

public class AddMedicamentActivity extends AppCompatActivity {

    private ImageButton imageButton;

    private TextInputEditText textInputEditTextMedicamentName, textInputEditTextMedicamentDosis, textInputEditTextMedicamentHorario;

    private boolean editMode;

    private Medicamento medicamentoEdit;

    private Button btnSave;

    private MedicamentoService medicamentoService;

    public static final String EXTRA_MEDICAMENTO = "medicaments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medicament);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
            startActivity(intent);
            finish();
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    medicamentoEdit.setNombre(textInputEditTextMedicamentName.getText().toString());
                    medicamentoEdit.setDosis(textInputEditTextMedicamentDosis.getText().toString());
                    medicamentoEdit.setHorario(textInputEditTextMedicamentHorario.getText().toString());
                    medicamentoEdit.setMomentDay(getMomentFromHour(
                            textInputEditTextMedicamentHorario.getText().toString()
                    ));

                    if (textInputEditTextMedicamentName.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (textInputEditTextMedicamentDosis.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Dosis is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (textInputEditTextMedicamentHorario.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Horario is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    medicamentoService.updateMedicament(medicamentoEdit);
                    Toast.makeText(AddMedicamentActivity.this, "Medicament updated", Toast.LENGTH_SHORT).show();
                    Log.i("Medicament id", medicamentoEdit.getId());

                    Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Medicamento medicamento = new Medicamento();
                    medicamento.setNombre(textInputEditTextMedicamentName.getText().toString());
                    medicamento.setDosis(textInputEditTextMedicamentDosis.getText().toString());
                    medicamento.setHorario(textInputEditTextMedicamentHorario.getText().toString());
                    medicamento.setMomentDay(getMomentFromHour(
                            textInputEditTextMedicamentHorario.getText().toString()
                    ));

                    if (textInputEditTextMedicamentName.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (textInputEditTextMedicamentDosis.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Dosis is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (textInputEditTextMedicamentHorario.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Horario is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String idMedicament = medicamentoService.insertMedicament(medicamento);

                    Toast.makeText(AddMedicamentActivity.this,
                            "Medicamento with id " + idMedicament + " inserted",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private String getMomentFromHour(String hora) {
        try {
            String[] parts = hora.split(":");
            int hour = Integer.parseInt(parts[0]);

            if (hour >= 6 && hour < 12) {
                return "DAY";
            } else if (hour >= 12 && hour < 20) {
                return "AFTERNOON";
            } else {
                return "NIGHT";
            }

        } catch (Exception e) {
            return "DAY";
        }
    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);

        textInputEditTextMedicamentName = findViewById(R.id.etName);
        textInputEditTextMedicamentDosis = findViewById(R.id.etDose);
        textInputEditTextMedicamentHorario = findViewById(R.id.etTime);

        btnSave = findViewById(R.id.btnSave);

        medicamentoService = new MedicamentoService(getApplicationContext());

        Intent intent = getIntent();
        if(intent.getSerializableExtra(EXTRA_MEDICAMENTO) != null){
            medicamentoEdit = (Medicamento) intent.getSerializableExtra(EXTRA_MEDICAMENTO);
            textInputEditTextMedicamentName.setText(medicamentoEdit.getNombre());
            textInputEditTextMedicamentDosis.setText(medicamentoEdit.getDosis());
            textInputEditTextMedicamentHorario.setText(medicamentoEdit.getHorario());

        }

        editMode = intent.getBooleanExtra("editMode", false);
    }
}