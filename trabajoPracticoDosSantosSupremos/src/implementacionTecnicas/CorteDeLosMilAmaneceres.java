package implementacionTecnicas;

import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class CorteDeLosMilAmaneceres extends Tecnica
{

	public CorteDeLosMilAmaneceres() 
	{
		super("Corte de los mil amaneceres", 20, TipoTecnica.OFENSIVA);
	}
	@Override
	protected void aplicar(Combatiente ejecutor, Combatiente objetivo)
	{

		int dano = ejecutor.calcularDanoRealizado(45);
		objetivo.recibirDano(dano);
	}
}
