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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import models.CitaMedica;
import models.Familiar;
import models.Medicamento;
import services.FamiliarService;

public class AddFamilyActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private TextInputEditText textInputEditTextFamilyName, textInputEditTextFamilyPhone, textInputEditTextFamilyRelation;

    private Button btnAddFamily;

    private Familiar familiarEdit;

    private Boolean editMode;

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

        toolbar.setOnClickListener(v -> {
            Intent intent = new Intent(AddFamilyActivity.this, ListFamilyActivity.class);
            startActivity(intent);
        });

        btnAddFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    familiarEdit.setNombre(textInputEditTextFamilyName.getText().toString());
                    familiarEdit.setPhone(textInputEditTextFamilyPhone.getText().toString());
                    familiarEdit.setRelacion(textInputEditTextFamilyRelation.getText().toString());

                    if (textInputEditTextFamilyName.getText().toString().isBlank()) {
                        Toast.makeText(AddFamilyActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (textInputEditTextFamilyPhone.getText().toString().isBlank()) {
                        Toast.makeText(AddFamilyActivity.this, "Phone is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (textInputEditTextFamilyRelation.getText().toString().isBlank()) {
                        Toast.makeText(AddFamilyActivity.this, "Relation is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    familiarService.updateFamiliar(familiarEdit);
                    Toast.makeText(AddFamilyActivity.this, "Family updated", Toast.LENGTH_SHORT).show();
                    Log.i("Family id", familiarEdit.getId());

                    Intent intent = new Intent(AddFamilyActivity.this, ListFamilyActivity.class);
                    startActivity(intent);

                } else {
                    Familiar familiar = new Familiar();
                    familiar.setNombre(textInputEditTextFamilyName.getText().toString());
                    familiar.setPhone(textInputEditTextFamilyPhone.getText().toString());
                    familiar.setRelacion(textInputEditTextFamilyRelation.getText().toString());


                    if (textInputEditTextFamilyName.getText().toString().isBlank()) {
                        Toast.makeText(AddFamilyActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (textInputEditTextFamilyPhone.getText().toString().isBlank()) {
                        Toast.makeText(AddFamilyActivity.this, "Phone is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (textInputEditTextFamilyRelation.getText().toString().isBlank()) {
                        Toast.makeText(AddFamilyActivity.this, "Relation is blank", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String idFamily = familiarService.insertFamiliar(familiar);
                    Toast.makeText(AddFamilyActivity.this, "Familiar with id " + idFamily + " inserted", Toast.LENGTH_SHORT).show();
                    Log.i("Familiar id", idFamily);

                    Intent intent = new Intent(AddFamilyActivity.this, ListFamilyActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void loadComponents(){
        toolbar = findViewById(R.id.toolbar);

        textInputEditTextFamilyName = findViewById(R.id.etName);
        textInputEditTextFamilyPhone = findViewById(R.id.etPhone);
        textInputEditTextFamilyRelation = findViewById(R.id.etRelation);

        btnAddFamily = findViewById(R.id.btnSave);

        familiarService = new FamiliarService(getApplicationContext());

        Intent intent = getIntent();
        if(intent.getSerializableExtra("family") != null){
            familiarEdit = (Familiar) intent.getSerializableExtra("family");
            textInputEditTextFamilyName.setText(familiarEdit.getNombre());
            textInputEditTextFamilyPhone.setText(familiarEdit.getPhone());
            textInputEditTextFamilyRelation.setText(familiarEdit.getRelacion());

        }

        editMode = intent.getBooleanExtra("editMode", false);
    }
}