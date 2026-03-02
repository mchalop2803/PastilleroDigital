package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;
import android.widget.Toast;

import com.example.pastillerodigital.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Alerta;

public class DetailsAlertaActivity extends AppCompatActivity {

    private ImageButton imgBtnBack, imgBtnAlarma, imgBtnCheck;
    private TextView tvAlarmTime, tvMedicamentName;
    private Button btnDeleteAlert;

    private Alerta alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_alerta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        alerta = (Alerta) getIntent().getSerializableExtra("alert");

        loadComponents();

        imgBtnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsAlertaActivity.this, DetailsMedicamentActivity.class);
            startActivity(intent);
        });


        imgBtnCheck.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsAlertaActivity.this, DetailsMedicamentActivity.class);
            startActivity(intent);
        });

        tvAlarmTime.setText("Hora: " + alerta.getHora());
        tvMedicamentName.setText("Medicamento: " + alerta.getNombre());

        btnDeleteAlert.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("alert")
                    .child(alerta.getId());
            ref.removeValue()
                    .addOnSuccessListener(d -> {
                        Toast.makeText(this, "Alert deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(f ->
                            Toast.makeText(this, "Alert deleted failed", Toast.LENGTH_SHORT).show());

        });
    }

    private void loadComponents(){
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnAlarma = findViewById(R.id.imgBtnAlarma);
        imgBtnCheck = findViewById(R.id.imgBtnCheck);

        btnDeleteAlert = findViewById(R.id.btnDeleteAlert);

        tvAlarmTime = findViewById(R.id.tvAlarmTime);
        tvMedicamentName = findViewById(R.id.tvMedicamentName);
    }
}