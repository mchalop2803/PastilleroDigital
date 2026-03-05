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

import models.Alerta;

public class AlarmAdapter extends ArrayAdapter<Alerta> {

    public AlarmAdapter(@NonNull Context context, List<Alerta> alertas) {
        super(context, 0, alertas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Alerta alerta = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }

        TextView tvNameAlarm = convertView.findViewById(R.id.tvNameAlarm);
        TextView tvTimeAlarm = convertView.findViewById(R.id.tvTimeAlarm);

        tvNameAlarm.setText("Nombre: " + alerta.getNombre());
        tvTimeAlarm.setText("Hora: " + alerta.getHora());

        return convertView;
    }
}
