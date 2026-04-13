package models;

import android.media.Image;

import java.time.LocalTime;

public class Alerta extends DomainEntity{

    private String nombre;
    private String hora;
    private String medicamentImageUrl;

    private String medicamentoId;



    /*
     * Constructor clase Alerta
     */

    public Alerta() {
        super();
    }

    /*
     * Getters y Setters
     */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMedicamentImageUrl() {
        return medicamentImageUrl;
    }

    public void setMedicamentImageUrl(String medicamentImageUrl) {
        this.medicamentImageUrl = medicamentImageUrl;
    }

    public String getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(String medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    @Override
    public String toString() {
        return "Alerta{" +
                "nombre='" + nombre + '\'' +
                ", hora=" + hora +
                ", medicamentImageUrl=" + medicamentImageUrl +
                '}';
    }
}
