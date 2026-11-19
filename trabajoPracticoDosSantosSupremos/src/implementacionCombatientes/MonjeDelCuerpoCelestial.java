package implementacionCombatientes;

import combatiente.Combatiente;
import escuela.Escuela;

public class MonjeDelCuerpoCelestial extends Combatiente {
    public MonjeDelCuerpoCelestial(String nombre, Escuela escuela) {
        super(nombre, escuela, 130, 30, 1.0, 1.0, 1.0);
    }

    @Override
    public void reaccionarCompaneroFueraDeCombate() {
        if (!tieneReaccionActiva()) {
            activarReaccion();
            aumentarVidaAct(25);
        }
    }

    @Override
    public double getMultDanoRecibido() {
        return super.getMultDanoRecibido() - 0.30;
    }

    @Override
    public double getMultDanoRealizado() {
        double multiplicador = super.getMultDanoRealizado();
        return tieneReaccionActiva() ? multiplicador + 0.15 : multiplicador;
    }
}
