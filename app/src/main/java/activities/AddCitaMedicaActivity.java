package activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import models.CitaMedica;
import services.CitaMedicaService;

public class AddCitaMedicaActivity extends AppCompatActivity {

    private TextInputEditText etDesc, etComp, etDate, etTime, etLoc, etMed;
    private MaterialButton btnSave;
    private CitaMedicaService service;
    private CitaMedica pendingCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_cita_medica);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        init();

        btnSave.setOnClickListener(v -> saveCita());
    }

    private void saveCita() {

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        String userId = prefs.getString("id", null);

        if (userId == null) {
            Toast.makeText(this, "Usuario no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        CitaMedica c = new CitaMedica();

        c.setUserId(userId);
        c.setDescription(etDesc.getText().toString());
        c.setAcompañante(etComp.getText().toString());
        c.setFecha(etDate.getText().toString());
        c.setHora(etTime.getText().toString());
        c.setMedico(etMed.getText().toString());
        c.setLocation(etLoc.getText().toString());

        if (c.getDescription().isEmpty() || c.getFecha().isEmpty() || c.getHora().isEmpty()) {
            Toast.makeText(this, "Campos vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        pendingCita = c;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR}, 100);
            return;
        }

        service.insertCitaMedica(c);

        createEventInCalendar(c);

    }

    private void saveAndCreate(CitaMedica c) {
        service.insertCitaMedica(c);
        createEventInCalendar(c);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (pendingCita != null) {
                saveAndCreate(pendingCita);
            }
        }
    }

    private void createEventInCalendar(CitaMedica c) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = sdf.parse(c.getFecha() + " " + c.getHora());

            if (date == null) return;

            long start = date.getTime();
            long end = start + 3600000;

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, c.getDescription())
                    .putExtra(CalendarContract.Events.DESCRIPTION, c.getMedico())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, c.getLocation())
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error abriendo calendario", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String date = String.format("%02d/%02d/%d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear);

                    etDate.setText(date);
                },
                year, month, day
        );

        datePicker.show();
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

                    etTime.setText(time);
                },
                hour,
                minute,
                true
        );

        timePicker.show();
    }


    private void init() {

        etDesc = findViewById(R.id.etDescription);
        etComp = findViewById(R.id.etCompanion);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etLoc = findViewById(R.id.etLocation);
        etMed = findViewById(R.id.etMedic);

        etTime.setOnClickListener(v -> showTimePicker());

        btnSave = findViewById(R.id.btnSave);
        service = new CitaMedicaService(getApplicationContext());
        etDate.setOnClickListener(v -> showDatePicker());
    }
}