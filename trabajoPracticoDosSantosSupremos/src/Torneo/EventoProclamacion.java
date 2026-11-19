package Torneo;

public class EventoProclamacion extends Evento
{
    private final Pareja campeona;
    public EventoProclamacion(Pareja campeona)
    {
        super("Los Dos Santos Supremos son los integrantes de " + campeona);
        this.campeona = campeona;
    }
    public Pareja getCampeona()
    {
        return campeona;
    }
}
