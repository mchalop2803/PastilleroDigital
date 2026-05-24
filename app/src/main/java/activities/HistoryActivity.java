package activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import models.CitaMedica;
import services.CitaMedicaService;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private MaterialCalendarView calendarView;

    private List<CitaMedica> citas = new ArrayList<>();
    private Set<CalendarDay> diasConCitas = new HashSet<>();

    private CitaMedicaService citaService;
    private String userId;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        userId = prefs.getString("id", null);

        citaService = new CitaMedicaService(getApplicationContext());

        imageButton = findViewById(R.id.imageButton);
        calendarView = findViewById(R.id.calendarView);

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        loadAllCitas();

        calendarView.setOnDateChangedListener((widget, date, selected) -> {

            CalendarDay clickedDay = CalendarDay.from(
                    date.getYear(),
                    date.getMonth(),
                    date.getDay()
            );

            for (CitaMedica c : citas) {

                CalendarDay citaDay = parseToDay(c.getFecha());

                if (citaDay != null && citaDay.equals(clickedDay)) {
                    showDialog(c);
                    return;
                }
            }

            Toast.makeText(this,
                    "No hay citas ese día",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void loadAllCitas() {

        citaService.getAllCitasByUser(userId, new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                citas.clear();
                diasConCitas.clear();

                for (DataSnapshot data : snapshot.getChildren()) {

                    CitaMedica cita = data.getValue(CitaMedica.class);

                    if (cita == null || cita.getFecha() == null) continue;

                    citas.add(cita);

                    CalendarDay day = parseToDay(cita.getFecha());
                    if (day != null) diasConCitas.add(day);
                }

                calendarView.removeDecorators();
                calendarView.addDecorator(new EventDecorator());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(HistoryActivity.this,
                        "Error cargando citas",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CalendarDay parseToDay(String fecha) {
        try {
            Date d = sdf.parse(fecha);
            if (d == null) return null;

            Calendar cal = Calendar.getInstance();
            cal.setTime(d);

            return CalendarDay.from(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            );

        } catch (Exception e) {
            return null;
        }
    }

    private void showDialog(CitaMedica c) {

        new AlertDialog.Builder(this)
                .setTitle("Cita médica")
                .setMessage(
                        "Fecha: " + c.getFecha() + "\n\n" +
                                "Hora: " + c.getHora() + "\n\n" +
                                "Motivo: " + c.getDescription()
                )
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private class EventDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return diasConCitas.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(8, Color.BLUE));
        }
    }
}