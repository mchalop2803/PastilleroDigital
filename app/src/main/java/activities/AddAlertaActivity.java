package activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import receivers.AlarmReceiver;
import models.Alerta;
import models.Medicamento;
import services.AlertService;

public class AddAlertaActivity extends AppCompatActivity {

    private ImageButton imgBtnBack;
    private TextInputEditText textInputEditTextAlertName, textInputEditTextAlertTime;
    private Button btnAddAlert;
    private ImageView imgAlert;

    private Boolean editMode;

    private Alerta alertEdit;

    private AlertService alertService;

    private Medicamento medicamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_alerta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imgBtnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddAlertaActivity.this, DetailsAlertaActivity.class);
            startActivity(intent);
            finish();
        });


        btnAddAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    alertEdit.setNombre(textInputEditTextAlertName.getText().toString());
                    alertEdit.setHora(textInputEditTextAlertTime.getText().toString());

                    if (textInputEditTextAlertName.getText().toString().isBlank()) {
                        Toast.makeText(AddAlertaActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (textInputEditTextAlertTime.getText().toString().isBlank()) {
                        Toast.makeText(AddAlertaActivity.this, "Time is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    alertService.updateAlert(alertEdit);
                    Toast.makeText(AddAlertaActivity.this, "Alert updated", Toast.LENGTH_SHORT).show();
                    Log.i("Alert id", alertEdit.getId());

                    Intent intent = new Intent(AddAlertaActivity.this, ListAlertActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Alerta alerta = new Alerta();
                    SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
                    String userId = prefs.getString("id", null);

                    alerta.setUserId(userId);
                    alerta.setMedicamentoId(medicamento.getId());
                    alerta.setNombre(textInputEditTextAlertName.getText().toString());
                    alerta.setHora(textInputEditTextAlertTime.getText().toString());
                    if (medicamento != null) {
                        alerta.setMedicamentoId(medicamento.getId());
                    }


                    if (textInputEditTextAlertName.getText().toString().isBlank()) {
                        Toast.makeText(AddAlertaActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (textInputEditTextAlertTime.getText().toString().isBlank()) {
                        Toast.makeText(AddAlertaActivity.this, "Time is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String idAlert = alertService.insertAlert(alerta);
                    Toast.makeText(AddAlertaActivity.this, "Alert with id " + idAlert + " inserted", Toast.LENGTH_SHORT).show();
                    Log.i("Alert id", idAlert);
                    scheduleAlarm(alerta);

                    Intent intent = new Intent(AddAlertaActivity.this, DetailsMedicamentActivity.class);
                    intent.putExtra(DetailsMedicamentActivity.EXTRA_MEDICAMENTO, medicamento);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void scheduleAlarm(Alerta alerta) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alerts", alerta);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                alerta.getId().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String[] parts = alerta.getHora().split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                Intent intentPerm = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intentPerm);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }

    private void showTimePicker() {

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {

                    String time = String.format("%02d:%02d",
                            selectedHour,
                            selectedMinute);

                    textInputEditTextAlertTime.setText(time);
                },
                hour,
                minute,
                true
        );

        timePicker.show();
    }

    private void loadComponents(){
        textInputEditTextAlertName = findViewById(R.id.etAlertName);
        textInputEditTextAlertTime = findViewById(R.id.etAlertTime);

        imgBtnBack = findViewById(R.id.btnBack);
        imgAlert = findViewById(R.id.imgAlert);
        btnAddAlert = findViewById(R.id.btnAddAlert);

        textInputEditTextAlertTime.setOnClickListener(v -> showTimePicker());

        alertService = new AlertService(getApplicationContext());

        Intent intent = getIntent();
        if(intent.getSerializableExtra("alert") != null){
            alertEdit = (Alerta) intent.getSerializableExtra("alert");
            textInputEditTextAlertName.setText(alertEdit.getNombre().toString());
            textInputEditTextAlertTime.setText(alertEdit.getHora().toString());

        }

        if (intent.getSerializableExtra(DetailsMedicamentActivity.EXTRA_MEDICAMENTO) != null) {
            medicamento = (Medicamento) getIntent()
                    .getSerializableExtra(DetailsMedicamentActivity.EXTRA_MEDICAMENTO);
            if (medicamento != null && medicamento.getImageUrl() != null) {
                Glide.with(this)
                        .load(medicamento.getImageUrl())
                        .placeholder(R.drawable.ic_pastillero)
                        .into(imgAlert);
            } else {
                imgAlert.setImageResource(R.drawable.ic_pastillero);
            }
        }

        editMode = intent.getBooleanExtra("editMode", false);
    }
}