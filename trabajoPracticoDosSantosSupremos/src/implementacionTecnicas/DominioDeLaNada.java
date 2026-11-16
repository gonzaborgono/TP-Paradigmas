package implementacionTecnicas;
import tecnica.Tecnica;
import tecnica.TipoTecnica;
import combatiente.Combatiente;
public class DominioDeLaNada extends Tecnica
{

	public DominioDeLaNada() 
	{
		super("Dominio de la nada", 35, TipoTecnica.OFENSIVA);
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) 
	{
		ejecutor.gastarEnergia(getCostoEnergia());
		objetivo.recibirDano(30);
		objetivo.disminuirEnergiaAct(20);
	}

}
