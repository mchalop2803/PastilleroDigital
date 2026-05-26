package adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastillerodigital.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import models.Alerta;
import models.DayMedicationGroup;

public class DayScheduleAdapter
        extends RecyclerView.Adapter<DayScheduleAdapter.ViewHolder> {

    private Context context;
    private List<DayMedicationGroup> list;

    public DayScheduleAdapter(
            Context context,
            List<DayMedicationGroup> list
    ) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_day_schedule,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        DayMedicationGroup item = list.get(position);

        holder.tvMedicationName.setText(
                "💊 " + item.getMedicationName()
        );

        holder.layoutHours.removeAllViews();

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                );

        for (Alerta alerta : item.getAlerts()) {

            // FILA COMPLETA
            LinearLayout row =
                    new LinearLayout(context);

            row.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            row.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            LinearLayout.LayoutParams rowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            rowParams.setMargins(
                    0,
                    0,
                    0,
                    20
            );

            row.setLayoutParams(rowParams);

            // HORA

            TextView tvHour =
                    new TextView(context);

            String hora =
                    sdf.format(alerta.getHora());

            tvHour.setText("⏰ " + hora);

            tvHour.setTextSize(18);

            tvHour.setTypeface(
                    null,
                    Typeface.BOLD
            );

            tvHour.setTextColor(
                    ContextCompat.getColor(
                            context,
                            R.color.blue_primary
                    )
            );

            tvHour.setBackgroundResource(
                    R.drawable.bg_hour_item
            );

            tvHour.setPadding(
                    32,
                    20,
                    32,
                    20
            );

            LinearLayout.LayoutParams hourParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );

            hourParams.setMargins(
                    0,
                    0,
                    20,
                    0
            );

            tvHour.setLayoutParams(hourParams);

            // ESTADO

            TextView tvEstado =
                    new TextView(context);

            tvEstado.setTextSize(14);

            tvEstado.setTypeface(
                    null,
                    Typeface.BOLD
            );

            tvEstado.setGravity(Gravity.CENTER);

            tvEstado.setPadding(
                    28,
                    16,
                    28,
                    16
            );

            String estado = alerta.getEstado();

            if (estado == null) {
                estado = "PENDIENTE";
            }

            switch (estado.toUpperCase()) {

                case "TOMADA":

                    tvEstado.setText("Tomada");

                    tvEstado.setTextColor(
                            Color.parseColor("#2E7D32")
                    );

                    tvEstado.setBackgroundResource(
                            R.drawable.bg_status_taken
                    );

                    break;

                case "PERDIDA":

                    tvEstado.setText("Perdida");

                    tvEstado.setTextColor(
                            Color.parseColor("#C62828")
                    );

                    tvEstado.setBackgroundResource(
                            R.drawable.bg_status_missed
                    );

                    break;

                default:

                    tvEstado.setText("Pendiente");

                    tvEstado.setTextColor(
                            Color.parseColor("#EF6C00")
                    );

                    tvEstado.setBackgroundResource(
                            R.drawable.bg_status_pending
                    );

                    break;
            }

            row.addView(tvHour);

            row.addView(tvEstado);

            holder.layoutHours.addView(row);
        }
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvMedicationName;

        LinearLayout layoutHours;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvMedicationName =
                    itemView.findViewById(
                            R.id.tvMedicationName
                    );

            layoutHours =
                    itemView.findViewById(
                            R.id.layoutHours
                    );
        }
    }
}