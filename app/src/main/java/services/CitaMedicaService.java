package services;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import models.CitaMedica;

public class CitaMedicaService {

    DatabaseReference databaseReference;
    public CitaMedicaService(Context context) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("citaMedics");
    }

    public void getCitasMedicasByMonth(Context context, int month, ValueEventListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("citaMedics");

        ref.addListenerForSingleValueEvent(listener);
    }

    public void getAllCitasByUser(String userId, ValueEventListener listener){
        databaseReference
                .orderByChild("userId")
                .equalTo(userId)
                .addValueEventListener(listener);
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
