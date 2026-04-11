package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pastillerodigital.R;

import java.util.List;

import models.Days;

public class DaysAdapter extends ArrayAdapter<Days> {

    public DaysAdapter(@NonNull Context context, List<Days> days) {
        super(context, 0, days);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Days day = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_day, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.tvDayOption);

        if (day != null) {
            switch (day) {
                case DAY:
                    tv.setText("Mañana");
                    break;
                case AFTERNOON:
                    tv.setText("Tarde");
                    break;
                case NIGHT:
                    tv.setText("Noche");
                    break;
            }
        }

        return convertView;
    }
}