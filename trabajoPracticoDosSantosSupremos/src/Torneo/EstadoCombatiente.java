package Torneo;

import combatiente.Combatiente;

public final class EstadoCombatiente {
    private final int id;
    private final String nombre;
    private final int vida;
    private final int energia;

    public EstadoCombatiente(Combatiente combatiente) {
        id = combatiente.getId();
        nombre = combatiente.getNombre();
        vida = combatiente.getVidaAct();
        energia = combatiente.getEnergiaAct();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getEnergia() {
        return energia;
    }

    @Override
    public String toString() {
        return nombre + " #" + id + " (vida " + vida + ", energía " + energia + ")";
    }
}
