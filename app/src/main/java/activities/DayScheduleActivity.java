package activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastillerodigital.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

import adapters.DayScheduleAdapter;
import models.Alerta;
import models.DayMedicationGroup;

public class DayScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvDate;
    private ImageButton btnBack;

    private final List<DayMedicationGroup> list = new ArrayList<>();
    private DayScheduleAdapter adapter;

    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_schedule);

        loadComponents();

        day = getIntent().getIntExtra("day", -1);
        month = getIntent().getIntExtra("month", -1);
        year = getIntent().getIntExtra("year", -1);

        tvDate.setText(day + "/" + month + "/" + year);

        adapter = new DayScheduleAdapter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadComponents() {
        recyclerView = findViewById(R.id.recyclerView);
        tvDate = findViewById(R.id.tvDate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadData() {

        String uid = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        Map<String, DayMedicationGroup> map = new HashMap<>();

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("alerts")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot d : snapshot.getChildren()) {

                            Alerta alerta = d.getValue(Alerta.class);
                            if (alerta == null) continue;

                            if (!isAlertInSelectedDay(alerta)) continue;

                            String key = alerta.getMedicamentoId();
                            if (key == null || key.isEmpty()) {
                                key = alerta.getNombre();
                            }

                            DayMedicationGroup group = map.get(key);

                            if (group == null) {
                                group = new DayMedicationGroup();
                                group.setMedicationName(alerta.getNombre());
                                map.put(key, group);
                            }

                            group.getAlerts().add(alerta);
                        }

                        list.clear();
                        list.addAll(map.values());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private boolean isAlertInSelectedDay(Alerta alerta) {

        Calendar start = Calendar.getInstance();
        start.set(year, month - 1, day, 0, 0, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(year, month - 1, day, 23, 59, 59);
        end.set(Calendar.MILLISECOND, 999);

        long hora = alerta.getHora();

        return hora >= start.getTimeInMillis()
                && hora <= end.getTimeInMillis();
    }
}