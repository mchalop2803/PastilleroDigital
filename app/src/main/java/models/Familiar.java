package models;

public class Familiar extends DomainEntity{

    private String nombre;
    private String phone;
    private String relacion;

    /*
    * Constructor clase Familiar
    */
    public Familiar() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    @Override
    public String toString() {
        return "Familiar{" +
                "nombre='" + nombre + '\'' +
                ", phone='" + phone + '\'' +
                ", relacion='" + relacion + '\'' +
                '}';
    }
}
