package services;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

import models.Alerta;

public class AlertService {

    private final DatabaseReference ref;

    public AlertService(Context context) {

        String uid = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("alerts");
    }

    public String insertAlert(Alerta alerta) {

        DatabaseReference newRef = ref.push();
        alerta.setId(newRef.getKey());

        newRef.setValue(alerta);
        return alerta.getId();
    }

    public void updateAlert(Alerta alerta) {
        ref.child(alerta.getId()).setValue(alerta);
    }

    public void deleteAlert(String id) {
        ref.child(id).removeValue();
    }

    public void getAlertsByMedicamentoId(String medicamentoId,
                                         AlertsCallback callback) {

        ref.orderByChild("medicamentoId")
                .equalTo(medicamentoId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        java.util.List<Alerta> list = new java.util.ArrayList<>();

                        for (DataSnapshot d : snapshot.getChildren()) {

                            Alerta a = d.getValue(Alerta.class);
                            if (a != null) {
                                a.setId(d.getKey());
                                list.add(a);
                            }
                        }

                        callback.onResult(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onResult(new java.util.ArrayList<>());
                    }
                });
    }

    public interface AlertsCallback {
        void onResult(java.util.List<Alerta> alerts);
    }
}