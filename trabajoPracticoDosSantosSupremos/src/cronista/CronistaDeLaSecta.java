package cronista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Torneo.*;

public class CronistaDeLaSecta implements ObservadorTorneo 
{
    private List<Evento> historial;
    private List<EventoResultadoCombate> resultados;

    public CronistaDeLaSecta() 
    {
        this.historial = new ArrayList<Evento>();
        this.resultados = new ArrayList<EventoResultadoCombate>();
    }

    @Override
    public void recibirEvento(Evento e) 
    {
        if (e == null) 
            throw new IllegalArgumentException("el evento no puede ser nulo");
        this.historial.add(e);

        if (e instanceof EventoResultadoCombate)
        {
            this.resultados.add((EventoResultadoCombate) e);
        }
    }

    public List<Evento> getHistorial()
    {
        return List.copyOf(this.historial);
    }

    public List<Pareja> obtenerTablaPosiciones()
    {
        Map<Pareja, RegistroFinal> ultimosRegistros = new HashMap<Pareja, RegistroFinal>();

        //leer los resultados para dejar solo el final
        //como estan ordenados de forma cronologica lo ultimo que haya de cada pareja es donde quedaron
        //uso el map para pisar el resultado dependiendo de cada pareja
        for (EventoResultadoCombate res : this.resultados) 
        {
            int ronda = res.getRonda();
            ultimosRegistros.put(res.getGanadora(), new RegistroFinal(res.getGanadora(), ronda, res.getVidaTotalGanadora()));
            ultimosRegistros.put(res.getPerdedora(), new RegistroFinal(res.getPerdedora(), ronda, res.getVidaTotalPerdedora()));
        }

        //hacer una lista en base a el hashmap
        List<RegistroFinal> ranking = new ArrayList<>(ultimosRegistros.values());

        //ordenar en base a ronda (mayor a menor), vidaTotal (mayor a menor) y sino ID (menor a mayor)
        //ya implementa compareTo asi que no le mando otro comparador
        ranking.sort(null);

        //ahora hacer lista con solo las parejas
        List<Pareja> tablaOrdenada = new ArrayList<>();
        for (RegistroFinal reg : ranking)
        {
            tablaOrdenada.add(reg.getPareja());
        }

        return tablaOrdenada;
    }
}
