package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pastillerodigital.R;

import java.util.ArrayList;

import adapters.DaysAdapter;
import models.Days;

public class ListDaysActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private ListView lvDay;
    private ArrayList<Days> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_days);

        loadComponents();

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListDaysActivity.this, MainActivity.class);
            startActivity(intent);
        });

        days = new ArrayList<>();
        days.add(Days.DAY);
        days.add(Days.AFTERNOON);
        days.add(Days.NIGHT);

        DaysAdapter adapter = new DaysAdapter(this, days);
        lvDay.setAdapter(adapter);

        lvDay.setOnItemClickListener((adapterView, view, position, id) -> {

            Days selectedDay = days.get(position);

            Intent intent = new Intent(ListDaysActivity.this, ListMedicamentActivity.class);
            intent.putExtra("momentDay", selectedDay.name());
            startActivity(intent);
        });
    }

    private void loadComponents(){
        lvDay = findViewById(R.id.lvDay);
        imageButton = findViewById(R.id.imageButton);
    }
}