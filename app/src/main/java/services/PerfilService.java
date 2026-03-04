package services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Perfil;

public class PerfilService {
    DatabaseReference databaseReference;
    public PerfilService(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("profiles");

    }

    public String insertProfile(Perfil perfil){
        DatabaseReference newReference = databaseReference.push();
        perfil.setId(newReference.getKey());

        newReference.setValue(perfil);
        return perfil.getId();
    }



    public void updateProfile(Perfil perfil){
        databaseReference.child(perfil.getId()).setValue(perfil);

    }

    public void deleteProfile(String id){
        databaseReference.child(id).removeValue();
    }
}
