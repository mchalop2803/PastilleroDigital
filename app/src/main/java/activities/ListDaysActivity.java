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

import java.util.ArrayList;

public class ListDaysActivity extends AppCompatActivity {

    private ListView lvDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_days);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadComponents();

        lvDay.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            //Evaluation evaluation = evaluations.get(position);

            Intent intent = new Intent(ListDaysActivity.this, ListMedicamentActivity.class);
            //intent.putExtra("evaluation", evaluation);

            startActivity(intent);
        }
        });
    }

    private void loadComponents(){
        lvDay = findViewById(R.id.lvDay);
    }
}