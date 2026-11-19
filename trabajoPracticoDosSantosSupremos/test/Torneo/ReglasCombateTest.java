package Torneo;

import combatiente.Combatiente;
import cronista.CronistaDeLaSecta;
import escuela.Escuela;
import implementacionCombatientes.CultivadorDeLaEspada;
import implementacionCombatientes.HechiceroDelVacio;
import implementacionCombatientes.MonjeDelCuerpoCelestial;
import implementacionEquipamiento.SelloDelFenix;
import implementacionTecnicas.AtaqueBasico;
import implementacionTecnicas.CorteDeLosMilAmaneceres;
import implementacionTecnicas.GolpeVenenoso;
import implementacionTecnicas.MeditacionDelInmortal;
import implementacionTecnicas.MuroDelCuerpoDorado;
import implementacionTecnicas.RugidoDeLaBestiaPrimordial;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

import static org.junit.jupiter.api.Assertions.*;

class ReglasCombateTest {
    private static final Escuela ESCUELA_DE_PRUEBA = new Escuela("Prueba", List.of(), List.of());

    @Test
    void ordenaParejasEIntegrantesPorIdInclusoSiSePasanInvertidos() {
        Combatiente a = resistente("A");
        Combatiente b = resistente("B");
        Combatiente c = resistente("C");
        Combatiente d = resistente("D");
        Pareja primera = new Pareja(b, a);
        Pareja segunda = new Pareja(d, c);
        CronistaDeLaSecta cronista = combatir(segunda, primera);
        List<EventoAccionRealizada> acciones = acciones(cronista);

        assertEquals(a.getId(), acciones.get(0).getEjecutor().getId());
        assertEquals(b.getId(), acciones.get(1).getEjecutor().getId());
        assertEquals(c.getId(), acciones.get(2).getEjecutor().getId());
        assertEquals(d.getId(), acciones.get(3).getEjecutor().getId());
        assertEquals(c.getId(), acciones.get(0).getObjetivo().getId());
        assertEquals(2, acciones.get(4).getAsalto());
    }

    @Test
    void enfocaAlRivalConMenosVidaYOmiteACaidos() {
        Combatiente a = new CombatienteControlado("A", 500, 60);
        Combatiente b = new CombatienteControlado("B", 500, 60);
        Combatiente c = new CombatienteControlado("C", 55, 0);
        Combatiente d = new CombatienteControlado("D", 40, 0);
        Pareja primera = new Pareja(a, b);
        Pareja segunda = new Pareja(c, d);
        CronistaDeLaSecta cronista = combatir(primera, segunda);
        List<EventoAccionRealizada> acciones = acciones(cronista);

        assertEquals(2, acciones.size());
        assertEquals(d.getId(), acciones.get(0).getObjetivo().getId());
        assertEquals(c.getId(), acciones.get(1).getObjetivo().getId());
        assertEquals(0, acciones.get(0).getObjetivo().getVida());
        assertEquals(0, acciones.get(1).getObjetivo().getVida());
        assertTrue(segunda.estaDerrotada());
        assertSame(primera, resultado(cronista).getGanadora());
        assertEquals(0, resultado(cronista).getVidaTotalPerdedora());
    }

    @Test
    void ataqueBasicoSiempreDisponibleNoConsumeEnergiaNiUsos() {
        Combatiente a = resistente("A");
        Combatiente b = resistente("B");
        Pareja primera = new Pareja(a, b);
        CronistaDeLaSecta cronista = combatir(primera, parejaResistente());
        List<EventoAccionRealizada> acciones = acciones(cronista);

        assertEquals(4 * Combate.LIMITE_ASALTOS_DEFAULT, acciones.size());
        for (EventoAccionRealizada accion : acciones) {
            assertInstanceOf(AtaqueBasico.class, accion.getAccion());
            assertEquals(40, accion.getEjecutor().getEnergia());
        }
        assertTrue(a.getTecnicas().isEmpty());
        assertTrue(b.getTecnicas().isEmpty());
    }

    @Test
    void usaBasicoSiNoAlcanzaEnergia() {
        Combatiente a = resistente("A");
        Tecnica rugido = new RugidoDeLaBestiaPrimordial();
        a.agregarTecnica(rugido);
        CronistaDeLaSecta cronista = combatir(new Pareja(a, resistente("B")), parejaResistente());

        assertInstanceOf(AtaqueBasico.class, accionDe(cronista, a, 1).getAccion());
        assertEquals(5, a.getUsosRestantes(rugido));
    }

