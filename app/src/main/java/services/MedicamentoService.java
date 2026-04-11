package services;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Medicamento;

public class MedicamentoService {

    DatabaseReference databaseReference;
    public MedicamentoService(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("medicaments");

    }

    public String insertMedicament(Medicamento medicamento){
        DatabaseReference newReference = databaseReference.push();
        medicamento.setId(newReference.getKey());

        newReference.setValue(medicamento);
        return medicamento.getId();
    }



    public void updateMedicament(Medicamento medicamento){
        databaseReference.child(medicamento.getId()).setValue(medicamento);

    }

    public void deleteMedicament(String medicamentoId) {

        if (medicamentoId == null || medicamentoId.isEmpty()) {
            Log.e("DELETE_MEDICAMENT", "ID es null o vacío");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("medicaments");

        ref.child(medicamentoId)
                .removeValue()
                .addOnSuccessListener(aVoid ->
                        Log.i("DELETE_MEDICAMENT", "Medicamento eliminado correctamente"))
                .addOnFailureListener(e ->
                        Log.e("DELETE_MEDICAMENT", "Error al eliminar", e));
    }
}
