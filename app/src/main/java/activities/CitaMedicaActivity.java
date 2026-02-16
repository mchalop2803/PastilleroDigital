package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;

import models.Perfil;

public class CitaMedicaActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private Button btnRegisterCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cita_medica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(CitaMedicaActivity.this, ListDaysActivity.class);
            startActivity(intent);
        });

        btnRegisterCita.setOnClickListener(v -> {
            Intent intent = new Intent(CitaMedicaActivity.this, ListDaysActivity.class);
            startActivity(intent);
        });

    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);

        btnRegisterCita = findViewById(R.id.btnRegisterCita);
    }
}