package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.app.DatePickerDialog;

import java.util.Calendar;

import models.Medicamento;
import services.MedicamentoService;

public class AddMedicamentActivity extends AppCompatActivity {

    private ImageButton imageButton;

    private TextInputEditText textInputEditTextMedicamentName,
            textInputEditTextMedicamentDescripcion, etFechaInicio, etFechaFin;

    private long fechaInicioMillis = 0;
    private long fechaFinMillis = 0;

    private boolean editMode;
    private boolean isSaving = false;

    private Medicamento medicamentoEdit;

    private Button btnSave;

    private MedicamentoService medicamentoService;

    public static final String EXTRA_MEDICAMENTO = "medicaments";

    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private ImageView imgMedicament;

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

        etFechaInicio.setOnClickListener(v -> showDatePicker(true));

        etFechaFin.setOnClickListener(v -> showDatePicker(false));

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
            startActivity(intent);
            finish();
        });

        btnSave.setOnClickListener(view -> {

            if (isSaving) return;
            isSaving = true;

            if (editMode) {
                updateMedicament();
            } else {
                createMedicament();
            }
        });
    }

    private void showDatePicker(boolean isStart) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    Calendar selected = Calendar.getInstance();

                    selected.set(
                            year,
                            month,
                            dayOfMonth,
                            0,
                            0,
                            0
                    );

                    selected.set(Calendar.MILLISECOND, 0);

                    long millis = selected.getTimeInMillis();

                    String date = String.format(
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );

                    if (isStart) {

                        fechaInicioMillis = millis;

                        etFechaInicio.setText(date);

                        if (fechaFinMillis < fechaInicioMillis) {
                            fechaFinMillis = 0;
                            etFechaFin.setText("");
                        }

                    } else {

                        selected.set(Calendar.HOUR_OF_DAY, 23);
                        selected.set(Calendar.MINUTE, 59);

                        fechaFinMillis = selected.getTimeInMillis();

                        etFechaFin.setText(date);
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        if (!isStart && fechaInicioMillis > 0) {
            datePickerDialog.getDatePicker().setMinDate(fechaInicioMillis);
        }

        datePickerDialog.show();
    }

    private void createMedicament() {

        Medicamento medicamento = new Medicamento();

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        String userId = prefs.getString("id", null);

        medicamento.setUserId(userId);
        medicamento.setNombre(textInputEditTextMedicamentName.getText().toString());
        medicamento.setDescription(textInputEditTextMedicamentDescripcion.getText().toString());
        medicamento.setFechaInicio(fechaInicioMillis);
        medicamento.setFechaFin(fechaFinMillis);

        if (medicamento.getNombre().isBlank() ||
                medicamento.getDescription().isBlank()) {

            Toast.makeText(this, "Campos vacíos", Toast.LENGTH_SHORT).show();
            isSaving = false;
            return;
        }

        medicamentoService.getAllMedicamentosByUser(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {

                    Medicamento m = data.getValue(Medicamento.class);

                    if (m != null &&
                            m.getNombre() != null &&
                            m.getNombre().equalsIgnoreCase(medicamento.getNombre())) {

                        Toast.makeText(AddMedicamentActivity.this,
                                "Ya existe un medicamento con ese nombre",
                                Toast.LENGTH_SHORT).show();

                        isSaving = false;
                        return;
                    }
                }

                saveMedicament(medicamento);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                Toast.makeText(AddMedicamentActivity.this,
                        "Error comprobando medicamentos",
                        Toast.LENGTH_SHORT).show();
                isSaving = false;
            }
        });
    }

    private void saveMedicament(Medicamento medicamento) {

        if (imageUri != null) {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            StorageReference storageRef = FirebaseStorage.getInstance()
                    .getReference("medicaments_images/" + uid + "/" + System.currentTimeMillis() + ".jpg");

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot ->
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                                medicamento.setImageUrl(uri.toString());

                                medicamentoService.insertMedicament(medicamento);

                                Toast.makeText(this,
                                        "Medicamento guardado",
                                        Toast.LENGTH_SHORT).show();

                                goToList();
                                isSaving = false;

                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(this,
                                "Error subiendo imagen",
                                Toast.LENGTH_SHORT).show();
                        isSaving = false;
                    });

        } else {

            medicamentoService.insertMedicament(medicamento);

            Toast.makeText(this,
                    "Medicamento guardado",
                    Toast.LENGTH_SHORT).show();

            goToList();
            isSaving = false;
        }
    }

    private void updateMedicament() {

        medicamentoEdit.setNombre(textInputEditTextMedicamentName.getText().toString());
        medicamentoEdit.setDescription(textInputEditTextMedicamentDescripcion.getText().toString());

        if (imageUri != null) {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            StorageReference storageRef = FirebaseStorage.getInstance()
                    .getReference("medicaments_images/" + uid + "/" + System.currentTimeMillis() + ".jpg");

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot ->
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                                medicamentoEdit.setImageUrl(uri.toString());

                                medicamentoService.updateMedicament(medicamentoEdit);

                                Toast.makeText(this, "Medicamento actualizado", Toast.LENGTH_SHORT).show();

                                goToList();
                            }))
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al actualizar imagen", Toast.LENGTH_SHORT).show()
                    );

        } else {

            medicamentoService.updateMedicament(medicamentoEdit);

            Toast.makeText(this, "Medicamento actualizado", Toast.LENGTH_SHORT).show();

            goToList();
        }

        isSaving = false;
    }

    private void goToList() {
        Intent intent = new Intent(this, ListMedicamentActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();
            imgMedicament.setImageURI(imageUri);
        }
    }

    private void loadComponents() {

        imageButton = findViewById(R.id.imageButton);

        textInputEditTextMedicamentName = findViewById(R.id.etName);
        textInputEditTextMedicamentDescripcion = findViewById(R.id.etDescripcion);

        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);

        btnSave = findViewById(R.id.btnSave);

        imgMedicament = findViewById(R.id.imgMedicament);

        medicamentoService = new MedicamentoService(getApplicationContext());

        Intent intent = getIntent();

        editMode = intent.getBooleanExtra("editMode", false);

        if (intent.getSerializableExtra(EXTRA_MEDICAMENTO) != null) {

            medicamentoEdit =
                    (Medicamento) intent.getSerializableExtra(EXTRA_MEDICAMENTO);

            textInputEditTextMedicamentName.setText(medicamentoEdit.getNombre());
            textInputEditTextMedicamentDescripcion.setText(medicamentoEdit.getDescription());

            if (medicamentoEdit.getImageUrl() != null &&
                    !medicamentoEdit.getImageUrl().isEmpty()) {

                Glide.with(this)
                        .load(medicamentoEdit.getImageUrl())
                        .placeholder(R.drawable.ic_pastillero)
                        .into(imgMedicament);
            }
        }

        imgMedicament.setOnClickListener(v -> {
            Intent inte = new Intent(Intent.ACTION_PICK);
            inte.setType("image/*");
            startActivityForResult(inte, PICK_IMAGE);
        });
    }
}