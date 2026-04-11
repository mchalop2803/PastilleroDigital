package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.medicament) {
                Intent intApp = new Intent(this, ListMedicamentActivity.class);
                startActivity(intApp);
            } else if (id == R.id.alert) {
                Intent intApp = new Intent(this, ListAlertActivity.class);
                startActivity(intApp);
            } else if (id == R.id.profile) {
                startActivity(new Intent(this, DetailsProfileActivity.class));
                finish();
            } else {
                return false;
            }

            return true;
        });

        NavigationView navView = findViewById(R.id.nvMenu);

        setupDrawerMenu(navView);

        Button logoutButton = navView.findViewById(R.id.btnLogout);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences = getSharedPreferences("Prefs", MODE_PRIVATE);
            preferences.edit().clear().apply();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        navView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.medication) {
                Toast.makeText(this, "Medicamentos", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ListMedicamentActivity.class));
                finish();
            } else if (itemId == R.id.agenda) {
                Toast.makeText(this, "Agenda", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                finish();
            }else if (itemId == R.id.days) {
                Toast.makeText(this, "Pastillero diario", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ListDaysActivity.class));
                finish();
            } else if (itemId == R.id.familiar) {
                Toast.makeText(this, "Familiares", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ListFamilyActivity.class));
                finish();
            } else if (itemId == R.id.citaMedica) {

                Toast.makeText(this, "Cita Medica", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, CitaMedicaActivity.class));
                finish();
            }

            return false;
        });
    }

    private void setupDrawerMenu(NavigationView navView) {
        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        String name = prefs.getString("name", "Usuario");

        Menu menu = navView.getMenu();
        menu.findItem(R.id.lateral_profile1).setTitle(name);
        menu.findItem(R.id.lateral_profile2).setVisible(false);
    }
}