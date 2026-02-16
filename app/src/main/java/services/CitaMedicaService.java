package services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.CitaMedica;

public class CitaMedicaService {

    DatabaseReference databaseReference;
    public CitaMedicaService() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CitaMedicas");

    }

    public String insertCitaMedica(CitaMedica citaMedica){
        DatabaseReference newReference = databaseReference.push();
        citaMedica.setId(newReference.getKey());

        newReference.setValue(citaMedica);
        return citaMedica.getId();
    }



    public void updateCitaMedica(CitaMedica citaMedica){
        databaseReference.child(citaMedica.getId()).setValue(citaMedica);

    }
}
