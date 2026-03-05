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

import adapters.FamilyAdapter;
import models.Familiar;

public class ListFamilyActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ListView lvFamily;
    private FloatingActionButton fltBtnAddFamily;
    private List<Familiar> familiars;
    private FamilyAdapter familyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_family);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("familys");

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        String familyId = prefs.getString("id", null);


        FirebaseDatabase.getInstance().getReference("familys")
                .orderByChild("familyId")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        familiars.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Familiar familiar = snapshot.getValue(Familiar.class);
                            if (familiar != null) {
                                familiars.add(familiar);
                                Log.i("Familiar cargado", familiar.toString());
                            }
                        }

                        familyAdapter = new FamilyAdapter(ListFamilyActivity.this, familiars);
                        lvFamily.setAdapter(familyAdapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Firebase", "Error al firebase", databaseError.toException());
                    }
                });


        fltBtnAddFamily.setOnClickListener(v -> {
            Intent intent = new Intent(ListFamilyActivity.this, AddFamilyActivity.class);
            startActivity(intent);
        });

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListFamilyActivity.this, MainActivity.class);
            startActivity(intent);
        });

        lvFamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Familiar familiar = familiars.get(position);

                Intent intent = new Intent(ListFamilyActivity.this, DetailsFamilyActivity.class);
                intent.putExtra("familys", familiar);

                startActivity(intent);
            }
        });
    }

    private void loadComponents(){
        lvFamily = findViewById(R.id.lvFamily);
        familiars = new ArrayList<>();
        fltBtnAddFamily = findViewById(R.id.fltBtnAddFamily);
        imageButton = findViewById(R.id.imageButton);
    }
}