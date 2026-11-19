package implementacionCombatientes;

import combatiente.Combatiente;
import escuela.Escuela;

public class CultivadorDeLaEspada extends Combatiente {
    public CultivadorDeLaEspada(String nombre, Escuela escuela) {
        super(nombre, escuela, 100, 40, 1.0, 1.0, 1.0);
    }

    @Override
    public void reaccionarCompaneroFueraDeCombate() {
        activarReaccion();
    }

    @Override
    public double getMultDanoRealizado() {
        double multiplicador = super.getMultDanoRealizado() + 0.30;
        return tieneReaccionActiva() ? multiplicador + 0.20 : multiplicador;
    }
}
