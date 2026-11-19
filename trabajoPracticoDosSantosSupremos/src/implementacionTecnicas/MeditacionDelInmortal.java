package implementacionTecnicas;

import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class MeditacionDelInmortal extends Tecnica 
{

	public MeditacionDelInmortal() {
		super("Meditacion del inmortal", 30, TipoTecnica.CURACION);
	}

	@Override
	protected void aplicar(Combatiente ejecutor, Combatiente objetivo) {

		objetivo.aumentarVidaAct(35);
	}

}
