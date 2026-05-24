package receivers;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.example.pastillerodigital.R;

import java.util.Calendar;

import activities.DetailsAlertaActivity;
import models.Alerta;

public class AlarmReceiver extends BroadcastReceiver {

    public static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {

        Alerta alerta = (Alerta) intent.getSerializableExtra("alerts");

        if (alerta == null) return;

        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }, 60000);

        Intent openIntent = new Intent(context, DetailsAlertaActivity.class);
        openIntent.putExtra("alerts", alerta);
        openIntent.putExtra("fromAlarm", true);

        openIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        int requestCode = (int) System.currentTimeMillis();

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                requestCode,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "alarm_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Alarmas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("⏰ Alarm")
                        .setContentText("Tap to view alert")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setOngoing(true);

        manager.notify(requestCode, builder.build());

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent newIntent = new Intent(context, AlarmReceiver.class);
        newIntent.putExtra("alerts", alerta);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alerta.getId().hashCode(),
                newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alerta.getHora());

        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                Intent perm = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                perm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(perm);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
}