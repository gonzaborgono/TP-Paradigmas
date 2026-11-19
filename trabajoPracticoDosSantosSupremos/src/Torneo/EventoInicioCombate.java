package Torneo;

import java.util.List;

public class EventoInicioCombate extends Evento {
    private final int ronda;
    private final Pareja pareja1;
    private final Pareja pareja2;
    private final List<EstadoCombatiente> estadosIniciales;

    public EventoInicioCombate(int ronda, Pareja pareja1, Pareja pareja2) {
        super("Ronda " + ronda + ". Inicia el combate entre " + pareja1 + " y " + pareja2 + ".");
        this.ronda = ronda;
        this.pareja1 = pareja1;
        this.pareja2 = pareja2;
        estadosIniciales = List.of(new EstadoCombatiente(pareja1.getCombatiente1()),
                new EstadoCombatiente(pareja1.getCombatiente2()), new EstadoCombatiente(pareja2.getCombatiente1()),
                new EstadoCombatiente(pareja2.getCombatiente2()));
    }

    public int getRonda() {
        return ronda;
    }

    public Pareja getPareja1() {
        return pareja1;
    }

    public Pareja getPareja2() {
        return pareja2;
    }

    public List<EstadoCombatiente> getEstadosIniciales() {
        return estadosIniciales;
    }
}