    @Test
    void bloqueaLaMismaTecnicaDeOtraInstanciaYLuegoLaLiberaAlAgotarSusUsos() {
        Combatiente a = resistente("A");
        Combatiente b = resistente("B");
        Tecnica primera = new TecnicaDePrueba("Compartida", 0, TipoTecnica.OFENSIVA);
        Tecnica equivalente = new TecnicaDePrueba("Compartida", 0, TipoTecnica.OFENSIVA);
        a.agregarTecnica(primera);
        b.agregarTecnica(equivalente);
        CronistaDeLaSecta cronista = combatir(new Pareja(a, b), parejaResistente());

        assertEquals(primera, accionDe(cronista, a, 1).getAccion());
        assertInstanceOf(AtaqueBasico.class, accionDe(cronista, b, 1).getAccion());
        assertInstanceOf(AtaqueBasico.class, accionDe(cronista, b, 4).getAccion());
        assertEquals(equivalente, accionDe(cronista, b, 5).getAccion());
        assertInstanceOf(AtaqueBasico.class, accionDe(cronista, a, 6).getAccion());
        assertEquals(0, a.getUsosRestantes(primera));
        assertEquals(0, b.getUsosRestantes(equivalente));
    }

    @Test
    void liberaLaTecnicaSiCaeQuienLaUso() {
        Combatiente a = new CombatienteControlado("A", 40, 0);
        Combatiente b = resistente("B");
        Tecnica compartida = new TecnicaDePrueba("Compartida", 0, TipoTecnica.OFENSIVA);
        a.agregarTecnica(compartida);
        b.agregarTecnica(compartida);
        Pareja propia = new Pareja(a, b);
        Pareja rival = new Pareja(new CombatienteControlado("C", 500, 45), resistente("D"));
        CronistaDeLaSecta cronista = combatir(propia, rival);

        assertInstanceOf(AtaqueBasico.class, accionDe(cronista, b, 1).getAccion());
        assertEquals(compartida, accionDe(cronista, b, 2).getAccion());
        assertEquals(4, a.getUsosRestantes(compartida));
        assertTrue(a.estaFueraDeCombate());
    }

    @Test
    void recuperaVidaYEnergiaParaElSiguienteCruceSinReponerUsosNiMantenerBloqueo() {
        Combatiente a = new CombatienteControlado("A", 40, 0);
        Combatiente b = resistente("B");
        Tecnica compartida = new TecnicaDePrueba("Compartida", 25, TipoTecnica.OFENSIVA);
        a.agregarTecnica(compartida);
        b.agregarTecnica(compartida);
        Pareja propia = new Pareja(a, b);
        Pareja rival = new Pareja(new CombatienteControlado("C", 500, 45), resistente("D"));
        combatir(propia, rival);
        assertEquals(4, a.getUsosRestantes(compartida));
        assertTrue(a.estaFueraDeCombate());
        assertEquals(15, a.getEnergiaAct(), "Un caído conserva su energía hasta el próximo combate.");
        assertEquals(0, b.getUsosRestantes(compartida));

        CronistaDeLaSecta segundo = combatir(propia, parejaResistente());
        EventoInicioCombate inicio = (EventoInicioCombate) segundo.getHistorial().get(0);
        assertEquals(40, inicio.getEstadosIniciales().get(0).getVida());
        assertEquals(40, inicio.getEstadosIniciales().get(0).getEnergia());
        assertEquals(40, inicio.getEstadosIniciales().get(1).getEnergia());
        assertEquals(compartida, accionDe(segundo, a, 1).getAccion());
        assertInstanceOf(AtaqueBasico.class, accionDe(segundo, b, 1).getAccion());
        assertEquals(0, a.getUsosRestantes(compartida));
    }

    @Test
    void bloqueoPorUsoTerminaAlCambiarDeCombateAunqueElCompaneroSigaEnPie() {
        Combatiente primero = resistente("Primero");
        Combatiente segundo = new CombatienteControlado("Segundo", 500, 20);
        Tecnica tecnica = new TecnicaDePrueba("Cara", 40, TipoTecnica.OFENSIVA, 10);
        Tecnica golpe = new TecnicaDePrueba("Otra", 0, TipoTecnica.OFENSIVA);
        primero.agregarTecnica(golpe);
        primero.agregarTecnica(tecnica);
        segundo.agregarTecnica(tecnica);
        Pareja propia = new Pareja(primero, segundo);
        Pareja rival = new Pareja(new CombatienteControlado("C", 11, 0),
                new CombatienteControlado("D", 20, 0));
        CronistaDeLaSecta inicial = combatir(propia, rival);
        assertEquals(tecnica, accionDe(inicial, segundo, 1).getAccion());
        assertEquals(golpe, accionDe(inicial, primero, 2).getAccion());
        assertEquals(5, primero.getUsosRestantes(tecnica));
        assertEquals(4, segundo.getUsosRestantes(tecnica));

        CronistaDeLaSecta siguiente = combatir(propia, parejaResistente());
        assertEquals(tecnica, accionDe(siguiente, primero, 1).getAccion());
        assertInstanceOf(AtaqueBasico.class, accionDe(siguiente, segundo, 1).getAccion());
    }

