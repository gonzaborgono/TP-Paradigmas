# El Torneo de los Inmortales

TP 2 de Paradigmas de Programación. El programa simula un torneo eliminatorio por parejas. Los cruces y las acciones se resuelven por reglas deterministas. El Heraldo anuncia los acontecimientos y el Cronista reconstruye la tabla a partir de los resultados registrados.

## Cómo ejecutar en Eclipse

1. Con un JDK 17 o superior, importá la carpeta `TP-Paradigmas` desde **File → Import → General → Existing Projects into Workspace**. Si Eclipse detecta también la subcarpeta `trabajoPracticoDosSantosSupremos` como proyecto, seleccioná solo `TP-Paradigmas`.
2. Abrí `TorneoDeLosInmortales.java` y elegí **Run As → Java Application**. Sin argumentos usa cuatro parejas. Para probar ocho, agregá `8` en **Run Configurations → Arguments → Program arguments**.
3. Para ejecutar las 33 pruebas juntas, hacé clic derecho en la carpeta `test` y elegí **Run As → JUnit Test**. También podés ejecutar cada clase de prueba por separado. JUnit 5 está incluido en la configuración del proyecto de Eclipse.

El [diagrama de clases](diagrama-clases.png) está disponible como imagen.

## Decisiones de diseño

- El creador asigna identificadores de combatiente en orden de creación. Cada pareja obtiene su identificador al construirse. Las inscripciones usan una lista para conservar el orden de los cruces y un conjunto para rechazar combatientes repetidos.
- Una escuela es una configuración ordenada de técnicas y equipo inicial. El creador permite combinar cualquier escuela con cualquiera de los tres tipos de combatiente.
- `Accion` es el contrato común de `AtaqueBasico` y `Tecnica`. Una técnica se identifica por su clase concreta y su nombre. Un mapa ordenado por combatiente conserva el orden de escuela y los cinco usos por técnica. Cada combate registra por separado qué técnicas usó cada integrante para aplicar el bloqueo del compañero.
- La elección intenta primero curación si falta vida, luego defensa si no hay una activa y luego la ofensiva con más usos. Los empates se resuelven por orden de escuela. El objetivo ofensivo es el rival en pie con menos vida y menor ID en caso de empate.
- Al comienzo de cada cruce se restauran vida y energía. Los usos se restauran al iniciar un torneo nuevo. Un combate dura hasta 20 asaltos o hasta que cae una pareja completa. Cada asalto devuelve 10 de energía a los combatientes en pie. Al límite se compara la vida total y luego el menor ID.
- El equipamiento se cambia antes del torneo o entre cruces. Un observador que necesite hacer un cambio entre cruces puede hacerlo al recibir `EventoResultadoCombate`, después de que se cerró el combate y se guardaron sus totales.
- Los acontecimientos guardan sus datos al emitirse. El historial usa el orden de emisión, la ronda y el asalto. Dos armados equivalentes se comparan por participantes y datos de juego, ya que los IDs son únicos en la ejecución. El Cronista ordena la última ronda y la vida final de mayor a menor, y el ID de menor a mayor.

## Valores y justificación

| Combatiente | Vida | Energía | Característica |
| --- | ---: | ---: | --- |
| Cultivador de la Espada | 100 | 40 | Inflige un 30 % más de daño. |
| Monje del Cuerpo Celestial | 130 | 30 | Recibe un 30 % menos de daño. |
| Hechicero del Vacío | 70 | 60 | Consume la mitad de energía. |

Los valores hacen visibles las ventajas de cada tipo. El Cultivador toma como referencia los 100 de vida y 40 de energía del enunciado. El Monje cambia energía por resistencia. El Hechicero puede usar técnicas caras con mayor frecuencia a cambio de tener menos vida. Los 10 puntos recuperados por asalto permiten recuperar el costo de las acciones más baratas rápidamente y obligan a espaciar las más caras. El límite de 20 acota los encuentros con defensa y recuperación.

| Acción | Costo base | Efecto y razón del valor |
| --- | ---: | --- |
| Ataque básico | 0 | 15 de daño. Permite avanzar cuando no se puede usar una técnica. |
| Corte de los Mil Amaneceres | 20 | 45 de daño. Triplica el daño base del básico a cambio de energía. |
| Muro del Cuerpo Dorado | 30 | Reduce un 25 % el daño recibido durante el resto del combate. Su costo consume la energía inicial de un Monje sin equipo. |
| Dominio de la Nada | 35 | 30 de daño y drena 20 de energía. Hace menos daño que Corte y retrasa el uso de técnicas rivales. |
| Rugido de la Bestia Primordial | 55 | 65 de daño. Es el mayor golpe directo y exige más energía que la base del Cultivador. |
| Meditación del Inmortal | 30 | Cura 35 de inmediato. A cambio del turno y de energía, permite recuperarse de más de dos ataques básicos sin modificadores. |
| Golpe venenoso | 25 | 10 de daño inmediato y 5 por turno durante 6 turnos propios del afectado. El daño extra depende de cuánto tiempo siga en pie. |
| Circulación del Qi | 10 | Cura 8 por turno durante 5 turnos propios. Puede curar más que Meditación si se completa, pero su ayuda es gradual. |

