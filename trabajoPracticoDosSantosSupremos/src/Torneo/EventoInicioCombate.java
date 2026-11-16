package Torneo;

public class EventoInicioCombate extends Evento
{
    private Pareja pareja1;
    private Pareja pareja2;

    public EventoInicioCombate(Pareja pareja1, Pareja pareja2)
    {
        super("se inicio la pelea entre las parejas: " + pareja1 + pareja2);
        this.pareja1 = pareja1;
        this.pareja2 = pareja2;
    }

    public Pareja getPareja1() {
        return pareja1;
    }
    public Pareja getPareja2() {
        return pareja2;
    }
}
