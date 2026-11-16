package Torneo;

public class EventoResultadoCombate extends Evento
{
    private Pareja ganadora;
    private Pareja perdedora;
    private int vidaParejaGanadora;
    private int vidaParejaPerdedora;

    public EventoResultadoCombate(Pareja ganadora, Pareja perdedora, int vidaParejaGanadora, int vidaParejaPerdedora)
    {
        super("El combate ha finalizado. La pareja ganadora es: " + ganadora + " con " + vidaParejaGanadora + " de vida restante. La pareja perdedora es: " + perdedora + " con " + vidaParejaPerdedora + " de vida restante.");
        this.ganadora = ganadora;
        this.perdedora = perdedora;
        this.vidaParejaGanadora = vidaParejaGanadora;
        this.vidaParejaPerdedora = vidaParejaPerdedora;
    }
    public Pareja getGanadora()
    {
        return ganadora;
    }
    public Pareja getPerdedora()
    {
        return perdedora;
    }
}
