package implementacionCombatientes;

import combatiente.Combatiente;

public class HechiceroDelVacio extends Combatiente{

	public HechiceroDelVacio(String nombre) {
        /*
         * vidaMax: 70 (Poca vida)
         * energiaMax: 60 (Mucha energía)
         * multDañoRecibido: 1.0f (Daño normal)
         * multDañoRealizado: 1.0f (Daño normal)
         * multGastoEnergia: 0.50f (Tecnicas cuestan un 50% menos)
         */
        super(nombre, 70, 60, 1.0, 1.0, 0.50);
    }
    @Override 
	public void reaccionarCompaneroFueraDeCombate()
	{ //al morir su compañero gasta menos energia (20% menos)
		if(!this.tieneReaccionActiva())
		{
			this.activarReaccion();
			this.disminuirMultGastoEnergia(0.2);
		}
	}
	@Override
	public void acomodarFinCompaneroFueraDeCombate()
	{
		if(this.tieneReaccionActiva())
		{
			this.desactivarReaccion();
			this.aumentarMultGastoEnergia(0.2);
		}
	}
}
