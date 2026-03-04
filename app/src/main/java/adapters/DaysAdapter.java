package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pastillerodigital.R;

import java.util.List;

import models.Medicamento;

public class DaysAdapter extends ArrayAdapter<Medicamento> {

    public DaysAdapter(@NonNull Context context, List<Medicamento> medicamentos) {
        super(context, 0, medicamentos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Medicamento medicamento = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_day, parent, false);
        }

        TextView tvMorning = convertView.findViewById(R.id.tvMorning);
        TextView tvAfternoon = convertView.findViewById(R.id.tvAfternoon);
        TextView tvNight = convertView.findViewById(R.id.tvNight);

        tvMorning.setText("Mañana");
        tvAfternoon.setText("Tarde");
        tvNight.setText("Noche");

        return convertView;
    }
}
