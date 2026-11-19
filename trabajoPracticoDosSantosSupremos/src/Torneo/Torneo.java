package Torneo;

import combatiente.Combatiente;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Torneo {
    public static final int MIN_PAREJAS = 4;
    public static final int MAX_PAREJAS = 8;

    private final List<Pareja> parejas = new ArrayList<>();
    private final Set<Combatiente> inscriptos = new HashSet<>();
    private final List<ObservadorTorneo> observadores = new ArrayList<>();
    private boolean iniciado;

    public void registrarObservador(ObservadorTorneo observador) {
        if (observador == null)
            throw new IllegalArgumentException("El observador no puede ser nulo.");
        if (!observadores.contains(observador))
            observadores.add(observador);
    }

    public void inscribir(Pareja pareja) {
        if (iniciado)
            throw new IllegalStateException("El torneo ya empezó.");
        if (pareja == null)
            throw new IllegalArgumentException("La pareja no puede ser nula.");
        if (parejas.contains(pareja))
            throw new IllegalArgumentException("La pareja ya está inscripta.");

        Combatiente primero = pareja.getCombatiente1();
        Combatiente segundo = pareja.getCombatiente2();
        if (inscriptos.contains(primero) || inscriptos.contains(segundo))
            throw new IllegalArgumentException("Un combatiente ya está inscripto en otra pareja.");
        if (parejas.size() >= MAX_PAREJAS)
            throw new IllegalStateException("El torneo admite como máximo ocho parejas.");

        parejas.add(pareja);
        inscriptos.add(primero);
        inscriptos.add(segundo);
    }

    public Pareja iniciar() {
        if (iniciado)
            throw new IllegalStateException("El torneo ya fue iniciado.");
        if (parejas.size() != MIN_PAREJAS && parejas.size() != MAX_PAREJAS)
            throw new IllegalStateException("El torneo solo puede iniciar con 4 u 8 parejas.");

        iniciado = true;
        for (Combatiente combatiente : inscriptos) {
            combatiente.reiniciarUsosTecnicas();
        }

        List<Pareja> rondaActual = new ArrayList<>(parejas);
        int ronda = 1;
        while (rondaActual.size() > 1) {
            List<Pareja> siguienteRonda = new ArrayList<>();
            for (int i = 0; i < rondaActual.size(); i += 2) {
                Combate combate = new Combate(ronda, rondaActual.get(i), rondaActual.get(i + 1), observadores);
                siguienteRonda.add(combate.desarrollarCombate());
            }
            rondaActual = siguienteRonda;
            ronda++;
        }

        Pareja campeona = rondaActual.get(0);
        Evento proclamacion = new EventoProclamacion(campeona);
        for (ObservadorTorneo observador : List.copyOf(observadores)) {
            observador.recibirEvento(proclamacion);
        }
        return campeona;
    }
}
