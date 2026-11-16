package Torneo;

import combatiente.Combatiente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tecnica.Tecnica;
import tecnica.TipoTecnica;

public class Combate 
{
    public static final int LIMITE_ASALTOS_DEFAULT = 20;
    public static final int ENERGIA_RECUPERADA_POR_ASALTO = 10;

    private Pareja pareja1;
    private Pareja pareja2;
    private int asaltoActual;
    private List<ObservadorTorneo> observadores;
    private Map<Combatiente, Set<Tecnica>> registroUsosCompa; //tiene cada combatiente y el set de tecnicas que uso en este combate, ahi busca el compañero para no repetir

    public Combate(Pareja pareja1, Pareja pareja2, List<ObservadorTorneo> observadores) 
    {
        if (pareja1 == null || pareja2 == null) 
        {
            throw new IllegalArgumentException("Las parejas del combate no pueden ser nulas.");
        }
        if (pareja1.equals(pareja2)) 
        {
            throw new IllegalArgumentException("Una pareja no puede combatir contra sí misma.");
        }

        this.pareja1 = pareja1;
        this.pareja2 = pareja2;
        this.asaltoActual = 0;
        this.observadores = observadores;
        this.registroUsosCompa = new HashMap<>();
    }

    public Pareja desarrollarCombate() 
    {
        resetearEstadoCombatientes();
        //hacer notificacion de inicio de pelea y avisar evento

        //hacer que peleen hasta que una pareja cae o se llegue al limite de asaltos
        while (!pareja1.estaDerrotada() && !pareja2.estaDerrotada() && asaltoActual < LIMITE_ASALTOS_DEFAULT) 
        {
            asaltoActual++;
            recuperarEnergiaInicioAsalto();
            ejecutarAsalto();
        }

        Pareja ganadora = determinarGanadora();
        //avisar ganador como evento

        return ganadora;
    }

    public void notificarEvento(Evento e) 
    {
        for (ObservadorTorneo obs : observadores) 
        {
            obs.recibirEvento(e);
        }
    }

    private void ejecutarAsalto() 
    {
    	Pareja primera;
    	Pareja segunda;
        //empieza pareja de menor id
        if(pareja1.getID() < pareja2.getID())
        {
        	primera = pareja1;
        	segunda = pareja2;
        }
        else
        {
        	primera = pareja1;
        	segunda = pareja2;
        }
        	
        ejecutarTurnoPareja(primera, segunda);

        //si alguna pareja cae no se hace el turno
        if (!segunda.estaDerrotada() && !primera.estaDerrotada()) 
        {
            ejecutarTurnoPareja(segunda, primera);
        }
    }

    private void recuperarEnergiaInicioAsalto() 
    {
        //todos los que no esten fuera de combate reciben energia
        List<Combatiente> todos = obtenerTodosLosCombatientes();
        for (Combatiente c : todos) 
        {
            if (!c.estaFueraDeCombate()) 
            {
                c.aumentarEnergiaAct(ENERGIA_RECUPERADA_POR_ASALTO);
            }
        }
    }

    private void resetearEstadoCombatientes() 
    {
        for (Combatiente c : obtenerTodosLosCombatientes()) 
        {
            c.resetearEstado();
            this.registroUsosCompa.put(c, new HashSet<>());
        }
    }

    

    private void ejecutarTurnoPareja(Pareja atacante, Pareja defensora) 
    {
        //dentro de la pareja va primero el de menor id
        List<Combatiente> integrantesOrdenados = obtenerIntegrantesOrdenadosPorId(atacante);

        for (Combatiente ejecutor : integrantesOrdenados) 
        {
            if (defensora.estaDerrotada()) //si la defensora cayo con el primer combatiente ya se acaba el combate, no ataca el segundo
            {
                break;
            }
            if (!ejecutor.estaFueraDeCombate()) //solo atacar si el combatiente sigue vivo
            {
                realizarAccionIndividual(ejecutor, atacante, defensora);
            }
        }
    }

