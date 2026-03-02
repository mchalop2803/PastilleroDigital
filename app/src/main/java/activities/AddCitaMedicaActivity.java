package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.textfield.TextInputEditText;

import models.Alerta;
import models.CitaMedica;
import services.CitaMedicaService;

public class AddCitaMedicaActivity extends AppCompatActivity {

    private ImageButton imageButton;

    private TextInputEditText textInputEditTextCitaDescription, textInputEditTextCitaCompanion, textInputEditTextCitaDate,
            textInputEditTextCitaTime, textInputEditTextCitaLocation, textInputEditTextCitaMedic;

    private Button btnRegisterCita;

    private CitaMedicaService citaMedicaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_cita_medica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddCitaMedicaActivity.this, DetailsAlertaActivity.class);
            startActivity(intent);
        });

        btnRegisterCita.setOnClickListener(v -> new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CitaMedica citaMedica = new CitaMedica();
                citaMedica.setDescription(textInputEditTextCitaDescription.getText().toString());
                citaMedica.setAcompañante(textInputEditTextCitaCompanion.getText().toString());
                citaMedica.setFecha(textInputEditTextCitaDate.getText().toString());
                citaMedica.setHora(textInputEditTextCitaTime.getText().toString());
                citaMedica.setMedico(textInputEditTextCitaMedic.getText().toString());
                citaMedica.setLocation(textInputEditTextCitaLocation.getText().toString());


                if (textInputEditTextCitaDescription.getText().toString().isBlank()){
                    Toast.makeText(AddCitaMedicaActivity.this, "Description is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textInputEditTextCitaCompanion.getText().toString().isBlank()){
                    Toast.makeText(AddCitaMedicaActivity.this, "Companion is blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (textInputEditTextCitaDate.getText().toString().isBlank()){
                    Toast.makeText(AddCitaMedicaActivity.this, "Date is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textInputEditTextCitaTime.getText().toString().isBlank()){
                    Toast.makeText(AddCitaMedicaActivity.this, "Time is blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (textInputEditTextCitaMedic.getText().toString().isBlank()){
                    Toast.makeText(AddCitaMedicaActivity.this, "Medic is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textInputEditTextCitaLocation.getText().toString().isBlank()){
                    Toast.makeText(AddCitaMedicaActivity.this, "Location is blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                String idCitaMedica = citaMedicaService.insertCitaMedica(citaMedica);
                Toast.makeText(AddCitaMedicaActivity.this, "CitaMedica with id " + idCitaMedica + " inserted", Toast.LENGTH_SHORT).show();
                Log.i("CitaMedica id", idCitaMedica);

                Intent intent = new Intent(AddCitaMedicaActivity.this, DetailsAlertaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadComponents(){
        textInputEditTextCitaDescription = findViewById(R.id.textInputEditTextCitaDescription);
        textInputEditTextCitaCompanion = findViewById(R.id.textInputEditTextCitaCompanion);
        textInputEditTextCitaDate = findViewById(R.id.textInputEditTextCitaDate);
        textInputEditTextCitaTime = findViewById(R.id.textInputEditTextCitaTime);
        textInputEditTextCitaLocation = findViewById(R.id.textInputEditTextCitaLocation);
        textInputEditTextCitaMedic = findViewById(R.id.textInputEditTextCitaMedic);

        imageButton = findViewById(R.id.imageButton);
        btnRegisterCita = findViewById(R.id.btnRegisterCita);

        citaMedicaService = new CitaMedicaService(getApplicationContext());
    }
}