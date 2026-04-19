package receivers;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import models.CitaMedica;
import services.CitaMedicaService;

public class CalendarObserver extends ContentObserver {

    private final Context context;
    private final CitaMedicaService service;

    public CalendarObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        this.service = new CitaMedicaService(context);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        syncCalendarToFirebase();
    }

    private void syncCalendarToFirebase() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Cursor cursor = context.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor == null) return;

        service.getAllCitasByUser(uid, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                while (cursor.moveToNext()) {

                    String title = cursor.getString(
                            cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
                    );

                    String desc = cursor.getString(
                            cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)
                    );

                    boolean exists = false;

                    for (DataSnapshot data : snapshot.getChildren()) {

                        CitaMedica c = data.getValue(CitaMedica.class);

                        if (c != null && c.getDescription() != null
                                && c.getDescription().equals(desc)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        CitaMedica nueva = new CitaMedica();
                        nueva.setDescription(desc != null ? desc : title);

                        service.insertCitaMedica(nueva);
                    }
                }

                cursor.close();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}