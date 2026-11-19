package cronista;
import Torneo.Pareja;
//esta default para que tengan acceso solo desde el paquete (osea solo el cronista)
class RegistroFinal implements Comparable<RegistroFinal>
{
    private final Pareja pareja;
    private final int ultimaRonda;
    private final int vidaFinal;

    RegistroFinal(Pareja pareja, int ultimaRonda, int vidaFinal) 
    {
        this.pareja = pareja;
        this.ultimaRonda = ultimaRonda;
        this.vidaFinal = vidaFinal;
    }

    @Override 
    public int compareTo(RegistroFinal otro)
    {
        /*consigna:
        las parejas ordenadas por la última
        ronda del torneo que jugaron, en empate por la vida total con la que terminaron su último
        combate y luego por identificador.
         */
        if(this.ultimaRonda != otro.ultimaRonda)
            return Integer.compare(otro.ultimaRonda, this.ultimaRonda);
        if(this.vidaFinal != otro.vidaFinal)
            return Integer.compare(otro.vidaFinal, this.vidaFinal);
        return Integer.compare(this.pareja.getID(), otro.pareja.getID());
    }
    public Pareja getPareja()
    {
        return pareja;
    }
}
