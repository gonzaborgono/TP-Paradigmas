package implementacionTecnicas;

import combatiente.Combatiente;
import implementacionEfectos.Regeneracion;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class CirculacionDelQi extends Tecnica
{
    public CirculacionDelQi()
    {
        super("Circulacion del qi", 10, TipoTecnica.CURACION);
    }
    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) 
	{
		ejecutor.gastarEnergia(getCostoEnergia());
        //se cura por 5 turnos 8 de vida cada turno
        objetivo.agregarEfectoTemporal(new Regeneracion(8, 5));
	}
}
