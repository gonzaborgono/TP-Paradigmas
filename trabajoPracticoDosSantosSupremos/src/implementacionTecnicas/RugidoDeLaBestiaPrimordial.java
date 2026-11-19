package implementacionTecnicas;

import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class RugidoDeLaBestiaPrimordial extends Tecnica 
{

	public RugidoDeLaBestiaPrimordial() 
	{
		super("Rugido de la bestia primordial" , 55, TipoTecnica.OFENSIVA);
	}

	@Override
	protected void aplicar(Combatiente ejecutor, Combatiente objetivo)
	{

		int dano = ejecutor.calcularDanoRealizado(65);
		objetivo.recibirDano(dano);
	}

}
