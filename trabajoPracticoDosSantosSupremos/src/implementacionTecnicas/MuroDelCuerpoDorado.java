package implementacionTecnicas;
import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;
public class MuroDelCuerpoDorado extends Tecnica
{

	public MuroDelCuerpoDorado() 
	{
		super("Muro del cuerpo dorado", 30, TipoTecnica.DEFENSIVA);
	}

	@Override
	protected void aplicar(Combatiente ejecutor, Combatiente objetivo)
	{

		objetivo.activarDefensaTemporal(0.25);//25% menos de daño recibido durante este combate
	}

}
