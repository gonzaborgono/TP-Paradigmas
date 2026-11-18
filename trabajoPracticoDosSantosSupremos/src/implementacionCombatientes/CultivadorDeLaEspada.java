package implementacionCombatientes;

import combatiente.Combatiente;

public class CultivadorDeLaEspada extends Combatiente{

	public CultivadorDeLaEspada(String nombre) {
		/*
		 * vidaMax: 100 (Vida estandar)
		 * energiaMax: 40 (Energía estandar)
		 * multDañoRecibido: 1.0f (Daño normal)
		 * multDañoRealizado: 1.30f (Aumenta el daño un 30%)
		 * multGastoEnergia: 1.0f (Gasto normal)
		 */
		super(nombre, 100, 40, 1.0, 1.30, 1.0);
	}
	@Override 
	public void reaccionarCompaneroFueraDeCombate()
	{ //al morir su compañero gana mas ataque (20% mas)
		if(!this.tieneReaccionActiva())
		{
			this.activarReaccion();
			this.aumentarMultDanoRealizado(0.2);
		}
	}
	@Override
	public void acomodarFinCompaneroFueraDeCombate()
	{
		if(this.tieneReaccionActiva())
		{
			this.desactivarReaccion();
			this.disminuirMultDanoRealizado(0.2);
		}
	}
}
