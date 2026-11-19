package Torneo;

import combatiente.Combatiente;

public class Pareja {
    private static int proximoId = 1;

    private final int id;
    private final Combatiente combatiente1;
    private final Combatiente combatiente2;

    public Pareja(Combatiente combatiente1, Combatiente combatiente2) {
        if (combatiente1 == null || combatiente2 == null || combatiente1.equals(combatiente2)) {
            throw new IllegalArgumentException("La pareja necesita dos combatientes distintos.");
        }
        id = proximoId++;
        this.combatiente1 = combatiente1;
        this.combatiente2 = combatiente2;
    }

    public int getID() {
        return id;
    }

    public Combatiente getCombatiente1() {
        return combatiente1;
    }

    public Combatiente getCombatiente2() {
        return combatiente2;
    }

    public boolean estaDerrotada() {
        return combatiente1.estaFueraDeCombate() && combatiente2.estaFueraDeCombate();
    }

    public int getVidaTotal() {
        return combatiente1.getVidaAct() + combatiente2.getVidaAct();
    }

    @Override
    public String toString() {
        return "Pareja #" + id + " [" + combatiente1.getNombre() + " y " + combatiente2.getNombre() + "]";
    }
}
