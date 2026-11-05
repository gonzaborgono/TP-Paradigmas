package tecnica;

import combatiente.Combatiente;

public interface Accion {
	void ejecutar(Combatiente ejecutor, Combatiente objetivo);
}
