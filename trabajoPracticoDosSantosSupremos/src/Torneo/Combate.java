package Torneo;

import combatiente.Combatiente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tecnica.Accion;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class Combate {
    public static final int LIMITE_ASALTOS_DEFAULT = 20;
    public static final int ENERGIA_RECUPERADA_POR_ASALTO = 10;

    private final Pareja pareja1;
    private final Pareja pareja2;
    private final List<ObservadorTorneo> observadores;
    private final Map<Combatiente, Set<Tecnica>> registroUsosCompa = new HashMap<>();
    private final int ronda;
    private int asaltoActual;
    private boolean iniciado;

    public Combate(int ronda, Pareja pareja1, Pareja pareja2, List<ObservadorTorneo> observadores) {
        if (ronda <= 0 || pareja1 == null || pareja2 == null || observadores == null) {
            throw new IllegalArgumentException("El combate necesita una ronda, dos parejas y una lista de observadores.");
        }
        this.ronda = ronda;
        this.pareja1 = pareja1;
        this.pareja2 = pareja2;
        this.observadores = List.copyOf(observadores);
        if (new HashSet<>(obtenerTodosLosCombatientes()).size() != 4) {
            throw new IllegalArgumentException("El combate necesita cuatro combatientes distintos.");
        }
    }

    public Pareja desarrollarCombate() {
        if (iniciado)
            throw new IllegalStateException("Este combate ya se inició.");
        iniciado = true;

        EventoResultadoCombate resultado;
        List<Combatiente> preparados = new ArrayList<>();
        try {
            for (Combatiente combatiente : obtenerTodosLosCombatientes()) {
                combatiente.iniciarCombate();
                preparados.add(combatiente);
                registroUsosCompa.put(combatiente, new HashSet<>());
            }
            notificarEvento(new EventoInicioCombate(ronda, pareja1, pareja2));

            while (!pareja1.estaDerrotada() && !pareja2.estaDerrotada() && asaltoActual < LIMITE_ASALTOS_DEFAULT) {
                asaltoActual++;
                recuperarEnergiaInicioAsalto(preparados);
                ejecutarAsalto();
            }

            resultado = obtenerResultado();
        } finally {
            for (Combatiente combatiente : preparados) {
                combatiente.finalizarCombate();
            }
        }

        // El resultado se publica con el combate cerrado, cuando ya se puede cambiar equipo.
        notificarEvento(resultado);
        return resultado.getGanadora();
    }

    private EventoResultadoCombate obtenerResultado() {
        Pareja ganadora = determinarGanadora();
        Pareja perdedora = pareja1;
        if (ganadora == pareja1)
            perdedora = pareja2;
        return new EventoResultadoCombate(ronda, ganadora, perdedora,
                ganadora.getVidaTotal(), perdedora.getVidaTotal());
    }

    private void notificarEvento(Evento evento) {
        for (ObservadorTorneo observador : observadores) {
            observador.recibirEvento(evento);
        }
    }

    private void ejecutarAsalto() {
        Pareja primera = pareja1;
        Pareja segunda = pareja2;
        if (pareja2.getID() < pareja1.getID()) {
            primera = pareja2;
            segunda = pareja1;
        }
        ejecutarTurnoPareja(primera, segunda);
        if (!primera.estaDerrotada() && !segunda.estaDerrotada()) {
            ejecutarTurnoPareja(segunda, primera);
        }
    }

    private void recuperarEnergiaInicioAsalto(List<Combatiente> combatientes) {
        for (Combatiente combatiente : combatientes) {
            if (!combatiente.estaFueraDeCombate())
                combatiente.aumentarEnergiaAct(ENERGIA_RECUPERADA_POR_ASALTO);
        }
    }

    private void ejecutarTurnoPareja(Pareja atacante, Pareja defensora) {
        for (Combatiente ejecutor : obtenerIntegrantesOrdenadosPorId(atacante)) {
            if (defensora.estaDerrotada())
                return;
            if (ejecutor.estaFueraDeCombate())
                continue;

            ejecutor.procesarEfectosTemporales();
            if (ejecutor.estaFueraDeCombate()) {
                registrarEliminacion(ejecutor, atacante);
            } else {
                realizarAccionIndividual(ejecutor, atacante, defensora);
            }
        }
    }

    private void realizarAccionIndividual(Combatiente ejecutor, Pareja propia, Pareja rival) {
        Tecnica tecnica = seleccionarTecnica(ejecutor, obtenerCompanero(ejecutor, propia));
        Accion accion = tecnica;
        if (accion == null)
            accion = ejecutor.getBasico();
        Combatiente objetivo = ejecutor;
        if (tecnica == null || tecnica.getTipo() == TipoTecnica.OFENSIVA)
            objetivo = elegirObjetivoRival(rival);

        accion.ejecutar(ejecutor, objetivo);
        if (tecnica != null) {
            ejecutor.usarTecnica(tecnica);
            registroUsosCompa.get(ejecutor).add(tecnica);
        }
        notificarEvento(new EventoAccionRealizada(ronda, asaltoActual, ejecutor, objetivo, accion));

        if (objetivo.estaFueraDeCombate()) {
            registrarEliminacion(objetivo, rival);
        }
        if (ejecutor.tieneReaccionActiva()) {
            ejecutor.acomodarFinCompaneroFueraDeCombate();
        }
    }

    private void registrarEliminacion(Combatiente caido, Pareja pareja) {
        notificarEvento(new EventoEliminacion(ronda, asaltoActual, caido));
        Combatiente companero = obtenerCompanero(caido, pareja);
        if (!companero.estaFueraDeCombate()) {
            companero.reaccionarCompaneroFueraDeCombate();
        }
    }

    private Tecnica seleccionarTecnica(Combatiente ejecutor, Combatiente companero) {
        Tecnica curacion = null;
        Tecnica defensiva = null;
        Tecnica ofensiva = null;
        int maxUsos = 0;
        for (Tecnica tecnica : ejecutor.getOrdenAprendidas()) {
            int usos = ejecutor.getUsosRestantes(tecnica);
            if (!puedeUsarTecnica(ejecutor, companero, tecnica, usos))
                continue;
            switch (tecnica.getTipo()) {
                case CURACION:
                    if (curacion == null && ejecutor.getVidaAct() < ejecutor.getVidaMax())
                        curacion = tecnica;
                    break;
                case DEFENSIVA:
                    if (defensiva == null && !ejecutor.tieneDefensivaActiva())
                        defensiva = tecnica;
                    break;
                case OFENSIVA:
                    if (usos > maxUsos) {
                        maxUsos = usos;
                        ofensiva = tecnica;
                    }
                    break;
            }
        }
        if (curacion != null)
            return curacion;
        if (defensiva != null)
            return defensiva;
        return ofensiva;
    }

    private boolean puedeUsarTecnica(Combatiente ejecutor, Combatiente companero, Tecnica tecnica, int usos) {
        if (usos <= 0 || ejecutor.getEnergiaAct() < ejecutor.calcularCostoEnergia(tecnica.getCostoEnergia()))
            return false;
        return companero.estaFueraDeCombate()
                || !registroUsosCompa.get(companero).contains(tecnica)
                || companero.getUsosRestantes(tecnica) == 0;
    }

    private Combatiente elegirObjetivoRival(Pareja rival) {
        Combatiente elegido = null;
        for (Combatiente candidato : obtenerIntegrantesOrdenadosPorId(rival)) {
            if (!candidato.estaFueraDeCombate()
                    && (elegido == null || candidato.getVidaAct() < elegido.getVidaAct())) {
                elegido = candidato;
            }
        }
        return elegido;
    }

    private Pareja determinarGanadora() {
        if (pareja1.estaDerrotada())
            return pareja2;
        if (pareja2.estaDerrotada())
            return pareja1;
        if (pareja1.getVidaTotal() > pareja2.getVidaTotal())
            return pareja1;
        if (pareja1.getVidaTotal() < pareja2.getVidaTotal())
            return pareja2;
        if (pareja1.getID() < pareja2.getID())
            return pareja1;
        return pareja2;
    }

    private List<Combatiente> obtenerTodosLosCombatientes() {
        return List.of(pareja1.getCombatiente1(), pareja1.getCombatiente2(),
                pareja2.getCombatiente1(), pareja2.getCombatiente2());
    }

    private List<Combatiente> obtenerIntegrantesOrdenadosPorId(Pareja pareja) {
        Combatiente primero = pareja.getCombatiente1();
        Combatiente segundo = pareja.getCombatiente2();
        if (primero.compareTo(segundo) < 0)
            return List.of(primero, segundo);
        return List.of(segundo, primero);
    }

    private Combatiente obtenerCompanero(Combatiente ejecutor, Pareja pareja) {
        if (ejecutor.equals(pareja.getCombatiente1()))
            return pareja.getCombatiente2();
        return pareja.getCombatiente1();
    }
}
