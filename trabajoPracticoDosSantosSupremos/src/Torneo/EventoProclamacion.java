package Torneo;

public class EventoProclamacion extends Evento
{
    private Pareja campeona;
    public EventoProclamacion(Pareja campeona)
    {
        super("La pareja campeona del torneo es: " + campeona);
        this.campeona = campeona;
    }
    public Pareja getCampeona()
    {
        return campeona;
    }
}
