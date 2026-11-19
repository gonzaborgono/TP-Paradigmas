package Torneo;

import combatiente.Combatiente;
import combatiente.CreadorCombatientes;
import escuela.Escuela;
import equipamiento.Equipamiento;
import implementacionCombatientes.CultivadorDeLaEspada;
import implementacionCombatientes.HechiceroDelVacio;
import implementacionCombatientes.MonjeDelCuerpoCelestial;
import implementacionEquipamiento.AnilloEspiritual;
import implementacionEquipamiento.ArmaduraDorada;
import implementacionEquipamiento.EspadaCelestial;
import implementacionEquipamiento.SelloDelFenix;
import implementacionTecnicas.CirculacionDelQi;
import implementacionTecnicas.CorteDeLosMilAmaneceres;
import implementacionTecnicas.DominioDeLaNada;
import implementacionTecnicas.GolpeVenenoso;
import implementacionTecnicas.MeditacionDelInmortal;
import implementacionTecnicas.MuroDelCuerpoDorado;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import tecnica.Tecnica;

import static org.junit.jupiter.api.Assertions.*;

class PreparacionTest {
    private static final Escuela ESCUELA_DE_PRUEBA = new Escuela("Prueba", List.of(), List.of());

    @Test
    void creadorAsignaIdsEnOrdenYPuedePrepararCualquierTipoEnUnaEscuela() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Tecnica dominio = new DominioDeLaNada();
        Equipamiento espada = new EspadaCelestial();
        Escuela escuela = new Escuela("Espada", List.of(corte, dominio), List.of(espada));
        CreadorCombatientes creador = new CreadorCombatientes();
        Combatiente a = creador.crear("Hechicero del Vacío", "Alex", escuela);
        Combatiente b = creador.crear("Monje del Cuerpo Celestial", "Alex", escuela);

