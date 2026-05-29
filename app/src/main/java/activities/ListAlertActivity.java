package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.app.DatePickerDialog;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.textfield.TextInputEditText;
import adapters.AlarmAdapter;
import models.Alerta;

public class ListAlertActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ListView lvAlert;
    private FloatingActionButton fltBtnAddAlert;
    private List<Alerta> alertas;
    private AlarmAdapter alarmAdapter;
    private Spinner spMedicamentos;
    private TextInputEditText etFechaInicio, etFechaFin;

    private List<Alerta> alertasOriginales;

    private long fechaInicio = 0;
    private long fechaFin = Long.MAX_VALUE;
    private String medicamentoSeleccionado = "Todos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_alert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("alerts");

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        String userId = prefs.getString("id", null);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("alerts")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        alertas.clear();
                        alertasOriginales.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            Alerta alerta = data.getValue(Alerta.class);

                            if (alerta != null) {
                                alertas.add(alerta);
                                alertasOriginales.add(alerta);
                            }
                        }

                        cargarMedicamentosSpinner();

                        alarmAdapter = new AlarmAdapter(ListAlertActivity.this, alertas);
                        lvAlert.setAdapter(alarmAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error", error.toException());
                    }
                });

        lvAlert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Alerta alerta = alarmAdapter.getItem(position);

                Intent intent = new Intent(ListAlertActivity.this, DetailsAlertaActivity.class);
                intent.putExtra("alerts", alerta);

                startActivity(intent);
                finish();
            }
        });

        spMedicamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                medicamentoSeleccionado =
                        parent.getItemAtPosition(position).toString();

                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etFechaInicio.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {

                        Calendar fecha = Calendar.getInstance();

                        fecha.set(year, month, dayOfMonth, 0, 0, 0);

                        fechaInicio = fecha.getTimeInMillis();

                        etFechaInicio.setText(
                                dayOfMonth + "/" + (month + 1) + "/" + year
                        );

                        if (fechaFin < fechaInicio) {

                            fechaFin = Long.MAX_VALUE;

                            etFechaFin.setText("");
                        }

                        aplicarFiltros();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        etFechaFin.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {

                        Calendar fecha = Calendar.getInstance();

                        fecha.set(year, month, dayOfMonth, 23, 59, 59);

                        fechaFin = fecha.getTimeInMillis();

                        etFechaFin.setText(
                                dayOfMonth + "/" + (month + 1) + "/" + year
                        );

                        aplicarFiltros();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            if (fechaInicio != 0) {
                dialog.getDatePicker().setMinDate(fechaInicio);
            }

            dialog.show();
        });

        imageButton.setOnClickListener(v -> {
            finish();
        });

        fltBtnAddAlert.setOnClickListener(v -> {
            Intent intent = new Intent(ListAlertActivity.this, AddAlertaActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void aplicarFiltros() {

        List<Alerta> filtradas = new ArrayList<>();

        for (Alerta alerta : alertasOriginales) {

            boolean coincideMedicamento =
                    medicamentoSeleccionado.equals("Todos")
                            || alerta.getNombre().equals(medicamentoSeleccionado);

            boolean coincideFecha =
                    alerta.getHora() >= fechaInicio
                            && alerta.getHora() <= fechaFin;

            if (coincideMedicamento && coincideFecha) {
                filtradas.add(alerta);
            }
        }

        alarmAdapter = new AlarmAdapter(this, filtradas);
        lvAlert.setAdapter(alarmAdapter);
    }

    private void cargarMedicamentosSpinner() {

        List<String> medicamentos = new ArrayList<>();
        medicamentos.add("Todos");

        for (Alerta alerta : alertasOriginales) {

            if (!medicamentos.contains(alerta.getNombre())) {
                medicamentos.add(alerta.getNombre());
            }
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        medicamentos
                );

        spMedicamentos.setAdapter(adapter);
    }

    private void loadComponents(){
        lvAlert = findViewById(R.id.lvAlarm);
        alertas = new ArrayList<>();
        imageButton = findViewById(R.id.imageButton);
        fltBtnAddAlert = findViewById(R.id.fltBtnAddAlert);
        lvAlert = findViewById(R.id.lvAlarm);

        alertas = new ArrayList<>();
        alertasOriginales = new ArrayList<>();

        imageButton = findViewById(R.id.imageButton);
        fltBtnAddAlert = findViewById(R.id.fltBtnAddAlert);

        spMedicamentos = findViewById(R.id.spMedicamentos);

        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
    }
}