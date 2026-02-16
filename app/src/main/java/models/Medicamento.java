package models;

import android.media.Image;

import java.time.LocalTime;

public class Medicamento extends DomainEntity{

    private String nombre;
    private String horario;
    private String dosis;
    private String duracion;
    private Image imagen;

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

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "nombre='" + nombre + '\'' +
                ", horario=" + horario +
                ", dosis='" + dosis + '\'' +
                ", duracion='" + duracion + '\'' +
                ", imagen=" + imagen +
                '}';
    }
}
