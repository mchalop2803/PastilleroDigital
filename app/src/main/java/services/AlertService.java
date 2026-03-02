package services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Alerta;

public class AlertService {

    DatabaseReference databaseReference;
    public AlertService(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("alert");

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
}
