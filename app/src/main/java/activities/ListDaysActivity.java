package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;
import com.google.firebase.database.*;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.*;

import models.Alerta;

public class ListDaysActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private MaterialCalendarView calendarView;

    private String userId;

    private final Map<CalendarDay, DayInfo> dayMap = new HashMap<>();

    private static class DayInfo {
        boolean alert;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_days);

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        userId = prefs.getString("id", null);

        backBtn = findViewById(R.id.imageButton);
        calendarView = findViewById(R.id.calendarView);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        loadAlerts();

        calendarView.setOnDateChangedListener((widget, date, selected) -> {

            Intent intent = new Intent(this, DayScheduleActivity.class);

            intent.putExtra("day", date.getDay());
            intent.putExtra("month", date.getMonth());
            intent.putExtra("year", date.getYear());

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dayMap.clear();
        calendarView.removeDecorators();
        loadAlerts();
    }

    private void loadAlerts() {

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("alerts")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot d : snapshot.getChildren()) {

                            Alerta alerta = d.getValue(Alerta.class);
                            if (alerta == null) continue;

                            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
                            c.setTimeInMillis(alerta.getHora());

                            CalendarDay day = CalendarDay.from(
                                    c.get(Calendar.YEAR),
                                    c.get(Calendar.MONTH) + 1,
                                    c.get(Calendar.DAY_OF_MONTH)
                            );

                            addEvent(day);
                        }

                        applyDecorators();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void addEvent(CalendarDay day) {
        DayInfo info = dayMap.get(day);

        if (info == null) {
            info = new DayInfo();
            dayMap.put(day, info);
        }

        info.alert = true;
    }

    private void applyDecorators() {

        calendarView.removeDecorators();

        for (Map.Entry<CalendarDay, DayInfo> entry : dayMap.entrySet()) {

            calendarView.addDecorator(
                    new MultiEventDecorator(entry.getKey(), entry.getValue())
            );
        }
    }

    private class MultiEventDecorator implements DayViewDecorator {

        private final CalendarDay day;
        private final DayInfo info;

        public MultiEventDecorator(CalendarDay day, DayInfo info) {
            this.day = day;
            this.info = info;
        }

        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            return calendarDay.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {

            if (info.alert) {
                view.addSpan(new MultiDotSpan(
                        Collections.singletonList(
                                Color.parseColor("#4CAF50")
                        )
                ));
            }
        }
    }
}