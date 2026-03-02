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

import models.Familiar;

public class FamilyAdapter extends ArrayAdapter<Familiar> {

    public FamilyAdapter(@NonNull Context context, List<Familiar> familiars) {
        super(context, 0, familiars);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Familiar familiar = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_family, parent, false);
        }

        TextView tvNameFamily = convertView.findViewById(R.id.tvMedicamentName);
        TextView tvPhoneFamily = convertView.findViewById(R.id.tvTimeofTake);
        TextView tvRelationFamily = convertView.findViewById(R.id.tvAmount);

        tvNameFamily.setText("Familiar: " + familiar.getNombre());
        tvPhoneFamily.setText("Teléfono: " + familiar.getPhone());
        tvRelationFamily.setText("Relación: " + familiar.getRelacion());

        return convertView;
    }
}
