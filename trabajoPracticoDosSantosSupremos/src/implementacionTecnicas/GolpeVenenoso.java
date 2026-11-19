package implementacionTecnicas;

import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;
import implementacionEfectos.Veneno;
//tecnica agregada para efectos
public class GolpeVenenoso extends Tecnica
{
    public GolpeVenenoso()
    {
        super("Golpe venenoso", 25, TipoTecnica.OFENSIVA);
    }
    @Override
    protected void aplicar(Combatiente ejecutor, Combatiente objetivo)
	{

        //hace 10 de daño fisico
		int dano = ejecutor.calcularDanoRealizado(10);
		objetivo.recibirDano(dano);
		objetivo.agregarEfectoTemporal(new Veneno(5, 6));
	}
}
