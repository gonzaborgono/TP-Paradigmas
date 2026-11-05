package combatiente;

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
        super(nombre, 130f, 30f, 0.70f, 1.0f, 1.0f);
    }
}
