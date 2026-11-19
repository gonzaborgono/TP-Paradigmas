package tecnica;

import combatiente.Combatiente;

public interface Accion {
    String getNombre();
    void ejecutar(Combatiente ejecutor, Combatiente objetivo);
}
