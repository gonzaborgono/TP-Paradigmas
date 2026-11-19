package Torneo;

import combatiente.Combatiente;
import combatiente.CreadorCombatientes;
import cronista.CronistaDeLaSecta;
import escuela.Escuela;
import implementacionCombatientes.CultivadorDeLaEspada;
import implementacionTecnicas.CorteDeLosMilAmaneceres;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TorneoTest {
    private static final Escuela ESCUELA_DE_PRUEBA = new Escuela("Prueba", List.of(), List.of());

    @Test
    void parejaTieneDosIntegrantesDistintosYUnIdAsignadoAlCrearse() {
        Combatiente a = new CultivadorDeLaEspada("A", ESCUELA_DE_PRUEBA);
        Combatiente b = new CultivadorDeLaEspada("B", ESCUELA_DE_PRUEBA);
        assertThrows(IllegalArgumentException.class, () -> new Pareja(a, a));
        assertThrows(IllegalArgumentException.class, () -> new Pareja(a, null));
        Pareja primera = new Pareja(a, b);
        Pareja segunda = nuevaPareja("Segunda");
        assertTrue(segunda.getID() > primera.getID());
        assertSame(a, primera.getCombatiente1());
        assertSame(b, primera.getCombatiente2());

        a.disminuirVidaAct(100);
        assertFalse(primera.estaDerrotada());
        assertEquals(100, primera.getVidaTotal());
        b.disminuirVidaAct(100);
        assertTrue(primera.estaDerrotada());
        assertEquals(0, primera.getVidaTotal());
    }

    @Test
    void rechazaInscribirLaMismaParejaORepetirUnCombatiente() {
        Torneo torneo = new Torneo();
        Pareja original = nuevaPareja("Original");
        torneo.inscribir(original);
        assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(original));
        Pareja repetida = new Pareja(new CultivadorDeLaEspada("Otro", ESCUELA_DE_PRUEBA), original.getCombatiente2());
        assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(repetida));
        torneo.inscribir(nuevaPareja("Segunda"));
        torneo.inscribir(nuevaPareja("Tercera"));
        torneo.inscribir(nuevaPareja("Cuarta"));
        assertNotNull(torneo.iniciar());
        assertThrows(IllegalStateException.class, () -> torneo.inscribir(nuevaPareja("Fuera de plazo")));
        assertThrows(IllegalStateException.class, torneo::iniciar);
    }

    @Test
    void soloCuatroUOchoParejasPuedenIniciarYLaNovenaSeRechaza() {
        for (int cantidad : List.of(0, 1, 2, 3, 5, 6, 7)) {
            Torneo torneo = new Torneo();
            for (int i = 0; i < cantidad; i++) {
                torneo.inscribir(nuevaPareja("Pareja " + i));
            }
            assertThrows(IllegalStateException.class, torneo::iniciar);
        }
        Torneo torneo = new Torneo();
        for (Pareja pareja : parejas(8)) {
            torneo.inscribir(pareja);
        }
        assertThrows(IllegalStateException.class, () -> torneo.inscribir(nuevaPareja("Novena")));
        assertNotNull(torneo.iniciar());
    }

    @Test
    void crucesConCuatroRespetanInscripcionYGanadoresDeLaRondaAnterior() {
        verificarCruces(4);
    }

    @Test
    void crucesConOchoRespetanInscripcionYGanadoresDeLaRondaAnterior() {
        verificarCruces(8);
    }

    @Test
    void tablaOrdenaPorUltimaRondaVidaDescendenteEIdAscendente() {
        List<Pareja> parejas = parejas(8);
        Pareja a = parejas.get(0);
        Pareja b = parejas.get(1);
        Pareja c = parejas.get(2);
        Pareja d = parejas.get(3);
        Pareja e = parejas.get(4);
        Pareja f = parejas.get(5);
        Pareja g = parejas.get(6);
        Pareja h = parejas.get(7);
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        cronista.recibirEvento(new EventoResultadoCombate(1, a, b, 200, 180));
        cronista.recibirEvento(new EventoResultadoCombate(1, c, d, 190, 25));
        cronista.recibirEvento(new EventoResultadoCombate(1, e, f, 150, 100));
        cronista.recibirEvento(new EventoResultadoCombate(1, g, h, 110, 100));
        cronista.recibirEvento(new EventoResultadoCombate(2, a, c, 100, 60));
        cronista.recibirEvento(new EventoResultadoCombate(2, e, g, 90, 70));
        cronista.recibirEvento(new EventoResultadoCombate(3, a, e, 50, 40));

        assertEquals(List.of(a, e, g, c, b, f, h, d), cronista.obtenerTablaPosiciones());
    }

    @Test
    void dosTorneosEquivalentesProducenElMismoCampeonYLosMismosEventos() {
        CronistaDeLaSecta primero = new CronistaDeLaSecta();
        CronistaDeLaSecta segundo = new CronistaDeLaSecta();
        Pareja campeona = jugar(parejas(8), primero);
        Pareja otraCampeona = jugar(parejas(8), segundo);
        assertEquals(nombres(campeona), nombres(otraCampeona));
        assertEquals(historialDeJuego(primero), historialDeJuego(segundo));
        assertThrows(UnsupportedOperationException.class, () -> primero.getHistorial().clear());
    }

    @Test
    void lasCapturasDelHistorialNoCambianAlActualizarElCombatiente() {
        List<Pareja> parejas = parejas(4);
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        jugar(parejas, cronista);
        EventoInicioCombate inicio = (EventoInicioCombate) cronista.getHistorial().get(0);
        EventoAccionRealizada accion = (EventoAccionRealizada) cronista.getHistorial().get(1);
        Combatiente objetivoOriginal = inicio.getPareja2().getCombatiente1();
        assertEquals(objetivoOriginal.getId(), accion.getObjetivo().getId());
        int vidaRegistrada = accion.getObjetivo().getVida();
        objetivoOriginal.resetearEstado();
        assertNotEquals(objetivoOriginal.getVidaAct(), vidaRegistrada);
        assertEquals(vidaRegistrada, accion.getObjetivo().getVida());
    }

    @Test
    void elNuevoTorneoReponeUsosYReproduceElHistorialConLosMismosIds() {
        List<Pareja> parejas = parejas(4);
        CronistaDeLaSecta primero = new CronistaDeLaSecta();
        CronistaDeLaSecta segundo = new CronistaDeLaSecta();
        assertSame(jugar(parejas, primero), jugar(parejas, segundo));
        List<String> descripciones = new ArrayList<>();
        List<String> otrasDescripciones = new ArrayList<>();
        for (Evento evento : primero.getHistorial()) {
            descripciones.add(evento.getDescripcion());
        }
        for (Evento evento : segundo.getHistorial()) {
            otrasDescripciones.add(evento.getDescripcion());
        }
        assertEquals(descripciones, otrasDescripciones);
    }

    private static void verificarCruces(int cantidad) {
        List<Pareja> inscriptas = parejas(cantidad);
        // El orden de IDs difiere del de inscripción.
        inscriptas.add(0, inscriptas.remove(inscriptas.size() - 1));
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Pareja campeona = jugar(inscriptas, cronista);
        List<EventoInicioCombate> inicios = new ArrayList<>();
        List<EventoResultadoCombate> resultados = new ArrayList<>();
        for (Evento evento : cronista.getHistorial()) {
            if (evento instanceof EventoInicioCombate)
                inicios.add((EventoInicioCombate) evento);
            if (evento instanceof EventoResultadoCombate)
                resultados.add((EventoResultadoCombate) evento);
        }

        assertEquals(cantidad - 1, inicios.size());
        assertEquals(cantidad - 1, resultados.size());
        List<Pareja> actuales = inscriptas;
        int cruce = 0;
        int ronda = 1;
        while (actuales.size() > 1) {
            List<Pareja> siguientes = new ArrayList<>();
            for (int i = 0; i < actuales.size(); i += 2) {
                EventoInicioCombate inicio = inicios.get(cruce);
                EventoResultadoCombate resultado = resultados.get(cruce);
                assertEquals(ronda, inicio.getRonda());
                assertEquals(ronda, resultado.getRonda());
                assertSame(actuales.get(i), inicio.getPareja1());
                assertSame(actuales.get(i + 1), inicio.getPareja2());
                siguientes.add(resultado.getGanadora());
                cruce++;
            }
            actuales = siguientes;
            ronda++;
        }
        assertSame(actuales.get(0), campeona);
        assertEquals(cantidad, cronista.obtenerTablaPosiciones().size());
        assertSame(campeona, cronista.obtenerTablaPosiciones().get(0));
        Evento proclamacion = cronista.getHistorial().get(cronista.getHistorial().size() - 1);
        assertSame(campeona, assertInstanceOf(EventoProclamacion.class, proclamacion).getCampeona());
    }

    private static List<String> historialDeJuego(CronistaDeLaSecta cronista) {
        List<String> historial = new ArrayList<>();
        for (Evento evento : cronista.getHistorial()) {
            if (evento instanceof EventoInicioCombate) {
                EventoInicioCombate inicio = (EventoInicioCombate) evento;
                String detalle = inicio.getRonda() + " inicia " + nombres(inicio.getPareja1())
                        + " contra " + nombres(inicio.getPareja2());
                for (EstadoCombatiente estado : inicio.getEstadosIniciales()) {
                    detalle += " " + estadoDeJuego(estado);
                }
                historial.add(detalle);
            } else if (evento instanceof EventoAccionRealizada) {
                EventoAccionRealizada accion = (EventoAccionRealizada) evento;
                historial.add(accion.getRonda() + " " + accion.getAsalto() + " "
                        + estadoDeJuego(accion.getEjecutor()) + " " + accion.getAccion().getNombre()
                        + " " + estadoDeJuego(accion.getObjetivo()));
            } else if (evento instanceof EventoEliminacion) {
                EventoEliminacion eliminacion = (EventoEliminacion) evento;
                historial.add(eliminacion.getRonda() + " " + eliminacion.getAsalto()
                        + " cae " + estadoDeJuego(eliminacion.getEliminado()));
            } else if (evento instanceof EventoResultadoCombate) {
                EventoResultadoCombate resultado = (EventoResultadoCombate) evento;
                historial.add(resultado.getRonda() + " gana " + nombres(resultado.getGanadora()) + " "
                        + resultado.getVidaTotalGanadora() + " contra " + nombres(resultado.getPerdedora())
                        + " " + resultado.getVidaTotalPerdedora());
            } else {
                EventoProclamacion proclamacion = assertInstanceOf(EventoProclamacion.class, evento);
                historial.add("Campeona " + nombres(proclamacion.getCampeona()));
            }
        }
        return historial;
    }

    private static String estadoDeJuego(EstadoCombatiente estado) {
        return estado.getNombre() + " " + estado.getVida() + " " + estado.getEnergia();
    }

    private static String nombres(Pareja pareja) {
        return pareja.getCombatiente1().getNombre() + " y " + pareja.getCombatiente2().getNombre();
    }

    private static Pareja jugar(List<Pareja> parejas, CronistaDeLaSecta cronista) {
        Torneo torneo = new Torneo();
        torneo.registrarObservador(cronista);
        for (Pareja pareja : parejas) {
            torneo.inscribir(pareja);
        }
        return torneo.iniciar();
    }

    private static List<Pareja> parejas(int cantidad) {
        Escuela escuela = new Escuela("Espada", List.of(new CorteDeLosMilAmaneceres()), List.of());
        CreadorCombatientes creador = new CreadorCombatientes();
        List<Pareja> parejas = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            parejas.add(new Pareja(creador.crear("Cultivador de la Espada", "A" + i, escuela),
                    creador.crear("Monje del Cuerpo Celestial", "B" + i, escuela)));
        }
        return parejas;
    }

    private static Pareja nuevaPareja(String prefijo) {
        return new Pareja(new CultivadorDeLaEspada(prefijo + " A", ESCUELA_DE_PRUEBA), new CultivadorDeLaEspada(prefijo + " B", ESCUELA_DE_PRUEBA));
    }
}
