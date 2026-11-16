package implementacionTecnicas;
import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;
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
		int dano = ejecutor.calcularDanoRealizado(30);
		objetivo.recibirDano(dano);
		objetivo.disminuirEnergiaAct(20);
	}

}
