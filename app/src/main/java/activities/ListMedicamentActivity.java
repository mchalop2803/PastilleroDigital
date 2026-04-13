package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

import adapters.FamilyAdapter;
import adapters.MedicamentAdapter;
import models.Familiar;
import models.Medicamento;

public class ListMedicamentActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ListView lvMedicament;
    private FloatingActionButton fltBtnAddMedicament;
    private List<Medicamento> medicamentos;
    private MedicamentAdapter medicamentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_medicament);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        String momentDay = getIntent().getStringExtra("momentDay");

        TextView title = findViewById(R.id.tvMedicamentoTitle);

        if (momentDay != null) {
            switch (momentDay) {
                case "DAY":
                    title.setText("Medicamentos de la mañana");
                    break;
                case "AFTERNOON":
                    title.setText("Medicamentos de la tarde");
                    break;
                case "NIGHT":
                    title.setText("Medicamentos de la noche");
                    break;
            }
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("medicaments");

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        String medicamentId = prefs.getString("id", null);

        FirebaseDatabase.getInstance()
                .getReference("medicaments")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        medicamentos.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            Medicamento medicamento = data.getValue(Medicamento.class);

                            if (medicamento != null) {
                                medicamento.setId(data.getKey());
                                if (momentDay == null || momentDay.equals(medicamento.getMomentDay())) {
                                    medicamentos.add(medicamento);
                                    Log.i("Medicamento filtrado", medicamento.toString());
                                }
                            }
                        }

                        if (medicamentAdapter == null) {
                            medicamentAdapter = new MedicamentAdapter(ListMedicamentActivity.this, medicamentos);
                            lvMedicament.setAdapter(medicamentAdapter);
                        } else {
                            medicamentAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error", error.toException());
                    }
                });

        lvMedicament.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Medicamento medicamento = medicamentos.get(position);

                Intent intent = new Intent(ListMedicamentActivity.this, DetailsMedicamentActivity.class);
                intent.putExtra("medicaments", medicamento);

                startActivity(intent);
                finish();
            }
        });

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListMedicamentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        fltBtnAddMedicament.setOnClickListener(v -> {
            Intent intent = new Intent(ListMedicamentActivity.this, AddMedicamentActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadComponents(){
        lvMedicament = findViewById(R.id.lvMedicament);
        medicamentos = new ArrayList<>();
        imageButton = findViewById(R.id.imageButton);
        fltBtnAddMedicament = findViewById(R.id.fltBtnAddMedicament);
    }
}