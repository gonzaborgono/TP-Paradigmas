package escuela;


import combatiente.Combatiente;
import equipamiento.Equipamiento;
import equipamiento.TipoEquipamiento;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tecnica.Tecnica;

public class Escuela 
{
    private final String  nombre;
    private final List<Tecnica> tecnicas;
    private final List<Equipamiento> equipamientos;

    public Escuela(String nombre, List<Tecnica> tecnicas, List<Equipamiento> equipamientos)
    {
        if (nombre == null || nombre.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("La escuela debe tener un nombre válido.");
        }
        if (tecnicas == null || equipamientos == null)
            throw new IllegalArgumentException("La escuela necesita listas de técnicas y equipos.");
        Set<Tecnica> tecnicasUnicas = new HashSet<>();
        for (Tecnica tecnica : tecnicas) {
            if (tecnica == null || !tecnicasUnicas.add(tecnica))
                throw new IllegalArgumentException("Las técnicas iniciales deben ser válidas y distintas.");
        }
        if (equipamientos.size() > Combatiente.MAX_EQUIPAMIENTOS)
            throw new IllegalArgumentException("Una escuela puede preparar hasta tres equipos.");
        Set<TipoEquipamiento> categorias = new HashSet<>();
        for (Equipamiento equipo : equipamientos) {
            if (equipo == null || !categorias.add(equipo.getTipoEquipamiento()))
                throw new IllegalArgumentException("El equipo inicial debe tener categorías distintas.");
        }
        this.nombre = nombre;
        this.tecnicas = List.copyOf(tecnicas);
        this.equipamientos = List.copyOf(equipamientos);
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
