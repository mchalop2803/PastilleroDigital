package models;

public class Alerta extends DomainEntity{

    private String userId;
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
                "userId='" + userId + '\'' +
                ", nombre='" + nombre + '\'' +
                ", hora='" + hora + '\'' +
                ", medicamentImageUrl='" + medicamentImageUrl + '\'' +
                ", medicamentoId='" + medicamentoId + '\'' +
                '}';
    }
}
