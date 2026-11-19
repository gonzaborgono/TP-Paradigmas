package Torneo;

import combatiente.Combatiente;

public class EventoEliminacion extends Evento {
    private final int ronda;
    private final int asalto;
    private final EstadoCombatiente eliminado;

    public EventoEliminacion(int ronda, int asalto, Combatiente eliminado) {
        super("Asalto " + asalto + ". " + eliminado + " queda fuera de combate.");
        this.ronda = ronda;
        this.asalto = asalto;
        this.eliminado = new EstadoCombatiente(eliminado);
    }

    public int getRonda() {
        return ronda;
    }

    public int getAsalto() {
        return asalto;
    }

    public EstadoCombatiente getEliminado() {
        return eliminado;
    }
}
