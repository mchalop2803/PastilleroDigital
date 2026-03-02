package services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Medicamento;

public class MedicamentoService {

    DatabaseReference databaseReference;
    public MedicamentoService(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("medicament");

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

    public void deleteMedicament(String id){
        databaseReference.child(id).removeValue();
    }
}
