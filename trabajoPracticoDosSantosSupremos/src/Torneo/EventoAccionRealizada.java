package Torneo;

import combatiente.Combatiente;
import tecnica.Accion;

public class EventoAccionRealizada extends Evento {
    private final int ronda;
    private final int asalto;
    private final EstadoCombatiente ejecutor;
    private final EstadoCombatiente objetivo;
    private final Accion accion;

    public EventoAccionRealizada(int ronda, int asalto, Combatiente ejecutor, Combatiente objetivo, Accion accion) {
        super("Asalto " + asalto + ". " + ejecutor + " usa " + accion.getNombre() + " sobre " + objetivo
                + " (vida restante: " + objetivo.getVidaAct() + ").");
        this.ronda = ronda;
        this.asalto = asalto;
        this.ejecutor = new EstadoCombatiente(ejecutor);
        this.objetivo = new EstadoCombatiente(objetivo);
        this.accion = accion;
    }

    public int getRonda() {
        return ronda;
    }

    public int getAsalto() {
        return asalto;
    }

    public EstadoCombatiente getEjecutor() {
        return ejecutor;
    }

    public EstadoCombatiente getObjetivo() {
        return objetivo;
    }

    public Accion getAccion() {
        return accion;
    }
}
