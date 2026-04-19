package activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.provider.CalendarContract;
import java.text.ParseException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pastillerodigital.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import models.Alerta;
import models.CitaMedica;
import receivers.CalendarObserver;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome, tvNextAlert, tvNextCita;
    private CalendarObserver calendarObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadNextAlert();
        loadNextCita();

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_slide);

        findViewById(R.id.cardAlert).startAnimation(anim);
        findViewById(R.id.cardCita).startAnimation(anim);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvNextAlert = findViewById(R.id.tvNextAlert);
        tvNextCita = findViewById(R.id.tvNextCita);

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        String name = prefs.getString("name", "Usuario");

        tvWelcome.setText("Bienvenido, " + name);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.medicament) {
                Intent intApp = new Intent(this, ListMedicamentActivity.class);
                startActivity(intApp);
            } else if (id == R.id.alert) {
                Intent intApp = new Intent(this, ListAlertActivity.class);
                startActivity(intApp);
            } else if (id == R.id.profile) {
                startActivity(new Intent(this, DetailsProfileActivity.class));
                finish();
            } else {
                return false;
            }

            return true;
        });

        NavigationView navView = findViewById(R.id.nvMenu);

        View headerView = navView.getHeaderView(0);

        TextView tvName = headerView.findViewById(R.id.tvUserName);
        TextView tvEmail = headerView.findViewById(R.id.tvUserEmail);

        tvName.setText(prefs.getString("name", "Usuario"));
        tvEmail.setText(prefs.getString("email", "email@email.com"));

        Button logoutButton = navView.findViewById(R.id.btnLogout);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences = getSharedPreferences("Prefs", MODE_PRIVATE);
            preferences.edit().clear().apply();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        navView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.profile){
                Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DetailsProfileActivity.class));
                finish();
            } else if (itemId == R.id.medication) {
                Toast.makeText(this, "Medicamentos", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ListMedicamentActivity.class));
                finish();
            } else if (itemId == R.id.agenda) {
                Toast.makeText(this, "Agenda", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                finish();
            }else if (itemId == R.id.days) {
                Toast.makeText(this, "Pastillero diario", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ListDaysActivity.class));
                finish();
            } else if (itemId == R.id.familiar) {
                Toast.makeText(this, "Familiares", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ListFamilyActivity.class));
                finish();
            } else if (itemId == R.id.citaMedica) {

                Toast.makeText(this, "Cita Medica", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, CitaMedicaActivity.class));
                finish();
            }

            return false;
        });

        calendarObserver = new CalendarObserver(new Handler(), this);

        getContentResolver().registerContentObserver(
                CalendarContract.Events.CONTENT_URI,
                true,
                calendarObserver
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (calendarObserver != null) {
            getContentResolver().unregisterContentObserver(calendarObserver);
        }
    }

    private void loadNextAlert() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("alerts")
                .get()
                .addOnSuccessListener(snapshot -> {

                    long now = System.currentTimeMillis();
                    long oneHourLater = now + (60 * 60 * 1000);

                    Alerta nextAlert = null;
                    long minTime = Long.MAX_VALUE;

                    for (DataSnapshot data : snapshot.getChildren()) {
                        Alerta alerta = data.getValue(Alerta.class);

                        if (alerta != null && alerta.getHora() != null) {
                            try {
                                String[] parts = alerta.getHora().split(":");
                                int hour = Integer.parseInt(parts[0]);
                                int minute = Integer.parseInt(parts[1]);

                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, hour);
                                cal.set(Calendar.MINUTE, minute);
                                cal.set(Calendar.SECOND, 0);

                                long alertTime = cal.getTimeInMillis();

                                if (alertTime < now) {
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    alertTime = cal.getTimeInMillis();
                                }

                                if (alertTime >= now && alertTime <= oneHourLater) {
                                    if (alertTime < minTime) {
                                        minTime = alertTime;
                                        nextAlert = alerta;
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (nextAlert != null) {

                        long diff = (minTime - now) / (60 * 1000);

                        tvNextAlert.setText(
                                nextAlert.getNombre() + " - " + nextAlert.getHora()
                        );

                        if (diff <= 30) {
                            showNotification(
                                    "💊 Próxima toma",
                                    "Te queda " + diff + " min para " + nextAlert.getNombre()
                            );
                        }

                    } else {
                        tvNextAlert.setText("No hay alarmas en la próxima hora");
                    }
                });
    }

    private void loadNextCita() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("citaMedics")
                .get()
                .addOnSuccessListener(snapshot -> {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    long now = System.currentTimeMillis();

                    CitaMedica nextCita = null;
                    long minTime = Long.MAX_VALUE;

                    for (DataSnapshot data : snapshot.getChildren()) {

                        CitaMedica cita = data.getValue(CitaMedica.class);

                        if (cita == null) continue;

                        try {
                            Date fecha = sdf.parse(cita.getFecha() + " " + cita.getHora());
                            long citaTime = fecha.getTime();

                            if (citaTime > now && citaTime < minTime) {
                                minTime = citaTime;
                                nextCita = cita;
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (nextCita != null) {

                        tvNextCita.setText(
                                nextCita.getFecha() + " - " +
                                        nextCita.getHora() + "\n" +
                                        nextCita.getDescription()
                        );

                        CitaMedica finalNextCita = nextCita;
                        tvNextCita.setOnClickListener(v -> openCalendar(finalNextCita));

                    } else {
                        tvNextCita.setText("No hay citas próximas");
                        tvNextCita.setOnClickListener(null);
                    }

                });
    }

    private void openCalendar(CitaMedica cita) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = sdf.parse(cita.getFecha() + " " + cita.getHora());

            long millis = date.getTime();

            Uri uri = Uri.parse("content://com.android.calendar/time/" + millis);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();

            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://calendar.google.com/calendar")
            ));
        }
    }

    private long getMillisFromCita(CitaMedica cita) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = sdf.parse(cita.getFecha() + " " + cita.getHora());
            return date.getTime();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    private void openCalendarDayView(CitaMedica cita) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = sdf.parse(cita.getFecha() + " " + cita.getHora());

            long millis = date.getTime();

            Uri uri = Uri.parse("content://com.android.calendar/time/" + millis);

            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setData(uri)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://calendar.google.com/calendar/u/0/r/day"));

            startActivity(browserIntent);
        }
    }

    private void showNotification(String title, String message) {

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "alerts_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Alertas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}