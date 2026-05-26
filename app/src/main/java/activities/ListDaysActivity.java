package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.*;

import models.Alerta;
import models.Familiar;

public class ListDaysActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private MaterialCalendarView calendarView;
    private TextView tvCalendarOwner;

    private String userId;
    private String myId;

    private List<Familiar> familyList = new ArrayList<>();
    private final Map<CalendarDay, DayInfo> dayMap = new HashMap<>();

    private static class DayInfo {
        boolean hasPending;
        boolean hasTaken;
        boolean hasMissed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_days);

        backBtn = findViewById(R.id.imageButton);
        calendarView = findViewById(R.id.calendarView);
        tvCalendarOwner = findViewById(R.id.tvCalendarOwner);

        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            userId = myId;
        }

        loadFamily();

        boolean isOwnCalendar = userId.equals(myId);

        tvCalendarOwner.setText(isOwnCalendar
                ? "Mi calendario"
                : "Calendario de familiar");

        tvCalendarOwner.setOnClickListener(v -> showCalendarMenu());

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
            intent.putExtra("userId", userId);

            startActivity(intent);
        });
    }

    // -------------------------
    // MENU DESPLEGABLE
    // -------------------------
    private void showCalendarMenu() {

        PopupMenu menu = new PopupMenu(this, tvCalendarOwner);

        menu.getMenu().add("Mi calendario");

        for (Familiar f : familyList) {
            menu.getMenu().add(f.getName());
        }

        menu.setOnMenuItemClickListener(item -> {

            String title = item.getTitle().toString();

            if (title.equals("Mi calendario")) {

                userId = myId;
                tvCalendarOwner.setText("Mi calendario");

            } else {

                for (Familiar f : familyList) {

                    if (f.getName().equals(title)) {

                        userId = f.getUserId();
                        tvCalendarOwner.setText("Calendario de " + f.getName());
                        break;
                    }
                }
            }

            refreshCalendar();
            return true;
        });

        menu.show();
    }

    private void loadFamily() {

        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                .getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                familyList.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {

                    String ownerId = userSnap.getKey();

                    DataSnapshot familySnap = userSnap.child("family");

                    if (familySnap.hasChild(myId)) {

                        String name = userSnap.child("name").getValue(String.class);

                        Familiar f = new Familiar();
                        f.setUserId(ownerId);
                        f.setOwnerId(ownerId);
                        f.setName(name);

                        familyList.add(f);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCalendar();
    }

    private void refreshCalendar() {

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

                            addEvent(day, alerta.getEstado());
                        }

                        applyDecorators();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void addEvent(CalendarDay day, String estado) {

        DayInfo info = dayMap.get(day);

        if (info == null) {
            info = new DayInfo();
            dayMap.put(day, info);
        }

        if (estado == null) estado = "PENDIENTE";

        switch (estado.toUpperCase()) {

            case "TOMADA":
                info.hasTaken = true;
                break;

            case "PERDIDA":
                info.hasMissed = true;
                break;

            default:
                info.hasPending = true;
                break;
        }
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

            List<Integer> colors = new ArrayList<>();

            if (info.hasTaken) colors.add(Color.parseColor("#4CAF50"));
            if (info.hasPending) colors.add(Color.parseColor("#FF9800"));
            if (info.hasMissed) colors.add(Color.parseColor("#F44336"));

            if (!colors.isEmpty()) {
                view.addSpan(new MultiDotSpan(colors));
            }
        }
    }
}