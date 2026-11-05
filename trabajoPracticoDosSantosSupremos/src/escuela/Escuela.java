package escuela;

import combatiente.Combatiente;
import equipamiento.Equipamiento;
import equipamiento.TipoEquipamiento;
import tecnica.Tecnica;

import java.util.List;

public class Escuela {
    private final String  nombre;
    private final List<Tecnica> tecnicas;
    private final List<Equipamiento> equipamientos;

    public Escuela(String nombre, List<Tecnica> tecnicas, List<Equipamiento> equipamientos)
    {
        this.nombre = nombre;
        this.tecnicas = List.copyOf(tecnicas);
        this.equipamientos = List.copyOf(equipamientos);
    }
    public void preparar(Combatiente combatiente)
    {
        for(Equipamiento equipamiento : equipamientos)
        {
            combatiente.equipar(equipamiento);
        }
        for(Tecnica tecnica: tecnicas)
        {
            combatiente.agregarTecnica(tecnica);
        }
    }
    public String getNombre()
    {
        return this.nombre;
    }


    public List<Tecnica> getTecnicas() {
        return tecnicas;
    }

    public List<Equipamiento> getEquipamientos() {
        return equipamientos;
    }
}
