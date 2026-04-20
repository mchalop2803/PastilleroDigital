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

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

                    if (imageUri != null) {

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        StorageReference storageRef = FirebaseStorage.getInstance()
                                .getReference("medicaments_images/" + uid + "/" + medicamentoEdit.getId() + ".jpg");

                        storageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot ->
                                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                                            medicamentoEdit.setImageUrl(uri.toString());
                                            medicamentoService.updateMedicament(medicamentoEdit);

                                            Toast.makeText(AddMedicamentActivity.this, "Medicament updated", Toast.LENGTH_SHORT).show();
                                            goToList();

                                        }))
                                .addOnFailureListener(e ->
                                        Toast.makeText(AddMedicamentActivity.this, "Error subiendo imagen", Toast.LENGTH_SHORT).show());

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
                    SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
                    String userId = prefs.getString("id", null);

                    medicamento.setUserId(userId);
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

                    if (imageUri != null) {

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        StorageReference storageRef = FirebaseStorage.getInstance()
                                .getReference("medicaments_images/" + uid + "/" + System.currentTimeMillis() + ".jpg");

                        storageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot ->
                                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                                            medicamento.setImageUrl(uri.toString());

                                            String idMedicament = medicamentoService.insertMedicament(medicamento);

                                            Toast.makeText(AddMedicamentActivity.this,
                                                    "Medicamento con imagen guardado",
                                                    Toast.LENGTH_SHORT).show();

                                            goToList();

                                        }))
                                .addOnFailureListener(e ->
                                        Toast.makeText(AddMedicamentActivity.this, "Error subiendo imagen", Toast.LENGTH_SHORT).show());

                    } else {

                        Toast.makeText(AddMedicamentActivity.this,
                                "Medicamento guardado",
                                Toast.LENGTH_SHORT).show();

                        goToList();
                    }

                }
            }
        });
    }

    private void goToList() {
        Intent intent = new Intent(AddMedicamentActivity.this, ListMedicamentActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgMedicament.setImageURI(imageUri);
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

        imgMedicament = findViewById(R.id.imgMedicament);

        imgMedicament.setOnClickListener(v -> {
            Intent inte = new Intent(Intent.ACTION_PICK);
            inte.setType("image/*");
            startActivityForResult(inte, PICK_IMAGE);
        });

        editMode = intent.getBooleanExtra("editMode", false);
    }
}