    private void realizarAccionIndividual(Combatiente ejecutor, Pareja parejaPropia, Pareja parejaRival) 
    {
    	//para ya tener compañero y que tecnica va a usar
        Combatiente companero = obtenerCompanero(ejecutor, parejaPropia);
        Tecnica tecnicaElegida = seleccionarTecnicaSegunCriterio(ejecutor, companero);
        
        if (tecnicaElegida != null) 
        {
        	//elegir objetivo
            Combatiente objetivo = determinarObjetivoTecnica(tecnicaElegida, ejecutor, parejaRival);
            int vidaAntes = objetivo.getVidaAct();
            
            tecnicaElegida.ejecutar(ejecutor, objetivo);
            ejecutor.usarTecnica(tecnicaElegida);
            this.registroUsosCompa.get(ejecutor).add(tecnicaElegida);

            //notificar evento de accion realizada

            if (vidaAntes > 0 && objetivo.estaFueraDeCombate()) //solo avisar eliminacion si antes de atacar estaba vivo y ahora esta muerto
            {
                //notificar evento de eliminacion
            }
        } 
        else 
        {
        	//mismo que arriba, elegir objetivo
            Combatiente objetivo = elegirObjetivoRival(parejaRival);
            int vidaAntes = objetivo.getVidaAct();

            //pedir el basico del ejecutor y hacerlo
            ejecutor.getBasico().ejecutar(ejecutor, objetivo);


            //notificar accion realizada

            if (vidaAntes > 0 && objetivo.estaFueraDeCombate()) 
            {
                //notificar eliminacion
            }
        }
    }

    private Tecnica seleccionarTecnicaSegunCriterio(Combatiente ejecutor, Combatiente companero) 
    {
        /* texto de consigna:
         una técnica de recuperación si le falta vida; 
         una defensiva si no tiene una activa; 
         la técnica ofensiva con más usos restantes. 
         Si ninguna es posible (por energía, usos o regla del compañero), se realiza un ataque básico. 
         En empate, la primera en el orden de la escuela
         */
        //recuperacion si le falta vida
        if (ejecutor.getVidaAct() < ejecutor.getVidaMax()) 
        {
        	//buscar la primer tecnica de recuperacion que tenga y que no haya usado el compañero
            Tecnica recup = buscarPrimeraDisponible(ejecutor, companero, TipoTecnica.CURACION);
            if (recup != null) 
            	return recup;
        }

        //defensiva
        if (!ejecutor.tieneDefensivaActiva()) //si no tiene una activa
        {
            Tecnica def = buscarPrimeraDisponible(ejecutor, companero, TipoTecnica.DEFENSIVA);
            if (def != null) 
            	return def;
        }

        //ofensiva con mas usos, si empatan elige por orden de insersion (revisar esto, creo que esta mal)
        Tecnica mejorOfensiva = null;
        int maxUsos = 0;
        
        //recorre por orden de aprendidas
        for (Tecnica t : ejecutor.getOrdenAprendidas()) 
        {
        	//busca los usos que le quedan a esa tecnica en el torneo
            int usosRestantes = ejecutor.getTecnicas().get(t);

            //si es ofensiva y la puede usar (toma en cuenta restriccion de compañero, energia, etc)
            if (t.getTipo() == TipoTecnica.OFENSIVA && puedeUsarTecnica(ejecutor, companero, t, usosRestantes)) 
            {
            	//busca la maxima de las que puede usar y son ofensivas
                if (usosRestantes > maxUsos) 
                {
                    maxUsos = usosRestantes;
                    mejorOfensiva = t;
                }
            }
        }

        return mejorOfensiva;
    }

    private Tecnica buscarPrimeraDisponible(Combatiente ejecutor, Combatiente companero, TipoTecnica tipo) 
    {
    	//pide todas las tecnicas del ejecutor e itera buscando la que pueda usar
        for (Map.Entry<Tecnica, Integer> entry : ejecutor.getTecnicas().entrySet()) 
        {
            Tecnica t = entry.getKey();
            int usos = entry.getValue();
            //si es del tipo que busca y la puede usar, usa esa
            if (t.getTipo() == tipo && puedeUsarTecnica(ejecutor, companero, t, usos)) 
            {
                return t;
            }
        }
        return null;
    }

