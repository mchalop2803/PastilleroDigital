package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

import adapters.AlarmAdapter;
import adapters.MedicamentAdapter;
import models.Alerta;
import models.Medicamento;

public class ListAlertActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ListView lvAlert;
    private FloatingActionButton fltBtnAddAlert;
    private List<Alerta> alertas;
    private AlarmAdapter alarmAdapter;

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

                        for (DataSnapshot data : snapshot.getChildren()) {
                            Alerta alerta = data.getValue(Alerta.class);

                            if (alerta != null) {
                                alertas.add(alerta);
                                Log.i("Alerta cargada", alerta.toString());
                            }
                        }

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
                Alerta alerta = alertas.get(position);

                Intent intent = new Intent(ListAlertActivity.this, DetailsAlertaActivity.class);
                intent.putExtra("alerts", alerta);

                startActivity(intent);
                finish();
            }
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

    private void loadComponents(){
        lvAlert = findViewById(R.id.lvAlarm);
        alertas = new ArrayList<>();
        imageButton = findViewById(R.id.imageButton);
        fltBtnAddAlert = findViewById(R.id.fltBtnAddAlert);
    }
}