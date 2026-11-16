package implementacionCombatientes;

import combatiente.Combatiente;

//solo creada para que ande el creadorCombatientes, falta implementar
//Stats sujetas a modificacion

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
	{ //al morir su compañero gana mas resistencia (20% mas)
		if(!this.tieneReaccionActiva())
		{
			this.activarReaccion();
			this.disminuirMultDanoRecibido(0.2);
		}
	}
	@Override
	public void acomodarFinCompaneroFueraDeCombate()
	{
		if(this.tieneReaccionActiva())
		{
			this.desactivarReaccion();
			this.aumentarMultDanoRecibido(0.2);
		}
	}
}
