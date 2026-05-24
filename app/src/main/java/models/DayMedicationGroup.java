package models;

import java.util.ArrayList;
import java.util.List;

public class DayMedicationGroup {

    private String medicationName;

    private List<Alerta> alerts = new ArrayList<>();
    private List<CitaMedica> citas = new ArrayList<>();
    private Medicamento medicamento;

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public List<Alerta> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alerta> alerts) {
        this.alerts = alerts;
    }

    public List<CitaMedica> getCitas() {
        return citas;
    }

    public void setCitas(List<CitaMedica> citas) {
        this.citas = citas;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
}