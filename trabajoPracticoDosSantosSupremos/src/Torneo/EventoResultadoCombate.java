package Torneo;

public class EventoResultadoCombate extends Evento
{
    private final Pareja ganadora;
    private final Pareja perdedora;
    private final int vidaParejaGanadora;
    private final int vidaParejaPerdedora;
    private final int ronda;
    public EventoResultadoCombate(int ronda, Pareja ganadora, Pareja perdedora, int vidaParejaGanadora, int vidaParejaPerdedora)
    {
        super("El combate de la ronda " + ronda + " ha finalizado. La pareja ganadora es: " + ganadora + " con " + vidaParejaGanadora + " de vida restante. La pareja perdedora es: " + perdedora + " con " + vidaParejaPerdedora + " de vida restante.");
        this.ronda = ronda;
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
    public int getVidaTotalGanadora()
    {
        return vidaParejaGanadora;
    }
    public int getVidaTotalPerdedora()
    {
        return vidaParejaPerdedora;
    }
    public int getRonda()
    {
        return ronda;
    }
}