La Espada Celestial suma 0,15 al multiplicador de daño realizado y la Armadura Dorada resta 0,15 al de daño recibido. Los cambios de equipo son menores que las ventajas innatas del Cultivador y del Monje. El Anillo Espiritual agrega 30 de energía máxima y el Sello del Fénix 30 de vida máxima. Aumentan la reserva respetando el valor actual hasta la próxima recuperación.

## Bonus

- **Efectos temporales.** Veneno y regeneración se aplican antes de cada acción propia del afectado, incluida la primera que ocurra después de recibir el efecto. Cada aplicación consume un turno del efecto. Si se vuelve a aplicar, cada instancia lleva su duración. El veneno ignora la resistencia y puede eliminar al afectado antes de su acción. Los efectos restantes se limpian al terminar el cruce.
- **Reacción a la caída del compañero.** El Cultivador suma 0,20 al multiplicador de daño, el Hechicero reduce en 0,20 su multiplicador de costo y el Monje recupera 25 de vida al activarse y suma 0,15 a su multiplicador de daño. El modificador dura hasta después de la siguiente acción del sobreviviente o hasta el cierre del combate.

## Correspondencia entre reglas y pruebas JUnit 5

| Sección | Reglas verificadas | Clase y métodos |
| --- | --- | --- |
| 4 | Consulta, acumulación y retiro de equipo | `PreparacionTest.equipoSeAcumulaYEsConsultableSinPermitirMutarLaLista` |
| 4 | Máximo de tres, categorías únicas y rechazo sin cambios | `PreparacionTest.rechazaCuartoEquipoCategoriaRepetidaYRetiroDeObjetoAjenoSinCambios` |
| 4 | Aumentos de máximos y recorte al retirar | `PreparacionTest.losMaximosSeActualizanSinRellenarYAlRetirarSeRecortan` |
| 4 | Cambio de equipo entre cruces | `ReglasCombateTest.permiteCambiarEquipoEntreCrucesYLoRechazaDuranteElCombate` |
| 5 | Identidad, dos integrantes distintos y derrota de la pareja | `TorneoTest.parejaTieneDosIntegrantesDistintosYUnIdAsignadoAlCrearse` |
| 5 | Dobles inscripciones y cantidad admisible | `TorneoTest.rechazaInscribirLaMismaParejaORepetirUnCombatiente`, `soloCuatroUOchoParejasPuedenIniciarYLaNovenaSeRechaza` |
| 5 | Cruces por inscripción, avance por ronda y proclamación | `TorneoTest.crucesConCuatroRespetanInscripcionYGanadoresDeLaRondaAnterior`, `crucesConOchoRespetanInscripcionYGanadoresDeLaRondaAnterior` |
| 6 | Orden de turnos, selección de objetivo, exclusión de caídos y fin inmediato | `ReglasCombateTest.ordenaParejasEIntegrantesPorIdInclusoSiSePasanInvertidos`, `enfocaAlRivalConMenosVidaYOmiteACaidos` |
| 6 | Prioridad de curación, defensa, ofensiva, usos y orden de escuela | `ReglasCombateTest.priorizaCuracionLuegoDefensaYAplicaAmbasAlEjecutor`, `ofensivasSeEligenPorUsosYDespuesPorOrdenDeEscuela` |
| 6 | Ataque básico y requisitos de energía y usos | `ReglasCombateTest.ataqueBasicoSiempreDisponibleNoConsumeEnergiaNiUsos`, `usaBasicoSiNoAlcanzaEnergia`, `bloqueaLaMismaTecnicaDeOtraInstanciaYLuegoLaLiberaAlAgotarSusUsos` |
| 6 | Energía por asalto solo para quienes siguen en pie, restauración al comienzo del cruce y conservación de usos | `ReglasCombateTest.recuperaDiezDeEnergiaPorAsaltoSinSuperarElMaximo`, `recuperaVidaYEnergiaParaElSiguienteCruceSinReponerUsosNiMantenerBloqueo` |
| 6 | Liberación de la técnica al agotar usos, caer o cambiar de cruce | `ReglasCombateTest.bloqueaLaMismaTecnicaDeOtraInstanciaYLuegoLaLiberaAlAgotarSusUsos`, `liberaLaTecnicaSiCaeQuienLaUso`, `bloqueoPorUsoTerminaAlCambiarDeCombateAunqueElCompaneroSigaEnPie` |
| 6 | Límite de asaltos, mayor vida total, menor ID y reproducibilidad | `ReglasCombateTest.alLimiteGanaLaMayorVidaTotalYLuegoElIdMenor`, `TorneoTest.dosTorneosEquivalentesProducenElMismoCampeonYLosMismosEventos`, `elNuevoTorneoReponeUsosYReproduceElHistorialConLosMismosIds` |
| 7 | Orden de posiciones y capturas del historial | `TorneoTest.tablaOrdenaPorUltimaRondaVidaDescendenteEIdAscendente`, `lasCapturasDelHistorialNoCambianAlActualizarElCombatiente` |
| 9 | Duración de efectos, eliminación antes de actuar y reacción por la caída del compañero | `PreparacionTest.bonusVenenoActuaSeisTurnosYRegeneracionCinco`, `ReglasCombateTest.venenoPuedeEliminarAlAfectadoAntesDeSuAccion`, `bonusReactivoPotenciaUnaAccionDespuesDeLaCaida` |
