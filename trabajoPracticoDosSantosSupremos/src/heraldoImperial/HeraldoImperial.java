package heraldoImperial;
import Torneo.Evento;
import Torneo.ObservadorTorneo;
import Torneo.Pareja;
import java.util.List;
public class HeraldoImperial implements ObservadorTorneo 
{
    @Override
    public void recibirEvento(Evento e) 
    {
        if (e == null) 
            throw new IllegalArgumentException("el evento no puede ser nulo");
        System.out.println(e.getDescripcion());
    }

    public void mostrarTablaPosiciones(List<Pareja> tabla) 
    {
        if (tabla == null || tabla.isEmpty()) 
            throw new IllegalArgumentException("la tabla no puede ser nula o estar vacia");

        System.out.println("\n=== TABLA DE POSICIONES DEL TORNEO ===");
        for (int i = 0; i < tabla.size(); i++) 
        {
            System.out.println((i + 1) + "° Puesto: " + tabla.get(i));
        }
        System.out.println("======================================\n");
    }
}
