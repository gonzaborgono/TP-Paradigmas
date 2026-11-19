import Torneo.Pareja;
import Torneo.Torneo;
import combatiente.CreadorCombatientes;
import cronista.CronistaDeLaSecta;
import escuela.Escuela;
import heraldoImperial.HeraldoImperial;
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
import implementacionTecnicas.RugidoDeLaBestiaPrimordial;
import java.util.List;

public class TorneoDeLosInmortales {
    public static void main(String[] args) {
        if (args.length > 1 || (args.length == 1 && !args[0].equals("4") && !args[0].equals("8"))) {
            throw new IllegalArgumentException("Indicar 4 u 8 parejas.");
        }
        boolean torneoDeOcho = args.length == 1 && args[0].equals("8");

        Escuela espada = new Escuela("Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres(), new RugidoDeLaBestiaPrimordial()),
                List.of(new EspadaCelestial()));
        Escuela vacio = new Escuela("Palacio del Vacío",
                List.of(new DominioDeLaNada(), new GolpeVenenoso(), new MeditacionDelInmortal()),
                List.of(new AnilloEspiritual()));
        Escuela monasterio = new Escuela("Monasterio del Cuerpo Dorado",
                List.of(new MuroDelCuerpoDorado(), new CirculacionDelQi(), new MeditacionDelInmortal()),
                List.of(new ArmaduraDorada(), new SelloDelFenix()));

        HeraldoImperial heraldo = new HeraldoImperial();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Torneo torneo = new Torneo();
        torneo.registrarObservador(heraldo);
        torneo.registrarObservador(cronista);

        CreadorCombatientes creador = new CreadorCombatientes();
        String cultivador = "Cultivador de la Espada";
        String hechicero = "Hechicero del Vacío";
        String monje = "Monje del Cuerpo Celestial";

        torneo.inscribir(new Pareja(creador.crear(cultivador, "Aurelio", espada),
                creador.crear(hechicero, "Nébula", vacio)));
        torneo.inscribir(new Pareja(creador.crear(monje, "Kael", vacio),
                creador.crear(cultivador, "Darian", espada)));
        torneo.inscribir(new Pareja(creador.crear(hechicero, "Selene", espada),
                creador.crear(monje, "Loto", monasterio)));
        torneo.inscribir(new Pareja(creador.crear(cultivador, "Jade", monasterio),
                creador.crear(hechicero, "Orión", vacio)));

        if (torneoDeOcho) {
            torneo.inscribir(new Pareja(creador.crear(monje, "Ámbar", espada),
                    creador.crear(hechicero, "Vesper", vacio)));
            torneo.inscribir(new Pareja(creador.crear(cultivador, "Arista", espada),
                    creador.crear(monje, "Chispa", monasterio)));
            torneo.inscribir(new Pareja(creador.crear(hechicero, "Sombra", monasterio),
                    creador.crear(cultivador, "Ciro", vacio)));
            torneo.inscribir(new Pareja(creador.crear(monje, "Sol", monasterio),
                    creador.crear(hechicero, "Alba", espada)));
        }

        torneo.iniciar();
        heraldo.mostrarTablaPosiciones(cronista.obtenerTablaPosiciones());
    }
}