    @Test
    void recuperaDiezDeEnergiaPorAsaltoSinSuperarElMaximo() {
        Combatiente a = resistente("A");
        a.agregarTecnica(new TecnicaDePrueba("Costosa", 35, TipoTecnica.OFENSIVA));
        CronistaDeLaSecta cronista = combatir(new Pareja(a, resistente("B")), parejaResistente());

        assertEquals(5, accionDe(cronista, a, 1).getEjecutor().getEnergia());
        assertEquals(15, accionDe(cronista, a, 2).getEjecutor().getEnergia());
        assertEquals(0, accionDe(cronista, a, 4).getEjecutor().getEnergia());
        assertEquals(40, accionDe(cronista, a, 20).getEjecutor().getEnergia());
    }

    @Test
    void priorizaCuracionLuegoDefensaYAplicaAmbasAlEjecutor() {
        Combatiente golpeador = new CombatienteControlado("Golpeador", 500, 20);
        Pareja primera = new Pareja(golpeador, resistente("B"));
        Combatiente defensor = new MonjeDelCuerpoCelestial("Defensor", ESCUELA_DE_PRUEBA);
        defensor.agregarTecnica(new CorteDeLosMilAmaneceres());
        defensor.agregarTecnica(new MuroDelCuerpoDorado());
        defensor.agregarTecnica(new MeditacionDelInmortal());
        Combatiente sano = resistente("Sano");
        sano.agregarTecnica(new MuroDelCuerpoDorado());
        sano.agregarTecnica(new MeditacionDelInmortal());
        CronistaDeLaSecta cronista = combatir(primera, new Pareja(defensor, sano));

        EventoAccionRealizada curacion = accionDe(cronista, defensor, 1);
        EventoAccionRealizada defensa = accionDe(cronista, sano, 1);
        assertInstanceOf(MeditacionDelInmortal.class, curacion.getAccion());
        assertEquals(defensor.getId(), curacion.getObjetivo().getId());
        assertEquals(defensor.getVidaMax(), curacion.getObjetivo().getVida());
        assertInstanceOf(MuroDelCuerpoDorado.class, defensa.getAccion());
        assertEquals(sano.getId(), defensa.getObjetivo().getId());
        assertEquals(4, sano.getUsosRestantes(new MuroDelCuerpoDorado()));
    }

    @Test
    void ofensivasSeEligenPorUsosYDespuesPorOrdenDeEscuela() {
        Combatiente a = resistente("A");
        Tecnica primera = new TecnicaDePrueba("Primera", 0, TipoTecnica.OFENSIVA);
        Tecnica segunda = new TecnicaDePrueba("Segunda", 0, TipoTecnica.OFENSIVA);
        a.agregarTecnica(primera);
        a.agregarTecnica(segunda);
        CronistaDeLaSecta cronista = combatir(new Pareja(a, resistente("B")), parejaResistente());

        assertEquals(primera, accionDe(cronista, a, 1).getAccion());
        assertEquals(segunda, accionDe(cronista, a, 2).getAccion());
        assertEquals(primera, accionDe(cronista, a, 3).getAccion());
    }

    @Test
    void alLimiteGanaLaMayorVidaTotalYLuegoElIdMenor() {
        Pareja menorId = parejaResistente();
        Pareja mayorId = parejaResistente();
        EventoResultadoCombate empate = resultado(combatir(mayorId, menorId));
        assertSame(menorId, empate.getGanadora());
        assertEquals(empate.getVidaTotalGanadora(), empate.getVidaTotalPerdedora());

        Pareja masVida = new Pareja(new CombatienteControlado("C", 501, 0), resistente("D"));
        EventoResultadoCombate diferencia = resultado(combatir(menorId, masVida));
        assertSame(masVida, diferencia.getGanadora());
        assertEquals(1001, diferencia.getVidaTotalGanadora());
        assertEquals(1000, diferencia.getVidaTotalPerdedora());
    }

    @Test
    void permiteCambiarEquipoEntreCrucesYLoRechazaDuranteElCombate() {
        Combatiente a = resistente("A");
        Pareja primera = new Pareja(a, resistente("B"));
        SelloDelFenix sello = new SelloDelFenix();
        new Combate(1, primera, parejaResistente(), List.of(evento -> {
            if (evento instanceof EventoInicioCombate) {
                assertThrows(IllegalStateException.class, () -> a.equipar(sello));
            } else if (evento instanceof EventoResultadoCombate) {
                a.equipar(sello);
            }
        })).desarrollarCombate();
        assertEquals(530, a.getVidaMax());
        assertEquals(500, a.getVidaAct());
        CronistaDeLaSecta siguiente = combatir(primera, parejaResistente());
        EventoInicioCombate inicio = (EventoInicioCombate) siguiente.getHistorial().get(0);
        assertEquals(530, inicio.getEstadosIniciales().get(0).getVida());
        a.desequipar(sello);
        assertEquals(500, a.getVidaAct());
    }

