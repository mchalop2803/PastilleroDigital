package activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.example.pastillerodigital.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.TimeZone;

import models.Alerta;
import models.Medicamento;
import receivers.AlarmReceiver;
import services.AlertService;

public class AddAlertaActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private TextInputEditText etAlertTime;
    private TextInputEditText etDosisBase;

    private Spinner spFrecuencia;

    private Button btnAddAlert;

    private ImageView imgAlert;

    private Medicamento medicamento;

    private AlertService alertService;

    private Calendar selectedTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_add_alerta);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> insets
        );

        loadComponents();
        loadMedicamentoData();
        loadSpinner();

        toolbar.setOnClickListener(v -> {
            Intent intent = new Intent(AddAlertaActivity.this, DetailsMedicamentActivity.class);
            intent.putExtra("medicaments", medicamento);
            startActivity(intent);
            finish();
        });

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                }
        );

        etAlertTime.setOnClickListener(v -> showTimePicker());

        btnAddAlert.setOnClickListener(v -> createAlerts());
    }

    private void createAlerts() {

        if (medicamento == null) {
            Toast.makeText(this, "Medicamento no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etAlertTime.getText() == null ||
                etAlertTime.getText().toString().isEmpty()) {

            Toast.makeText(this,
                    "Selecciona una hora",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        SharedPreferences prefs =
                getSharedPreferences("Prefs", MODE_PRIVATE);

        String frecuencia =
                spFrecuencia.getSelectedItem().toString();

        int intervalHours =
                getIntervalHours(frecuencia);

        Calendar inicio = Calendar.getInstance();

        inicio.setTimeInMillis(medicamento.getFechaInicio());

        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);

        Calendar fin = Calendar.getInstance();

        fin.setTimeInMillis(medicamento.getFechaFin());

        fin.set(Calendar.HOUR_OF_DAY, 23);
        fin.set(Calendar.MINUTE, 59);
        fin.set(Calendar.SECOND, 59);
        fin.set(Calendar.MILLISECOND, 999);

        Calendar actual =
                (Calendar) inicio.clone();

        while (!actual.after(fin)) {

            Calendar alarmaTime =
                    (Calendar) actual.clone();

            alarmaTime.set(
                    Calendar.HOUR_OF_DAY,
                    selectedTime.get(Calendar.HOUR_OF_DAY)
            );

            alarmaTime.set(
                    Calendar.MINUTE,
                    selectedTime.get(Calendar.MINUTE)
            );

            alarmaTime.set(Calendar.SECOND, 0);
            alarmaTime.set(Calendar.MILLISECOND, 0);

            while (alarmaTime.get(Calendar.DAY_OF_MONTH)
                    == actual.get(Calendar.DAY_OF_MONTH)) {

                Alerta alerta = new Alerta();

                alerta.setUserId(
                        prefs.getString("id", null)
                );

                alerta.setNombre(
                        medicamento.getNombre()
                );

                alerta.setMedicamentoId(
                        medicamento.getId()
                );

                alerta.setHora(
                        alarmaTime.getTimeInMillis()
                );

                alerta.setFrecuencia(
                        frecuencia
                );

                alerta.setDosisBase(
                        etDosisBase.getText().toString()
                );

                if (alarmaTime.getTimeInMillis()
                        < System.currentTimeMillis()) {

                    alerta.setEstado("PERDIDA");

                } else {

                    alerta.setEstado("PENDIENTE");
                }

                if (medicamento.getImageUrl() != null) {

                    alerta.setMedicamentImageUrl(
                            medicamento.getImageUrl()
                    );
                }

                alertService.insertAlert(alerta);

                if (alarmaTime.getTimeInMillis()
                        >= System.currentTimeMillis()) {

                    scheduleAlarm(alerta);
                }

                alarmaTime.add(
                        Calendar.HOUR_OF_DAY,
                        intervalHours
                );
            }
            actual.add(Calendar.DAY_OF_MONTH, 1);
        }

        Toast.makeText(this,
                "Alarmas creadas correctamente",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    private int getIntervalHours(String frecuencia) {

        switch (frecuencia) {
            case "Cada 12 horas":
                return 12;
            case "Cada 8 horas":
                return 8;
            case "Cada 6 horas":
                return 6;
            default:
                return 24;
        }
    }

    private void loadSpinner() {

        String[] frecuencias = {
                "Cada 24 horas",
                "Cada 12 horas",
                "Cada 8 horas",
                "Cada 6 horas"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.item_spinner,
                        frecuencias
                );

        adapter.setDropDownViewResource(
                R.layout.item_spinner_dropdown
        );

        spFrecuencia.setAdapter(adapter);
    }

    private void loadMedicamentoData() {

        if (medicamento == null) return;

        if (medicamento.getImageUrl() != null && !medicamento.getImageUrl().isEmpty()) {

            Glide.with(this)
                    .load(medicamento.getImageUrl())
                    .placeholder(R.drawable.ic_pastillero)
                    .into(imgAlert);
        }
    }

    private void showTimePicker() {

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog dialog =
                new TimePickerDialog(
                        this,
                        (view, hour, minute) -> {

                            selectedTime.set(Calendar.HOUR_OF_DAY, hour);
                            selectedTime.set(Calendar.MINUTE, minute);
                            selectedTime.set(Calendar.SECOND, 0);

                            etAlertTime.setText(
                                    String.format("%02d:%02d", hour, minute)
                            );
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                );

        dialog.show();
    }

    private void scheduleAlarm(Alerta alerta) {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alerts", alerta);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this,
                        (int) System.currentTimeMillis(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

        if (alerta.getHora() < System.currentTimeMillis()) return;

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alerta.getHora(),
                pendingIntent
        );
    }

    private void loadComponents() {

        etAlertTime = findViewById(R.id.etAlertTime);
        etDosisBase = findViewById(R.id.etDosisBase);
        spFrecuencia = findViewById(R.id.spFrecuencia);
        toolbar = findViewById(R.id.toolbar);
        imgAlert = findViewById(R.id.imgAlert);
        btnAddAlert = findViewById(R.id.btnAddAlert);

        alertService = new AlertService(getApplicationContext());

        medicamento =
                (Medicamento) getIntent().getSerializableExtra("medicaments");
    }
}