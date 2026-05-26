package activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import models.Alerta;
import receivers.AlarmReceiver;

public class DetailsAlertaActivity extends AppCompatActivity {

    private static final int MINUTES_TO_MARK_AS_MISSED = 30;

    private MaterialToolbar toolbar;

    private FloatingActionButton fltBtnCheck;

    private TextView tvAlarmTime, tvMedicamentName;

    private Button btnDeleteAlert,
            btnEdit,
            btnTaken,
            btnMissed;

    private Alerta alerta;

    private boolean fromAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_details_alerta);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    v.setPadding(0, 0, 0, 0);
                    return insets;
                });

        alerta = (Alerta) getIntent().getSerializableExtra("alerts");

        fromAlarm = getIntent()
                .getBooleanExtra("fromAlarm", false);

        if (alerta == null) {

            Toast.makeText(
                    this,
                    "Error loading alert",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        loadComponents();

        toolbar.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsAlertaActivity.this, ListAlertActivity.class);
            startActivity(intent);
            finish();
        });


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

            btnEdit.setVisibility(View.GONE);
        }


        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(alerta.getHora());

        String hora = String.format(
                "%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        );

        tvAlarmTime.setText("⏰ " + hora);

        tvMedicamentName.setText("💊 " + alerta.getNombre());


        checkIfAlertIsMissed();


        boolean isPastAlert = alerta.getHora() <= System.currentTimeMillis();

        boolean isAlarmRunning = fromAlarm;

        if (isPastAlert || isAlarmRunning) {

            btnTaken.setEnabled(true);

            btnMissed.setEnabled(true);

        } else {

            btnTaken.setEnabled(false);

            btnMissed.setEnabled(false);
        }

        fltBtnCheck.setOnClickListener(v -> {

            stopAlarm();

            finish();
        });


        btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(
                    this,
                    AddAlertaActivity.class
            );

            intent.putExtra("editMode", true);

            intent.putExtra("alert", alerta);

            startActivity(intent);

            finish();
        });


        btnTaken.setOnClickListener(v -> {

            Calendar cal = Calendar.getInstance();

            String currentHour = String.format(
                    "%02d:%02d",
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE)
            );

            alerta.setEstado("TOMADA");

            alerta.setHoraTomada(currentHour);

            showDoseDialog();
        });


        btnMissed.setOnClickListener(v -> {

            alerta.setEstado("PERDIDA");

            updateAlertStatus();

            Toast.makeText(
                    this,
                    "Alarma registrada como perdida",
                    Toast.LENGTH_SHORT
            ).show();

            stopAlarm();

            finish();
        });


        btnDeleteAlert.setOnClickListener(v -> {

            String uid = FirebaseAuth
                    .getInstance()
                    .getCurrentUser()
                    .getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("alerts")
                    .child(alerta.getId());

            ref.removeValue()

                    .addOnSuccessListener(d -> {

                        Toast.makeText(
                                this,
                                "Alert deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    })

                    .addOnFailureListener(f ->

                            Toast.makeText(
                                    this,
                                    "Delete failed",
                                    Toast.LENGTH_SHORT
                            ).show());
        });
    }


    private void checkIfAlertIsMissed() {


        if ("TOMADA".equals(alerta.getEstado()) ||
                "PERDIDA".equals(alerta.getEstado())) {

            return;
        }

        try {

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(alerta.getHora());

            int alertHour = cal.get(Calendar.HOUR_OF_DAY);
            int alertMinute = cal.get(Calendar.MINUTE);

            Calendar alertCalendar = Calendar.getInstance();

            alertCalendar.set(Calendar.HOUR_OF_DAY, alertHour);

            alertCalendar.set(Calendar.MINUTE, alertMinute);

            alertCalendar.set(Calendar.SECOND, 0);

            alertCalendar.set(Calendar.MILLISECOND, 0);

            long alertTimeMillis =
                    alertCalendar.getTimeInMillis();

            long currentMillis =
                    System.currentTimeMillis();

            long difference =
                    currentMillis - alertTimeMillis;

            long limit =
                    MINUTES_TO_MARK_AS_MISSED * 60 * 1000;

            if (difference > limit) {

                alerta.setEstado("PERDIDA");

                updateAlertStatus();

                Toast.makeText(
                        this,
                        "Esta alarma se ha marcado automáticamente como perdida",
                        Toast.LENGTH_LONG
                ).show();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private void showDoseDialog() {

        EditText editText = new EditText(this);

        editText.setHint("Dosis tomada");

        new AlertDialog.Builder(this)

                .setTitle("Registrar dosis")

                .setView(editText)

                .setPositiveButton(
                        "Guardar",
                        (dialog, which) -> {

                            String dosis =
                                    editText.getText().toString();

                            alerta.setDosisTomada(dosis);

                            updateAlertStatus();

                            Toast.makeText(
                                    this,
                                    "Toma registrada",
                                    Toast.LENGTH_SHORT
                            ).show();

                            stopAlarm();

                            finish();
                        })

                .setNegativeButton(
                        "Cancelar",
                        null
                )

                .show();
    }


    private void updateAlertStatus() {

        String uid = FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .getUid();

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(uid)
                .child("alerts")
                .child(alerta.getId());

        ref.setValue(alerta);
    }


    private void loadComponents() {

        fltBtnCheck = findViewById(R.id.fltBtnCheck);

        btnDeleteAlert = findViewById(R.id.btnDelete);

        btnEdit = findViewById(R.id.btnEdit);

        tvAlarmTime = findViewById(R.id.tvTime);

        tvMedicamentName = findViewById(R.id.tvName);

        btnTaken = findViewById(R.id.btnTaken);

        btnMissed = findViewById(R.id.btnMissed);

        toolbar = findViewById(R.id.toolbar);
    }


    private void stopAlarm() {

        if (fromAlarm &&
                AlarmReceiver.mediaPlayer != null) {

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