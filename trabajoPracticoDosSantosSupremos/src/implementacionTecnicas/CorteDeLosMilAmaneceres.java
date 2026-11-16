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
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) 
	{
		ejecutor.gastarEnergia(this.getCostoEnergia());
		int dano = ejecutor.calcularDanoRealizado(45);
		objetivo.recibirDano(dano);
	}
}
