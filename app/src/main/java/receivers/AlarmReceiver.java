package receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.example.pastillerodigital.R;

import java.util.Calendar;

import activities.DetailsAlertaActivity;
import models.Alerta;

public class AlarmReceiver extends BroadcastReceiver {

    public static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        Alerta alerta = (Alerta) intent.getSerializableExtra("alerts");

        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        Intent i = new Intent(context, DetailsAlertaActivity.class);
        i.putExtra("alerts", alerta);
        i.putExtra("fromAlarm", true);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent newIntent = new Intent(context, AlarmReceiver.class);
        newIntent.putExtra("alerts", alerta);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alerta.getId().hashCode(),
                newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        String[] parts = alerta.getHora().split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }
}