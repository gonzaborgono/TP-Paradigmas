package implementacionTecnicas;

import combatiente.Combatiente;
import tecnica.Accion;

public class AtaqueBasico implements Accion {
    @Override
    public String getNombre() {
        return "Ataque básico";
    }

    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        if (ejecutor.estaFueraDeCombate() || objetivo.estaFueraDeCombate())
            throw new IllegalStateException("Los combatientes de la acción deben estar en pie.");
        objetivo.recibirDano(ejecutor.calcularDanoRealizado(15));
    }
}
