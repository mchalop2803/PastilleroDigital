package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;

public class DetailsMedicamentActivity extends AppCompatActivity {

    private ImageButton imageButton, imgBtnMedicament, btnAddAlert;
    private TextView tvMedicamentName, tvMedicamentAmount, tvMedicamentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_medicament);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsMedicamentActivity.this, ListMedicamentActivity.class);
            startActivity(intent);
        });


        btnAddAlert.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsMedicamentActivity.this, AddAlertaActivity.class);
            startActivity(intent);
        });

        tvMedicamentName.setText("Medicamento: ");
        tvMedicamentAmount.setText("Dosis: ");
        tvMedicamentTime.setText("Hora: ");
    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);
        imgBtnMedicament = findViewById(R.id.imgBtnMedicament);
        btnAddAlert = findViewById(R.id.btnAddAlert);

        tvMedicamentName = findViewById(R.id.tvMedicamentName);
        tvMedicamentAmount = findViewById(R.id.tvMedicamentAmount);
        tvMedicamentTime = findViewById(R.id.tvMedicamentTime);
    }
}