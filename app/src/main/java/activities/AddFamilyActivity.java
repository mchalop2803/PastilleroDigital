package activities;

import android.content.Intent;
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

import models.CitaMedica;
import models.Familiar;
import services.FamiliarService;

public class AddFamilyActivity extends AppCompatActivity {

    private ImageButton imageButton;

    private TextInputEditText textInputEditTextFamilyName, textInputEditTextFamilyPhone, textInputEditTextFamilyRelation;

    private Button btnAddFamily;

    private FamiliarService familiarService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_family);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddFamilyActivity.this, ListFamilyActivity.class);
            startActivity(intent);
        });

        btnAddFamily.setOnClickListener(v -> new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Familiar familiar = new Familiar();
                familiar.setNombre(textInputEditTextFamilyName.getText().toString());
                familiar.setPhone(textInputEditTextFamilyPhone.getText().toString());
                familiar.setRelacion(textInputEditTextFamilyRelation.getText().toString());


                if (textInputEditTextFamilyName.getText().toString().isBlank()){
                    Toast.makeText(AddFamilyActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textInputEditTextFamilyPhone.getText().toString().isBlank()){
                    Toast.makeText(AddFamilyActivity.this, "Phone is blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (textInputEditTextFamilyRelation.getText().toString().isBlank()){
                    Toast.makeText(AddFamilyActivity.this, "Relation is blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                String idFamily = familiarService.insertFamiliar(familiar);
                Toast.makeText(AddFamilyActivity.this, "Familiar with id " + idFamily + " inserted", Toast.LENGTH_SHORT).show();
                Log.i("Familiar id", idFamily);

                Intent intent = new Intent(AddFamilyActivity.this, ListFamilyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);

        textInputEditTextFamilyName = findViewById(R.id.textInputEditTextFamilyName);
        textInputEditTextFamilyPhone = findViewById(R.id.textInputEditTextFamilyPhone);
        textInputEditTextFamilyRelation = findViewById(R.id.textInputEditTextFamilyRelation);

        btnAddFamily = findViewById(R.id.btnAddFamily);

        familiarService = new FamiliarService(getApplicationContext());
    }
}