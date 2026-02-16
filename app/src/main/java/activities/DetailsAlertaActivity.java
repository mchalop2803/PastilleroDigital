package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;

import com.example.pastillerodigital.R;

public class DetailsAlertaActivity extends AppCompatActivity {

    private ImageButton imgBtnBack, imgBtnAlarma, imgBtnCheck;
    private TextView tvAlarmTime, tvMedicamentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_alerta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imgBtnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsAlertaActivity.this, DetailsMedicamentActivity.class);
            startActivity(intent);
        });


        imgBtnCheck.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsAlertaActivity.this, DetailsMedicamentActivity.class);
            startActivity(intent);
        });

        tvAlarmTime.setText("Hora: ");
        tvMedicamentName.setText("Medicamento: ");
    }

    private void loadComponents(){
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnAlarma = findViewById(R.id.imgBtnAlarma);
        imgBtnCheck = findViewById(R.id.imgBtnCheck);

        tvAlarmTime = findViewById(R.id.tvAlarmTime);
        tvMedicamentName = findViewById(R.id.tvMedicamentName);
    }
}