package implementacionCombatientes;

import combatiente.Combatiente;

public class MonjeDelCuerpoCelestial extends Combatiente{

	public MonjeDelCuerpoCelestial(String nombre) {
        /*
         * vidaMax: 130 (Mucha vida)
         * energiaMax: 30 (Poca energia)
         * multDañoRecibido: 0.70f (Reduce el daño un 30%)
         * multDañoRealizado: 1.0f (Daño normal)
         * multGastoEnergia: 1.0f (Gasto normal)
         */
        super(nombre, 130, 30, 0.70, 1.0, 1.0);
    }
    @Override 
	public void reaccionarCompaneroFueraDeCombate()
	{ //al morir su compañero se cura un poco y gana ataque
		if(!this.tieneReaccionActiva())
		{
			this.activarReaccion();
			this.aumentarVidaAct(25);
        	this.aumentarMultDanoRealizado(0.15);
		}
	}
	@Override
	public void acomodarFinCompaneroFueraDeCombate()
	{
		if(this.tieneReaccionActiva())
		{
			this.desactivarReaccion();
			this.disminuirMultDanoRealizado(0.15);
		}
	}
}
