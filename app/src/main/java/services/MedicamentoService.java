package services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Medicamento;

public class MedicamentoService {

    DatabaseReference databaseReference;
    public MedicamentoService() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Medicamentos");

    }

    public String insertMedicamento(Medicamento medicamento){
        DatabaseReference newReference = databaseReference.push();
        medicamento.setId(newReference.getKey());

        newReference.setValue(medicamento);
        return medicamento.getId();
    }



    public void updateMedicamento(Medicamento medicamento){
        databaseReference.child(medicamento.getId()).setValue(medicamento);

    }
}