        assertInstanceOf(HechiceroDelVacio.class, a);
        assertInstanceOf(MonjeDelCuerpoCelestial.class, b);
        assertTrue(a.getId() < b.getId());
        assertNotEquals(a, b);
        assertSame(escuela, a.getEscuela());
        assertSame(escuela, b.getEscuela());
        assertEquals(List.of(corte, dominio), a.getOrdenAprendidas());
        assertEquals(List.of(espada), a.getEquipamientos());
        a.usarTecnica(new CorteDeLosMilAmaneceres());
        assertEquals(4, a.getUsosRestantes(corte));
        assertEquals(5, b.getUsosRestantes(corte));
        assertThrows(IllegalStateException.class, () -> a.agregarTecnica(new CorteDeLosMilAmaneceres()));
        assertThrows(IllegalArgumentException.class, () -> creador.crear("Otro tipo", "Alex", escuela));
    }

    @Test
    void escuelaCopiaLasListasYValidaElementosNulos() {
        List<Tecnica> tecnicas = new ArrayList<>(List.of(new CorteDeLosMilAmaneceres()));
        List<Equipamiento> equipo = new ArrayList<>(List.of(new EspadaCelestial()));
        Escuela escuela = new Escuela("Espada", tecnicas, equipo);
        tecnicas.clear();
        equipo.clear();
        assertEquals(1, escuela.getTecnicas().size());
        assertEquals(1, escuela.getEquipamientos().size());
        assertThrows(UnsupportedOperationException.class, () -> escuela.getTecnicas().clear());
        assertThrows(IllegalArgumentException.class, () -> new Escuela("Espada",
                Arrays.asList((Tecnica) null), List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Escuela("Espada",
                List.of(), Arrays.asList((Equipamiento) null)));
        assertThrows(IllegalArgumentException.class, () -> new Escuela("Espada",
                List.of(new CorteDeLosMilAmaneceres(), new CorteDeLosMilAmaneceres()), List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Escuela("Espada", List.of(),
                List.of(new EspadaCelestial(), new EspadaCelestial())));
    }

    @Test
    void tecnicaUsaMultiplicadoresDelEjecutorYDelObjetivo() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Combatiente cultivador = new CultivadorDeLaEspada("Espada", ESCUELA_DE_PRUEBA);
        Combatiente hechicero = new HechiceroDelVacio("Vacío", ESCUELA_DE_PRUEBA);
        Combatiente monje = new MonjeDelCuerpoCelestial("Monje", ESCUELA_DE_PRUEBA);
        Combatiente objetivo = new CultivadorDeLaEspada("Objetivo", ESCUELA_DE_PRUEBA);

        corte.ejecutar(cultivador, objetivo);
        assertEquals(59, objetivo.getVidaMax() - objetivo.getVidaAct());
        assertEquals(20, cultivador.getEnergiaAct());
        objetivo.resetearEstado();
        corte.ejecutar(hechicero, objetivo);
        assertEquals(45, objetivo.getVidaMax() - objetivo.getVidaAct());
        assertEquals(50, hechicero.getEnergiaAct());
        objetivo.resetearEstado();
        monje.recibirDano(40);
        assertEquals(28, monje.getVidaMax() - monje.getVidaAct());
    }

    @Test
    void tecnicasAplicanDrenajeCuracionYDefensaHastaElFinDelCombate() {
        Combatiente ejecutor = new HechiceroDelVacio("Vacío", ESCUELA_DE_PRUEBA);
        Combatiente receptor = new CultivadorDeLaEspada("Espada", ESCUELA_DE_PRUEBA);
        new DominioDeLaNada().ejecutar(ejecutor, receptor);
        assertEquals(70, receptor.getVidaAct());
        assertEquals(20, receptor.getEnergiaAct());
        ejecutor.recibirDano(40);
        new MeditacionDelInmortal().ejecutar(ejecutor, ejecutor);
        assertEquals(65, ejecutor.getVidaAct());
        receptor.aumentarEnergiaAct(20);
        new MuroDelCuerpoDorado().ejecutar(receptor, receptor);
        receptor.recibirDano(20);
        receptor.recibirDano(20);
        assertEquals(40, receptor.getVidaAct());
        receptor.resetearEstado();
        receptor.recibirDano(20);
        assertEquals(80, receptor.getVidaAct());
    }

    @Test
    void tecnicaSinEnergiaNoModificaAlEjecutorNiAlObjetivo() {
        Combatiente a = new CultivadorDeLaEspada("Espada", ESCUELA_DE_PRUEBA);
        Combatiente b = new CultivadorDeLaEspada("Rival", ESCUELA_DE_PRUEBA);
        a.disminuirEnergiaAct(21);
        assertThrows(IllegalStateException.class, () -> new CorteDeLosMilAmaneceres().ejecutar(a, b));
        assertEquals(19, a.getEnergiaAct());
        assertEquals(b.getVidaMax(), b.getVidaAct());
    }

    @Test
    void equipoSeAcumulaYEsConsultableSinPermitirMutarLaLista() {
        Combatiente combatiente = new MonjeDelCuerpoCelestial("Monje", ESCUELA_DE_PRUEBA);
        Equipamiento espada = new EspadaCelestial();
        Equipamiento armadura = new ArmaduraDorada();
        Equipamiento anillo = new AnilloEspiritual();
        combatiente.equipar(espada);
        combatiente.equipar(armadura);
        combatiente.equipar(anillo);

        assertEquals(List.of(espada, armadura, anillo), combatiente.getEquipamientos());
        assertEquals(115, combatiente.calcularDanoRealizado(100));
        combatiente.recibirDano(100);
        assertEquals(75, combatiente.getVidaAct());
        assertEquals(60, combatiente.getEnergiaMax());
        assertThrows(UnsupportedOperationException.class, () -> combatiente.getEquipamientos().clear());
        combatiente.desequipar(armadura);
        assertEquals(0.70, combatiente.getMultDanoRecibido(), 0.0001);
        assertFalse(combatiente.getEquipamientos().contains(armadura));
    }

    @Test
    void rechazaCuartoEquipoCategoriaRepetidaYRetiroDeObjetoAjenoSinCambios() {
        Combatiente combatiente = new CultivadorDeLaEspada("Espada", ESCUELA_DE_PRUEBA);
        Equipamiento espada = new EspadaCelestial();
        Equipamiento armadura = new ArmaduraDorada();
        Equipamiento anillo = new AnilloEspiritual();
        combatiente.equipar(espada);
        double dano = combatiente.getMultDanoRealizado();
        assertThrows(IllegalStateException.class, () -> combatiente.equipar(new EspadaCelestial()));
        assertEquals(List.of(espada), combatiente.getEquipamientos());
        assertEquals(dano, combatiente.getMultDanoRealizado());

        combatiente.equipar(armadura);
        combatiente.equipar(anillo);
        assertThrows(IllegalStateException.class, () -> combatiente.equipar(new SelloDelFenix()));
        assertThrows(IllegalArgumentException.class, () -> combatiente.desequipar(new EspadaCelestial()));
        assertEquals(List.of(espada, armadura, anillo), combatiente.getEquipamientos());
        assertEquals(100, combatiente.getVidaMax());
        assertEquals(dano, combatiente.getMultDanoRealizado());
    }

    @Test
    void losMaximosSeActualizanSinRellenarYAlRetirarSeRecortan() {
        Combatiente combatiente = new CultivadorDeLaEspada("Espada", ESCUELA_DE_PRUEBA);
        Equipamiento sello = new SelloDelFenix();
        Equipamiento anillo = new AnilloEspiritual();
        combatiente.equipar(sello);
        combatiente.equipar(anillo);

        assertEquals(130, combatiente.getVidaMax());
        assertEquals(100, combatiente.getVidaAct());
        assertEquals(70, combatiente.getEnergiaMax());
        assertEquals(40, combatiente.getEnergiaAct());
        combatiente.resetearEstado();
        combatiente.desequipar(sello);
        combatiente.desequipar(anillo);
        assertEquals(100, combatiente.getVidaMax());
        assertEquals(100, combatiente.getVidaAct());
        assertEquals(40, combatiente.getEnergiaMax());
        assertEquals(40, combatiente.getEnergiaAct());
    }

    @Test
    void bonusVenenoActuaSeisTurnosYRegeneracionCinco() {
        Combatiente a = new HechiceroDelVacio("Vacío", ESCUELA_DE_PRUEBA);
        Combatiente b = new CultivadorDeLaEspada("Rival", ESCUELA_DE_PRUEBA);
        new GolpeVenenoso().ejecutar(a, b);
        assertEquals(90, b.getVidaAct());
        for (int turno = 1; turno <= 6; turno++) {
            b.procesarEfectosTemporales();
            assertEquals(90 - 5 * turno, b.getVidaAct());
        }
        b.procesarEfectosTemporales();
        assertEquals(60, b.getVidaAct());

        b.disminuirVidaAct(30);
        new CirculacionDelQi().ejecutar(b, b);
        for (int turno = 1; turno <= 5; turno++) {
            b.procesarEfectosTemporales();
            assertEquals(30 + 8 * turno, b.getVidaAct());
        }
        b.procesarEfectosTemporales();
        assertEquals(70, b.getVidaAct());
    }
}
