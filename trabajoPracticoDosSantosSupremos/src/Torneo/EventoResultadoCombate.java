package Torneo;

public class EventoResultadoCombate extends Evento
{
    private Pareja ganadora;
    private Pareja perdedora;
    private int vidaParejaGanadora;
    private int vidaParejaPerdedora;
    private int ronda;
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
