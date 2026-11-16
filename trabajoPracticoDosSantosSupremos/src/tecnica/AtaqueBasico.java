package tecnica;

import combatiente.Combatiente;

public class AtaqueBasico implements Accion {

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		objetivo.recibirDano(15); //daño bajo
	}

}
