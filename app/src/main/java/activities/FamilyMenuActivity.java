package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;

public class FamilyMenuActivity extends AppCompatActivity {

    Button btnAddFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_menu);

        btnAddFamily = findViewById(R.id.btnAddFamily);

        btnAddFamily.setOnClickListener(v -> {
            startActivity(new Intent(this, AddFamilyActivity.class));
        });
    }
}