package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pastillerodigital.R;

import java.util.List;

import models.Medicamento;

public class MedicamentAdapter extends ArrayAdapter<Medicamento> {

    public MedicamentAdapter(Context context, List<Medicamento> medicamentos) {
        super(context, 0, medicamentos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Medicamento medicamento = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_medicamentos, parent, false);
        }

        TextView tvMedicamentName = convertView.findViewById(R.id.tvMedicamentName);
        TextView tvTimeofTake = convertView.findViewById(R.id.tvTimeofTake);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);

        tvMedicamentName.setText("Medicamento: " + medicamento.getNombre());
        tvTimeofTake.setText("Hora: " + medicamento.getHorario());
        tvAmount.setText("Cantidad: " + medicamento.getDosis());

        return convertView;
    }
}
