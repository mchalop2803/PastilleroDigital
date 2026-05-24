package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
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

        ImageView img = convertView.findViewById(R.id.imgMedicament);

        if (medicamento.getImageUrl() != null && !medicamento.getImageUrl().isEmpty()) {
            Glide.with(getContext())
                    .load(medicamento.getImageUrl())
                    .placeholder(R.drawable.ic_pastillero)
                    .into(img);
        } else {
            img.setImageResource(R.drawable.ic_pastillero);
        }

        TextView tvMedicamentName = convertView.findViewById(R.id.tvMedicamentName);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);

        tvMedicamentName.setText("Medicamento: " + medicamento.getNombre());
        tvDescription.setText("Descipción: " + medicamento.getDescription());

        return convertView;
    }
}
