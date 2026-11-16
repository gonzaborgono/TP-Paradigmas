package implementacionTecnicas;

import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class MeditacionDelnmortal extends Tecnica 
{

	public MeditacionDelnmortal() {
		super("Meditacion del inmortal", 30, TipoTecnica.CURACION);
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		ejecutor.gastarEnergia(getCostoEnergia());
		objetivo.aumentarVidaAct(35);
	}

}
