package combatiente;

//solo creada para que ande el creadorCombatientes, falta implementar
//Stats sujetas a modificacion

public class HechiceroDelVacio extends Combatiente{

	public HechiceroDelVacio(String nombre) {
        /*
         * vidaMax: 70 (Poca vida)
         * energiaMax: 60 (Mucha energía)
         * multDañoRecibido: 1.0f (Daño normal)
         * multDañoRealizado: 1.0f (Daño normal)
         * multGastoEnergia: 0.50f (Tecnicas cuestan un 50% menos)
         */
        super(nombre, 70f, 60f, 1.0f, 1.0f, 0.50f);
    }

}
