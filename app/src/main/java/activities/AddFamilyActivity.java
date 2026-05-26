package activities;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.FamilyAdapter;
import models.Familiar;
import models.User;

public class AddFamilyActivity extends AppCompatActivity {

    private ListView lvUsers;

    private List<Familiar> familiarList;

    private FamilyAdapter adapter;

    private DatabaseReference usersRef;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);

        lvUsers = findViewById(R.id.lvUsers);

        currentUserId = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        familiarList = new ArrayList<>();

        adapter = new FamilyAdapter(this, familiarList);

        lvUsers.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance()
                .getReference("users");

        loadUsers();

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    android.view.View view,
                                    int position,
                                    long id) {

                Familiar familiar = familiarList.get(position);

                addFamily(familiar);
            }
        });
    }

    private void loadUsers() {

        usersRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                familiarList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {

                    User user = data.getValue(User.class);

                    if (user != null &&
                            !data.getKey().equals(currentUserId)) {

                        Familiar familiar = new Familiar();

                        familiar.setOwnerId(currentUserId);

                        familiar.setUserId(data.getKey());

                        familiar.setName(user.getName());

                        if (user.getUserAccount() != null) {

                            familiar.setEmail(
                                    user.getUserAccount().getEmail()
                            );
                        }

                        familiarList.add(familiar);
                    }
                }

                adapter.notifyDataSetChanged();

                Toast.makeText(
                        AddFamilyActivity.this,
                        "Usuarios cargados: " + familiarList.size(),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(
                        AddFamilyActivity.this,
                        "Error al cargar usuarios",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void addFamily(Familiar familiar) {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUserId)
                .child("family")
                .child(familiar.getUserId());

        ref.setValue(familiar)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            this,
                            "Familiar añadido",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Error al añadir familiar",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}