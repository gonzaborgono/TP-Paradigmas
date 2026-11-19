package Torneo;

public abstract class Evento {
    private final String descripcion;

    protected Evento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
