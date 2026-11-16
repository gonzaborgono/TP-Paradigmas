package Torneo;
import combatiente.Combatiente;
public class EventoEliminacion extends Evento
{
    private Combatiente eliminado;
    public EventoEliminacion(Combatiente eliminado)
    {
        super("El combatiente " + eliminado + " ha sido derrotado");
        this.eliminado = eliminado;
    }
    public Combatiente getEliminado()
    {
        return eliminado;
    }
}
