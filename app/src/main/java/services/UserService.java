package services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.User;

public class UserService {

    private final DatabaseReference reference;
    private String insertResult;

    public UserService(Context context) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        reference = database.getReference().child("users");
    }

    public String insertUser(User user) {
        Thread thr = new Thread(() -> {

            DatabaseReference newReference = reference.push();

            //asignamos el id generado
            user.setId(newReference.getKey());

            //insertamos el usuario en la base de datos
            newReference.setValue(user);

            insertResult = user.getId();
        });
        thr.start();
        try {
            thr.join();
        } catch (InterruptedException e) {
            Log.e("UserService", "Error al añadir el usuario " + e.toString());
        }
        return insertResult;
    }


    public void updateUser(User user) {
        reference.child(user.getId()).setValue(user);

    }

    public boolean updatephoto(User user, Uri imageUri) {

        try {
            DatabaseReference photoRef = reference.child(user.getId()).child("photo");
            photoRef.setValue(imageUri.toString());

        } catch (Exception e) {
            Log.e("Error updating photo", e.toString());
            String error = "Error updating photo: " + e.toString();
            //    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    public void deleteUser(String id) {
        reference.child(id).removeValue();
    }
}
