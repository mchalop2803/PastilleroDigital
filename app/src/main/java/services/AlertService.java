package services;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Alerta;

public class AlertService {

    DatabaseReference databaseReference;
    public AlertService(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("alerts");

    }

    public String insertAlert(Alerta alerta){
        DatabaseReference newReference = databaseReference.push();
        alerta.setId(newReference.getKey());

        newReference.setValue(alerta);
        return alerta.getId();
    }



    public void updateAlert(Alerta alerta){
        databaseReference.child(alerta.getId()).setValue(alerta);

    }

    public void deleteAlert(String id){
        databaseReference.child(id).removeValue();
    }

    public void deleteAlertsByMedicamentoId(String medicamentoId, Runnable onComplete) {
        databaseReference
                .orderByChild("medicamentoId")
                .equalTo(medicamentoId)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (!snapshot.exists()) {
                        onComplete.run();
                        return;
                    }

                    int total = (int) snapshot.getChildrenCount();
                    final int[] deleted = {0};

                    for (DataSnapshot data : snapshot.getChildren()) {
                        data.getRef().removeValue().addOnCompleteListener(task -> {
                            deleted[0]++;

                            if (deleted[0] == total) {
                                onComplete.run();
                            }
                        });
                    }
                });
    }
}
