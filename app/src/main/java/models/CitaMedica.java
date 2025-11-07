package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaMedica extends DomainEntity{

    private String description;
    private String acompañante;
    private String medico;
    private LocalTime hora;
    private LocalDate fecha;
    private String location;

    /*
    * Constructor clase CitaMedica
    */

    public CitaMedica() {
        super();
    }

    /*
    * Getters y Setters
    */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcompañante() {
        return acompañante;
    }

    public void setAcompañante(String acompañante) {
        this.acompañante = acompañante;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CitaMedica{" +
                "description='" + description + '\'' +
                ", acompañante='" + acompañante + '\'' +
                ", medico='" + medico + '\'' +
                ", hora=" + hora +
                ", fecha=" + fecha +
                ", location='" + location + '\'' +
                '}';
    }
}
