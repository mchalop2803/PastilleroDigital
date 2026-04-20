package models;


import java.io.Serializable;

public class Medicamento extends DomainEntity implements Serializable {
    private String userId;
    private String nombre;
    private String horario;
    private String dosis;
    private String momentDay;
    private String imageUrl;

    /*
    * Constructor clase Medicamento
    */

    public Medicamento() {
        super();
    }

    /*
    * Getters y Setters
    */

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "userId='" + userId + '\'' +
                ", nombre='" + nombre + '\'' +
                ", horario='" + horario + '\'' +
                ", dosis='" + dosis + '\'' +
                ", momentDay='" + momentDay + '\'' +
                '}';
    }
}
