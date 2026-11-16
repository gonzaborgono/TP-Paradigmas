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
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) 
	{
		ejecutor.gastarEnergia(getCostoEnergia());
		objetivo.recibirDano(65);
	}

}
