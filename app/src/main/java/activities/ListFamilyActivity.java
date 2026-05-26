package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

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

        loadComponents();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            startActivity(
                    new Intent(this, LoginActivity.class)
            );

            finish();
            return;
        }

        String userId = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("family");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                familiars.clear();

                for (DataSnapshot data : snapshot.getChildren()) {

                    Familiar familiar = data.getValue(Familiar.class);

                    if (familiar != null) {
                        familiars.add(familiar);
                    }
                }

                familyAdapter = new FamilyAdapter(ListFamilyActivity.this, familiars);
                lvFamily.setAdapter(familyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        fltBtnAddFamily.setOnClickListener(v -> {
            startActivity(new Intent(this, AddFamilyActivity.class));
        });

        imageButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        lvFamily.setOnItemClickListener((adapterView, view, position, id) -> {

            Familiar familiar = familiars.get(position);

            Intent intent = new Intent(this, DetailsFamilyActivity.class);
            intent.putExtra("family", familiar);

            startActivity(intent);
        });
    }

    private void loadComponents() {

        lvFamily = findViewById(R.id.lvFamily);
        fltBtnAddFamily = findViewById(R.id.fltBtnAddFamily);
        imageButton = findViewById(R.id.imageButton);

        familiars = new ArrayList<>();
    }
}