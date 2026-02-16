package services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Familiar;

public class FamiliarService {

    DatabaseReference databaseReference;
    public FamiliarService() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Familiar");

    }

    public String insertFamiliar(Familiar familiar){
        DatabaseReference newReference = databaseReference.push();
        familiar.setId(newReference.getKey());

        newReference.setValue(familiar);
        return familiar.getId();
    }



    public void updateFamiliar(Familiar familiar){
        databaseReference.child(familiar.getId()).setValue(familiar);

    }
}
