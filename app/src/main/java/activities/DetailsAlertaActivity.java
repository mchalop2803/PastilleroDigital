package activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
            v.setPadding(0, 0, 0, 0);
            return insets;
        });

        alerta = (Alerta) getIntent().getSerializableExtra("alerts");
        fromAlarm = getIntent().getBooleanExtra("fromAlarm", false);

        if (alerta == null) {
            Toast.makeText(this, "Error loading alert", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadComponents();

        // ================= MODO ALARMA =================
        if (fromAlarm) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
            } else {
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                );
            }

            btnEdit.setVisibility(View.GONE); // 👈 ocultar editar solo en alarma
        }

        // ================= DATOS =================
        tvAlarmTime.setText("⏰ " + alerta.getHora());
        tvMedicamentName.setText("💊 " + alerta.getNombre());

        // ================= ACCIONES =================
        imgBtnBack.setOnClickListener(v -> finish());

        fltBtnCheck.setOnClickListener(v -> {
            stopAlarm();
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAlertaActivity.class);
            intent.putExtra("editMode", true);
            intent.putExtra("alert", alerta);
            startActivity(intent);
            finish();
        });

        btnDeleteAlert.setOnClickListener(v -> {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("alerts")
                    .child(alerta.getId());

            ref.removeValue()
                    .addOnSuccessListener(d -> {
                        Toast.makeText(this, "Alert deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(f ->
                            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show());
        });
    }

    private void loadComponents() {
        imgBtnBack = findViewById(R.id.btnBack);
        fltBtnCheck = findViewById(R.id.fltBtnCheck);
        btnDeleteAlert = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        tvAlarmTime = findViewById(R.id.tvTime);
        tvMedicamentName = findViewById(R.id.tvName);
    }

    private void stopAlarm() {
        if (fromAlarm && AlarmReceiver.mediaPlayer != null) {
            AlarmReceiver.mediaPlayer.stop();
            AlarmReceiver.mediaPlayer.release();
            AlarmReceiver.mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }
}