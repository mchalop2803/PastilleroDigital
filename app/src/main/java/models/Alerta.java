package models;

import android.media.Image;

import java.time.LocalTime;

public class Alerta extends DomainEntity{

    private String nombre;
    private String hora;
    private Image medicamentImage;

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

    public Image getMedicamentImage() {
        return medicamentImage;
    }

    public void setMedicamentImage(Image medicamentImage) {
        this.medicamentImage = medicamentImage;
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
                ", medicamentImage=" + medicamentImage +
                '}';
    }
}
