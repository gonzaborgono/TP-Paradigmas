package implementacionTecnicas;

import combatiente.Combatiente;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class AtaqueBasico extends Tecnica
{
	public AtaqueBasico() 
	{
		super("Ataque basico", 0, TipoTecnica.OFENSIVA);
	}
	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) 
	{
		int dano = ejecutor.calcularDanoRealizado(15);//daño bajo
		objetivo.recibirDano(dano); 
	}

}
