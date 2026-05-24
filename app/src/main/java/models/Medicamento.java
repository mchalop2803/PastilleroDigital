package models;

import java.io.Serializable;

public class Medicamento extends DomainEntity implements Serializable {

    private String userId;
    private String nombre;
    private String description;
    private String imageUrl;

    private long fechaInicio;
    private long fechaFin;

    public Medicamento() {
        super();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(long fechaInicio) { this.fechaInicio = fechaInicio; }

    public long getFechaFin() { return fechaFin; }
    public void setFechaFin(long fechaFin) { this.fechaFin = fechaFin; }
}