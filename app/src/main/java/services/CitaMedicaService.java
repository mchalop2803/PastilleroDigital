package services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.CitaMedica;

public class CitaMedicaService {

    DatabaseReference databaseReference;
    public CitaMedicaService(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("citaMedics");

    }

    public String insertCitaMedica(CitaMedica citaMedica){
        DatabaseReference newReference = databaseReference.push();
        citaMedica.setId(newReference.getKey());

        newReference.setValue(citaMedica);
        return citaMedica.getId();
    }



    public void updateCitMedic(CitaMedica citaMedica){
        databaseReference.child(citaMedica.getId()).setValue(citaMedica);

    }

    public void deleteCitMedic(String id){
        databaseReference.child(id).removeValue();
    }
}
