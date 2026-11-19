package efecto;
import combatiente.Combatiente;
public abstract class Efecto 
{
    private int turnosRestantes;
    private String nombre;
    public Efecto(String nombre, int turnos)
    {
        if (nombre == null || nombre.trim().isEmpty()) 
            throw new IllegalArgumentException("El nombre del efecto no puede ser nulo o vacío.");
        
        if (turnos <= 0) 
            throw new IllegalArgumentException("La duración en turnos debe ser mayor a 0.");
        
        this.turnosRestantes = turnos;
        this.nombre = nombre;
    }
    public boolean termino()
    {
        return turnosRestantes == 0;
    }

    protected abstract void aplicarEfecto(Combatiente combatiente);
    public void realizarTurno(Combatiente combatiente)
    {
        if(combatiente == null)
            throw new IllegalArgumentException("el combatiente no puede ser nulo");
        if(this.turnosRestantes > 0 && !combatiente.estaFueraDeCombate())
        {
            aplicarEfecto(combatiente);
            turnosRestantes--;
        }
    }
    public int getTurnosRestantes()
    {
        return turnosRestantes;
    }
    public String getNombre()
    {
        return nombre;
    }
    @Override
    public String toString()
    {
    	return nombre;
    }
}
