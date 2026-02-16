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

public class AddProfileActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextInputEditText textInputEditTextProfileName, textInputEditTextProfilePermissions;
    private Button btnAddProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddProfileActivity.this, ListDaysActivity.class);
            startActivity(intent);
        });

        btnAddProfile.setOnClickListener(v -> new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Perfil perfil = new Perfil();
                perfil.setName(textInputEditTextProfileName.getText().toString());
                perfil.setPermiso(textInputEditTextProfilePermissions.getText().toString());


                if (textInputEditTextProfileName.getText().toString().isBlank()){
                    Toast.makeText(AddProfileActivity.this, "Name is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textInputEditTextProfilePermissions.getText().toString().isBlank()){
                    Toast.makeText(AddProfileActivity.this, "Permisos is blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(AddProfileActivity.this, ListDaysActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadComponents(){
        imageButton = findViewById(R.id.imageButton);

        textInputEditTextProfileName = findViewById(R.id.textInputEditTextProfileName);
        textInputEditTextProfilePermissions = findViewById(R.id.textInputEditTextProfilePermissions);
        btnAddProfile = findViewById(R.id.btnAddProfile);
    }
}