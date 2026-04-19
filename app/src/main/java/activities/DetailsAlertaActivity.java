package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Alerta;
import receivers.AlarmReceiver;

public class DetailsAlertaActivity extends AppCompatActivity {

    private FloatingActionButton fltBtnCheck;
    private ImageButton imgBtnBack;
    private TextView tvAlarmTime, tvMedicamentName;
    private Button btnDeleteAlert, btnEdit;

    private Alerta alerta;
    private boolean fromAlarm = false;

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

        alerta = (Alerta) getIntent().getSerializableExtra("alerts");
        fromAlarm = getIntent().getBooleanExtra("fromAlarm", false);
        if (alerta == null) {
            Toast.makeText(this, "Error cargando alerta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadComponents();

        if (fromAlarm) {
            btnEdit.setVisibility(View.GONE);
        }

        imgBtnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsAlertaActivity.this, DetailsMedicamentActivity.class);
            startActivity(intent);
            finish();
        });

        fltBtnCheck.setOnClickListener(v -> {
            if (fromAlarm && AlarmReceiver.mediaPlayer != null) {
                AlarmReceiver.mediaPlayer.stop();
                AlarmReceiver.mediaPlayer.release();
                AlarmReceiver.mediaPlayer = null;
            }
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            Intent intEditAle = new Intent(DetailsAlertaActivity.this, AddAlertaActivity.class);
            intEditAle.putExtra("editMode", true);
            intEditAle.putExtra("alert", alerta);
            startActivity(intEditAle);
            finish();
        });

        tvAlarmTime.setText("Hora: " + alerta.getHora());
        tvMedicamentName.setText("Medicamento: " + alerta.getNombre());

        btnDeleteAlert.setOnClickListener(v -> {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("alerts")
                    .child(alerta.getId());

            ref.removeValue()
                    .addOnSuccessListener(d -> {
                        Toast.makeText(this, "Alert deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(f ->
                            Toast.makeText(this, "Alert delete failed", Toast.LENGTH_SHORT).show());
        });
    }

    private void loadComponents(){
        imgBtnBack = findViewById(R.id.btnBack);
        fltBtnCheck = findViewById(R.id.fltBtnCheck);

        btnDeleteAlert = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        tvAlarmTime = findViewById(R.id.tvTime);
        tvMedicamentName = findViewById(R.id.tvName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fromAlarm && AlarmReceiver.mediaPlayer != null) {
            AlarmReceiver.mediaPlayer.stop();
            AlarmReceiver.mediaPlayer.release();
            AlarmReceiver.mediaPlayer = null;
        }
    }
}