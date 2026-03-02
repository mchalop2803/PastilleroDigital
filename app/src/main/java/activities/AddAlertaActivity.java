package activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.time.LocalTime;

import models.Alerta;
import services.AlertService;

public class AddAlertaActivity extends AppCompatActivity {

    private ImageButton imgBtnBack, imgBtnMedicamentImage;
    private TextInputEditText textInputEditTextAlertName, textInputEditTextAlertTime;
    private Button btnAddAlert;

    private AlertService alertService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_alerta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imgBtnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddAlertaActivity.this, DetailsAlertaActivity.class);
            startActivity(intent);
        });


        btnAddAlert.setOnClickListener(v -> new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Alerta alerta = new Alerta();
                alerta.setNombre(textInputEditTextAlertName.getText().toString());
                alerta.setHora(textInputEditTextAlertTime.getText().toString());


                if (textInputEditTextAlertName.getText().toString().isBlank()){
                    Toast.makeText(AddAlertaActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textInputEditTextAlertTime.getText().toString().isBlank()){
                    Toast.makeText(AddAlertaActivity.this, "Time is blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                String idAlert = alertService.insertAlert(alerta);
                Toast.makeText(AddAlertaActivity.this, "Alert with id " + idAlert + " inserted", Toast.LENGTH_SHORT).show();
                Log.i("Alert id", idAlert);

                Intent intent = new Intent(AddAlertaActivity.this, DetailsAlertaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadComponents(){
        textInputEditTextAlertName = findViewById(R.id.textInputEditTextAlertName);
        textInputEditTextAlertTime = findViewById(R.id.textInputEditTextAlertTime);

        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnMedicamentImage = findViewById(R.id.imgBtnMedicamentImage);
        btnAddAlert = findViewById(R.id.btnAddAlert);

        alertService = new AlertService(getApplicationContext());
    }
}