package models;

import java.io.Serializable;

public class Alerta extends DomainEntity implements Serializable {

    private String userId;
    private String nombre;
    private long hora;

    private String medicamentImageUrl;
    private String dosisBase;
    private String frecuencia;

    private String estado;
    private String horaTomada;
    private String dosisTomada;

    private String medicamentoId;

    public Alerta() {
        super();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public long getHora() { return hora; }
    public void setHora(long hora) { this.hora = hora; }

    public String getMedicamentImageUrl() { return medicamentImageUrl; }
    public void setMedicamentImageUrl(String medicamentImageUrl) {
        this.medicamentImageUrl = medicamentImageUrl;
    }

    public String getDosisBase() { return dosisBase; }
    public void setDosisBase(String dosisBase) { this.dosisBase = dosisBase; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getHoraTomada() { return horaTomada; }
    public void setHoraTomada(String horaTomada) { this.horaTomada = horaTomada; }

    public String getDosisTomada() { return dosisTomada; }
    public void setDosisTomada(String dosisTomada) { this.dosisTomada = dosisTomada; }

    public String getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(String medicamentoId) {
        this.medicamentoId = medicamentoId;
    }
}