    private boolean puedeUsarTecnica(Combatiente ejecutor, Combatiente companero, Tecnica tecnica, int usosRestantes) 
    {
    	/* consgina sobre usar tecnica
    	 cuando un integrante usa una técnica en un combate, 
    	 su compañero no puede usarla mientras el primero conserve usos de ella y siga en pie;
    	 si los agota o queda fuera de combate, el compañero puede usarla. 
    	 La restricción termina con el combate: en el siguiente, 
    	 cualquiera de los dos puede volver a usarla.
    	 */
    	
        //validar usos restantes > 0 y que energia alcanze
    	//calcular costo real, teniendo en cuenta que no todos los combatientes gastan la misma energia para hacer la misma tecnica
        int costoReal = (int) Math.round(tecnica.getCostoEnergia() * ejecutor.getMultGastoEnergia());
        
        if (usosRestantes <= 0 || ejecutor.getEnergiaAct() < costoReal) 
        {
            return false;
        }

        
        //si el compañero sigue en combate y ya uso la tecnica no la puede usar
        if (!companero.estaFueraDeCombate()) 
        {
            Set<Tecnica> usadasPorCompa = this.registroUsosCompa.get(companero);
            //si el compañero no uso ninguna tecnica o las que uso no tienen la tecnica que se quiere, directamente va a return true
            if (usadasPorCompa != null && usadasPorCompa.contains(tecnica)) 
            {
            	//si la uso pero no le quedan usos del torneo directamente va a return true
                Integer usosCompa = companero.getTecnicas().get(tecnica);
                if (usosCompa != null && usosCompa > 0) 
                { //solo llega a dar falso si el compañero esta vivo, uso la tecnica y le quedan usos en el torneo
                    return false;
                }
            }
        }

        return true;
    }

    private Combatiente determinarObjetivoTecnica(Tecnica tecnica, Combatiente ejecutor, Pareja parejaRival) 
    {
        //si es defensiva o de cura, se devuelve a si mismo
        if (tecnica.getTipo() == TipoTecnica.DEFENSIVA || tecnica.getTipo() == TipoTecnica.CURACION) 
        {
            return ejecutor;
        }
        return elegirObjetivoRival(parejaRival);
    }

    private Combatiente elegirObjetivoRival(Pareja parejaRival) 
    {
        //elige al rival de menos vida, si empatan al de menor id
        Combatiente elegido = null;
        for (Combatiente rival : obtenerIntegrantesOrdenadosPorId(parejaRival)) 
        {
            if (!rival.estaFueraDeCombate()) //si esta fuera de combate no lo puede elegir
            {
            	//si es el primero en revisar lo elige,
            	//si el actual tiene menos vida que el ya elegido elige ese
                if (elegido == null || rival.getVidaAct() < elegido.getVidaAct()) 
                {
                    elegido = rival;
                }
            }
        }
        return elegido;
    }

    private Pareja determinarGanadora() 
    {
        if (pareja1.estaDerrotada()) return pareja2;
        if (pareja2.estaDerrotada()) return pareja1;

        //si se llego al limite de rondas, se elige la de mayor vida total, si empatan, la de menor id
        int vidaTotal1 = pareja1.getVidaTotal();
        int vidaTotal2 = pareja2.getVidaTotal();

        if (vidaTotal1 > vidaTotal2) 
        	return pareja1;
        if (vidaTotal2 > vidaTotal1) 
        	return pareja2;

        if(pareja1.getID() > pareja2.getID())
        	return pareja2;
        return pareja1;
    }

    private List<Combatiente> obtenerTodosLosCombatientes() 
    {
        List<Combatiente> todos = new ArrayList<>();
        todos.add(pareja1.getCombatiente1());
        todos.add(pareja1.getCombatiente2());
        todos.add(pareja2.getCombatiente1());
        todos.add(pareja2.getCombatiente2());
        return todos;
    }

    private List<Combatiente> obtenerIntegrantesOrdenadosPorId(Pareja pareja) 
    {
        List<Combatiente> lista = new ArrayList<>();
        lista.add(pareja.getCombatiente1());
        lista.add(pareja.getCombatiente2());
        lista.sort(null); //combatiente ya compara por id
        return lista;
    }

    private Combatiente obtenerCompanero(Combatiente ejecutor, Pareja pareja) 
    {
    	//si tengo el 1, dame el 2, sino dame el 1
    	if(pareja.getCombatiente1().equals(ejecutor))
    		return pareja.getCombatiente2();
    	
        return pareja.getCombatiente1();
    }
}
