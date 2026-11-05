package combatiente;

import escuela.Escuela;
import tecnica.Tecnica;
import equipamiento.Equipamiento;


public class CreadorCombatientes {
    public Combatiente crear(String tipo, String nombre, Escuela escuela) {
        Combatiente nuevoCombatiente = null;

        switch(tipo) {
            case "Cultivador de la Espada":
                nuevoCombatiente = new CultivadorDeLaEspada(nombre);
                break;
            case "Monje del Cuerpo Celestial":
                nuevoCombatiente = new MonjeDelCuerpoCelestial(nombre);
                break;
            case "Hechicero del Vacío":
                nuevoCombatiente = new HechiceroDelVacio(nombre);
                break;
            default:
                throw new IllegalArgumentException("Tipo de combatiente desconocido: " + tipo);
        }

        
        for(Equipamiento item : escuela.getEquipamientos()) {
            nuevoCombatiente.equipar(item);
        }
        
        
        for(Tecnica tecnica : escuela.getTecnicas()) {
            nuevoCombatiente.agregarTecnica(tecnica); 
        }
	
	
        return nuevoCombatiente;
    }
}