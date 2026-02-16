package models;

public class Perfil extends DomainEntity{

    private String name;
    private String permiso;

    public Perfil() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "nombre='" + name + '\'' +
                ", permiso='" + permiso + '\'' +
                '}';
    }
}
