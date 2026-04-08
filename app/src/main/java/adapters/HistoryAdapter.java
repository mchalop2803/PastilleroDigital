package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pastillerodigital.R;

import java.text.SimpleDateFormat;
import java.util.List;

import models.CitaMedica;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<CitaMedica> citas;

    public HistoryAdapter(List<CitaMedica> citas) {
        this.citas = citas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CitaMedica cita = citas.get(position);
        holder.tvCitaTitle.setText(cita.getDescription());
        holder.tvCitaDoctor.setText("Doctor: " + cita.getMedico());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.tvCitaDate.setText("Fecha: " + sdf.format(cita.getFecha()));
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCitaTitle, tvCitaDate, tvCitaDoctor;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCitaTitle = itemView.findViewById(R.id.tvCitaTitle);
            tvCitaDate = itemView.findViewById(R.id.tvCitaDate);
            tvCitaDoctor = itemView.findViewById(R.id.tvCitaDoctor);
        }
    }

    public void setCitas(List<CitaMedica> nuevasCitas) {
        citas.clear();
        citas.addAll(nuevasCitas);
        notifyDataSetChanged();
    }
}