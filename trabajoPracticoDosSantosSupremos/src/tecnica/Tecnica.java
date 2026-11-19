package tecnica;

import combatiente.Combatiente;
import java.util.Objects;

public abstract class Tecnica implements Accion {
    private final String nombre;
    private final int costoEnergia;
    private final TipoTecnica tipo;

    protected Tecnica(String nombre, int costoEnergia, TipoTecnica tipo) {
        if (nombre == null || nombre.trim().isEmpty() || costoEnergia < 0 || tipo == null) {
            throw new IllegalArgumentException("La técnica necesita nombre, costo válido y tipo.");
        }
        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
        this.tipo = tipo;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public int getCostoEnergia() {
        return costoEnergia;
    }

    public TipoTecnica getTipo() {
        return tipo;
    }

    @Override
    public final void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        if (ejecutor.estaFueraDeCombate() || objetivo.estaFueraDeCombate())
            throw new IllegalStateException("Los combatientes de la acción deben estar en pie.");
        ejecutor.gastarEnergia(costoEnergia);
        aplicar(ejecutor, objetivo);
    }

    protected abstract void aplicar(Combatiente ejecutor, Combatiente objetivo);

    @Override
    public final boolean equals(Object otro) {
        if (this == otro)
            return true;
        if (otro == null || getClass() != otro.getClass())
            return false;
        return nombre.equals(((Tecnica) otro).nombre);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