    @Test
    void bonusReactivoPotenciaUnaAccionDespuesDeLaCaida() {
        Combatiente caido = new HechiceroDelVacio("Caído", ESCUELA_DE_PRUEBA);
        Combatiente espada = new CultivadorDeLaEspada("Espada", ESCUELA_DE_PRUEBA);
        Pareja rival = new Pareja(new CombatienteControlado("Atacante", 500, 75), resistente("Apoyo"));
        CronistaDeLaSecta cronista = combatir(rival, new Pareja(caido, espada));

        assertEquals(477, accionDe(cronista, espada, 1).getObjetivo().getVida());
        assertEquals(457, accionDe(cronista, espada, 2).getObjetivo().getVida());
        assertFalse(espada.tieneReaccionActiva());
    }

    @Test
    void venenoPuedeEliminarAlAfectadoAntesDeSuAccion() {
        Combatiente envenenador = resistente("Envenenador");
        envenenador.agregarTecnica(new GolpeVenenoso());
        Pareja primera = new Pareja(envenenador, resistente("Apoyo"));
        Combatiente afectado = new CombatienteControlado("Afectado", 5, 0);
        CronistaDeLaSecta cronista = combatir(primera, new Pareja(afectado, resistente("Rival")));
        boolean registróCaída = false;
        for (Evento evento : cronista.getHistorial()) {
            if (evento instanceof EventoEliminacion) {
                EventoEliminacion eliminacion = (EventoEliminacion) evento;
                if (eliminacion.getEliminado().getId() == afectado.getId()) {
                    assertEquals(1, eliminacion.getAsalto());
                    assertEquals(0, eliminacion.getEliminado().getVida());
                    registróCaída = true;
                }
            } else if (evento instanceof EventoAccionRealizada) {
                assertNotEquals(afectado.getId(), ((EventoAccionRealizada) evento).getEjecutor().getId());
            }
        }
        assertTrue(registróCaída);
    }

    private static CronistaDeLaSecta combatir(Pareja primera, Pareja segunda) {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        new Combate(1, primera, segunda, List.of(cronista)).desarrollarCombate();
        return cronista;
    }

    private static EventoResultadoCombate resultado(CronistaDeLaSecta cronista) {
        List<Evento> historial = cronista.getHistorial();
        return (EventoResultadoCombate) historial.get(historial.size() - 1);
    }

    private static EventoAccionRealizada accionDe(CronistaDeLaSecta cronista, Combatiente actor, int asalto) {
        for (EventoAccionRealizada accion : acciones(cronista)) {
            if (accion.getAsalto() == asalto && accion.getEjecutor().getId() == actor.getId()) {
                return accion;
            }
        }
        throw new AssertionError("Falta acción de " + actor + " en el asalto " + asalto);
    }

    private static List<EventoAccionRealizada> acciones(CronistaDeLaSecta cronista) {
        List<EventoAccionRealizada> acciones = new ArrayList<>();
        for (Evento evento : cronista.getHistorial()) {
            if (evento instanceof EventoAccionRealizada) {
                acciones.add((EventoAccionRealizada) evento);
            }
        }
        return acciones;
    }

    private static Combatiente resistente(String nombre) {
        return new CombatienteControlado(nombre, 500, 0);
    }

    private static Pareja parejaResistente() {
        return new Pareja(resistente("Rival 1"), resistente("Rival 2"));
    }

    private static class CombatienteControlado extends Combatiente {
        private final int dano;

        CombatienteControlado(String nombre, int vida, int dano) {
            super(nombre, ESCUELA_DE_PRUEBA, vida, 40, 1.0, 1.0, 1.0);
            this.dano = dano;
        }

        @Override
        public int calcularDanoRealizado(int base) {
            return dano;
        }

        @Override
        public void reaccionarCompaneroFueraDeCombate() {
        }

        @Override
        public void acomodarFinCompaneroFueraDeCombate() {
        }
    }

    private static class TecnicaDePrueba extends Tecnica {
        private final int dano;

        TecnicaDePrueba(String nombre, int costo, TipoTecnica tipo) {
            this(nombre, costo, tipo, 0);
        }

        TecnicaDePrueba(String nombre, int costo, TipoTecnica tipo, int dano) {
            super(nombre, costo, tipo);
            this.dano = dano;
        }

        @Override
        protected void aplicar(Combatiente ejecutor, Combatiente objetivo) {
            objetivo.recibirDano(dano);
        }
    }
}
