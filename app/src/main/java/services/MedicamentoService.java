package services;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import models.Medicamento;

public class MedicamentoService {

    DatabaseReference databaseReference;

    public MedicamentoService(Context context) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("medicaments");
    }

    public String insertMedicament(Medicamento medicamento) {
        DatabaseReference newReference = databaseReference.push();
        medicamento.setId(newReference.getKey());

        newReference.setValue(medicamento);
        return medicamento.getId();
    }

    public void updateMedicament(Medicamento medicamento) {
        databaseReference.child(medicamento.getId()).setValue(medicamento);
    }

    public void deleteMedicament(String medicamentoId) {

        if (medicamentoId == null || medicamentoId.isEmpty()) {
            Log.e("DELETE_MED", "ID vacío");
            return;
        }

        databaseReference.child(medicamentoId)
                .removeValue()
                .addOnSuccessListener(aVoid ->
                        Log.i("DELETE_MED", "OK"))
                .addOnFailureListener(e ->
                        Log.e("DELETE_MED", "ERROR", e));
    }

    public void getAllMedicamentosByUser(String userId, ValueEventListener listener) {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("medicaments")
                .addListenerForSingleValueEvent(listener);
    }
}