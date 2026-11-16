package Torneo;
import combatiente.Combatiente;
import tecnica.Tecnica;
public class EventoAccionRealizada extends Evento
{
    private Combatiente ejecutor;
    private Combatiente objetivo;
    private Tecnica tecnicaUsada;

    public EventoAccionRealizada(Combatiente ejecutor, Combatiente objetivo, Tecnica tecnicaUsada)
    {
        super("El combatiente " + ejecutor + " realizo la tecnica " + tecnicaUsada + " sobre el combatiente " + objetivo);
        this.ejecutor = ejecutor;
        this.objetivo = objetivo;
        this.tecnicaUsada = tecnicaUsada;
    }

    public Combatiente getEjecutor() {
        return ejecutor;
    }

    public Combatiente getObjetivo() {
        return objetivo;
    }

    public Tecnica getTecnicaUsada() {
        return tecnicaUsada;
    }
}
