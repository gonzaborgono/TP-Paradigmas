package escuela;


import equipamiento.Equipamiento;
import java.util.List;
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
        if (tecnicas.contains(null))
            throw new IllegalArgumentException("no puede haber una tecnica nula");
        if (equipamientos.contains(null))
            throw new IllegalArgumentException("no puede haber un equipamiento nulo");
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
