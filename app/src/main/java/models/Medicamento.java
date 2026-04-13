package models;

import android.media.Image;

import java.io.Serializable;
import java.time.LocalTime;

public class Medicamento extends DomainEntity implements Serializable {

    private String nombre;
    private String horario;
    private String dosis;
    private String momentDay;

    /*
    * Constructor clase Medicamento
    */

    public Medicamento() {
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getMomentDay() {
        return momentDay;
    }

    public void setMomentDay(String momentDay) {
        this.momentDay = momentDay;
    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "nombre='" + nombre + '\'' +
                ", horario='" + horario + '\'' +
                ", dosis='" + dosis + '\'' +
                ", momentDay='" + momentDay + '\'' +
                '}';
    }
}
