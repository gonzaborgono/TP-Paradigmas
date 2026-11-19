package implementacionCombatientes;

import combatiente.Combatiente;
import escuela.Escuela;

public class HechiceroDelVacio extends Combatiente {
    public HechiceroDelVacio(String nombre, Escuela escuela) {
        super(nombre, escuela, 70, 60, 1.0, 1.0, 1.0);
    }

    @Override
    public void reaccionarCompaneroFueraDeCombate() {
        activarReaccion();
    }

    @Override
    public double getMultGastoEnergia() {
        double multiplicador = super.getMultGastoEnergia() - 0.50;
        return tieneReaccionActiva() ? multiplicador - 0.20 : multiplicador;
    }
}
