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
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) 
	{
		ejecutor.gastarEnergia(getCostoEnergia());
		objetivo.activarDefensaTemporal(0.25);//25% menos de daño recibido durante este combate
	}

}
