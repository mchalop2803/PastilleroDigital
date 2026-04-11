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

    private ImageButton imageButton, imgBtnMedicament;

    private TextInputEditText textInputEditTextMedicamentName, textInputEditTextMedicamentDosis, textInputEditTextMedicamentHorario,
            textInputEditTextMedicamentDuracion;

    private Boolean editMode;

    private Medicamento medicamentoEdit;

    private Button btnSave, btnCancel;

    private MedicamentoService medicamentoService;

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
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    medicamentoEdit.setNombre(textInputEditTextMedicamentName.getText().toString());
                    medicamentoEdit.setDosis(textInputEditTextMedicamentDosis.getText().toString());
                    medicamentoEdit.setHorario(textInputEditTextMedicamentHorario.getText().toString());
                    medicamentoEdit.setDuracion(textInputEditTextMedicamentDuracion.getText().toString());
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
                    if (textInputEditTextMedicamentDuracion.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Duracion is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    medicamentoService.updateMedicament(medicamentoEdit);
                    Toast.makeText(AddMedicamentActivity.this, "Medicament updated", Toast.LENGTH_SHORT).show();
                    Log.i("Medicament id", medicamentoEdit.getId());

                    Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
                    startActivity(intent);

                } else {

                    Medicamento medicamento = new Medicamento();
                    medicamento.setNombre(textInputEditTextMedicamentName.getText().toString());
                    medicamento.setDosis(textInputEditTextMedicamentDosis.getText().toString());
                    medicamento.setHorario(textInputEditTextMedicamentHorario.getText().toString());
                    medicamento.setDuracion(textInputEditTextMedicamentDuracion.getText().toString());
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

                    if (textInputEditTextMedicamentDuracion.getText().toString().isBlank()) {
                        Toast.makeText(AddMedicamentActivity.this, "Duracion is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String idMedicament = medicamentoService.insertMedicament(medicamento);

                    Toast.makeText(AddMedicamentActivity.this,
                            "Medicamento with id " + idMedicament + " inserted",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
            startActivity(intent);
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
        imgBtnMedicament = findViewById(R.id.imgBtnMedicament);

        textInputEditTextMedicamentName = findViewById(R.id.textInputEditTextMedicamentName);
        textInputEditTextMedicamentDosis = findViewById(R.id.textInputEditTextMedicamentDosis);
        textInputEditTextMedicamentHorario = findViewById(R.id.textInputEditTextMedicamentHorario);
        textInputEditTextMedicamentDuracion = findViewById(R.id.textInputEditTextMedicamentDuracion);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        medicamentoService = new MedicamentoService(getApplicationContext());

        Intent intent = getIntent();
        if(intent.getSerializableExtra("medicament") != null){
            medicamentoEdit = (Medicamento) intent.getSerializableExtra("medicament");
            textInputEditTextMedicamentName.setText(medicamentoEdit.getNombre().toString());
            textInputEditTextMedicamentDosis.setText(medicamentoEdit.getDosis().toString());
            textInputEditTextMedicamentHorario.setText(medicamentoEdit.getHorario().toString());
            textInputEditTextMedicamentDuracion.setText(medicamentoEdit.getDuracion().toString());

        }

        editMode = intent.getBooleanExtra("editMode", false);
    }
}