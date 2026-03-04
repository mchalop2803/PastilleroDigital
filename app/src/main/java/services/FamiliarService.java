package services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Familiar;

public class FamiliarService {

    DatabaseReference databaseReference;
    public FamiliarService(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("familys");

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

    public void deleteFamiliar(String id){
        databaseReference.child(id).removeValue();
    }
}
