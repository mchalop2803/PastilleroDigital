package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListMedicamentActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ListView lvMedicament;
    private FloatingActionButton fltBtnAddMedicament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_medicament);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        lvMedicament.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Evaluation evaluation = evaluations.get(position);

                Intent intent = new Intent(ListMedicamentActivity.this, DetailsMedicamentActivity.class);
                //intent.putExtra("evaluation", evaluation);

                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListMedicamentActivity.this, ListDaysActivity.class);
            startActivity(intent);
        });

        fltBtnAddMedicament.setOnClickListener(v -> {
            Intent intent = new Intent(ListMedicamentActivity.this, AddMedicamentActivity.class);
            startActivity(intent);
        });
    }

    private void loadComponents(){
        lvMedicament = findViewById(R.id.lvMedicament);

        imageButton = findViewById(R.id.imageButton);
        fltBtnAddMedicament = findViewById(R.id.fltBtnAddMedicament);
    }
}