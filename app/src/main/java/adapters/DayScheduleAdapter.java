package adapters;

import android.content.Context;
import android.graphics.Typeface;
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

            TextView tv = new TextView(context);

            String hora =
                    sdf.format(alerta.getHora());

            tv.setText("⏰ " + hora);

            tv.setTextSize(18);

            tv.setTypeface(null, Typeface.BOLD);

            tv.setTextColor(
                    ContextCompat.getColor(
                            context,
                            R.color.blue_primary
                    )
            );

            tv.setBackgroundResource(
                    R.drawable.bg_hour_item
            );

            tv.setPadding(
                    32,
                    20,
                    32,
                    20
            );

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(
                    0,
                    0,
                    0,
                    16
            );

            tv.setLayoutParams(params);

            holder.layoutHours.addView(tv);
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

        public ViewHolder(@NonNull View itemView) {

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