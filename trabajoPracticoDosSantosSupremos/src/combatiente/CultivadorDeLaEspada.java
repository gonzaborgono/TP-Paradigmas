package combatiente;

//solo creada para que ande el creadorCombatientes, falta implementar
//Stats sujetas a modificacion

public class CultivadorDeLaEspada extends Combatiente{

	public CultivadorDeLaEspada(String nombre) {
		/*
		 * vidaMax: 100f (Vida estandar)
		 * energiaMax: 40f (Energía estandar)
		 * multDañoRecibido: 1.0f (Daño normal)
		 * multDañoRealizado: 1.30f (Aumenta el daño un 30%)
		 * multGastoEnergia: 1.0f (Gasto normal)
		 */
		super(nombre, 100f, 40f, 1.0f, 1.30f, 1.0f);
	}

}
