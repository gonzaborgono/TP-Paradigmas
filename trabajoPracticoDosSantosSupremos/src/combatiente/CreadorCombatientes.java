package combatiente;

import equipamiento.Equipamiento;
import escuela.Escuela;
import implementacionCombatientes.CultivadorDeLaEspada;
import implementacionCombatientes.HechiceroDelVacio;
import implementacionCombatientes.MonjeDelCuerpoCelestial;
import tecnica.Tecnica;

public class CreadorCombatientes {
    private static int proximoId = 1;

    static int asignarId() {
        return proximoId++;
    }

    public Combatiente crear(String tipo, String nombre, Escuela escuela) {
        if (tipo == null || escuela == null) {
            throw new IllegalArgumentException("Se requiere un tipo y una escuela.");
        }

        Combatiente combatiente;
        switch (tipo) {
            case "Cultivador de la Espada":
                combatiente = new CultivadorDeLaEspada(nombre, escuela);
                break;
            case "Monje del Cuerpo Celestial":
                combatiente = new MonjeDelCuerpoCelestial(nombre, escuela);
                break;
            case "Hechicero del Vacío":
                combatiente = new HechiceroDelVacio(nombre, escuela);
                break;
            default:
                throw new IllegalArgumentException("Tipo de combatiente desconocido: " + tipo);
        }

        for (Equipamiento item : escuela.getEquipamientos()) {
            combatiente.equipar(item);
        }
        for (Tecnica tecnica : escuela.getTecnicas()) {
            combatiente.agregarTecnica(tecnica);
        }
        return combatiente;
    }
}
