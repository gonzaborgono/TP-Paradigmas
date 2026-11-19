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
	protected void aplicar(Combatiente ejecutor, Combatiente objetivo)
	{

		int dano = ejecutor.calcularDanoRealizado(30);
		objetivo.recibirDano(dano);
		objetivo.disminuirEnergiaAct(20);
	}

}
