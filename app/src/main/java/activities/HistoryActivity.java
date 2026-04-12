package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastillerodigital.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapters.HistoryAdapter;
import models.CitaMedica;
import services.CitaMedicaService;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private Spinner spinnerMonth;
    private RecyclerView recyclerView;
    private TextView tvHistorialTitle;

    private List<CitaMedica> citas = new ArrayList<>();
    private CitaMedicaService citaService;

    private HistoryAdapter adapter;

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

        citaService = new CitaMedicaService(getApplicationContext());

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
        });

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo",
                        "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"});
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));

        spinnerMonth.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                loadCitasByMonth(position + 1);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        adapter = new HistoryAdapter(citas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadComponents() {
        imageButton = findViewById(R.id.imageButton);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        recyclerView = findViewById(R.id.recyclerViewCitas);
        tvHistorialTitle = findViewById(R.id.tvHistorialTitle);
    }

    private void loadCitasByMonth(int month) {
        citaService.getAllCitas(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CitaMedica> citasDelMes = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                for (DataSnapshot data : snapshot.getChildren()) {
                    CitaMedica cita = data.getValue(CitaMedica.class);
                    if (cita != null) {
                        try {
                            Date fecha = sdf.parse(cita.getFecha());
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(fecha);
                            if (cal.get(Calendar.MONTH) + 1 == month) {
                                citasDelMes.add(cita);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                adapter.setCitas(citasDelMes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Error cargando citas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}