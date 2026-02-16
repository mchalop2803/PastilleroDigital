package services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Alerta;

public class AlertService {

    DatabaseReference databaseReference;
    public AlertService() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Alerts");

    }

    public String insertAlert(Alerta alerta){
        DatabaseReference newReference = databaseReference.push();
        alerta.setId(newReference.getKey());

        newReference.setValue(alerta);
        return alerta.getId();
    }



    public void updateAlerta(Alerta alerta){
        databaseReference.child(alerta.getId()).setValue(alerta);

    }
}
