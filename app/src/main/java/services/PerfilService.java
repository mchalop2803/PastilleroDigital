package services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Perfil;

public class PerfilService {
    DatabaseReference databaseReference;
    public PerfilService() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Perfiles");

    }

    public String insertPerfil(Perfil perfil){
        DatabaseReference newReference = databaseReference.push();
        perfil.setId(newReference.getKey());

        newReference.setValue(perfil);
        return perfil.getId();
    }



    public void updatePerfil(Perfil perfil){
        databaseReference.child(perfil.getId()).setValue(perfil);

    }
}
