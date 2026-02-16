package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListFamilyActivity extends AppCompatActivity {

    private ListView lvFamily;
    private FloatingActionButton fltBtnAddFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_family);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        lvFamily.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Evaluation evaluation = evaluations.get(position);

                //Intent intent = new Intent(ListFamilyActivity.this, DetailsEvaluationActivity.class);
                //intent.putExtra("evaluation", evaluation);

                //startActivity(intent);
            }
        });

        fltBtnAddFamily.setOnClickListener(v -> {
            Intent intent = new Intent(ListFamilyActivity.this, AddFamilyActivity.class);
            startActivity(intent);
        });
    }

    private void loadComponents(){
        lvFamily = findViewById(R.id.lvFamily);

        fltBtnAddFamily = findViewById(R.id.fltBtnAddFamily);
    }
}