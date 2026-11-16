package Torneo;

import Pareja.Pareja;
import combatiente.Combatiente;
import java.util.ArrayList;
import java.util.List;

public class Torneo 
{
    public static final int MIN_PAREJAS = 4;
    public static final int MAX_PAREJAS = 8;

    private final List<Pareja> parejas;
    private final List<ObservadorTorneo> observadores;
    private boolean iniciado;

    public Torneo() 
    {
        this.parejas = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.iniciado = false;
    }

    public void registrarObservador(ObservadorTorneo observador) 
    {
        if (observador == null) 
        {
            throw new IllegalArgumentException("El observador no puede ser nulo.");
        }
        if (!this.observadores.contains(observador)) 
        {
            this.observadores.add(observador);
        }
    }

    public void inscribir(Pareja pareja) 
    {
        if (this.iniciado) 
        {
            throw new IllegalStateException("No se pueden inscribir parejas una vez iniciado el torneo.");
        }
        if (pareja == null) 
        {
            throw new IllegalArgumentException("La pareja no puede ser nula.");
        }
        if (this.parejas.size() >= MAX_PAREJAS) 
        {
            throw new IllegalStateException("El torneo admite como máximo " + MAX_PAREJAS + " parejas.");
        }
        if (this.parejas.contains(pareja)) 
        {
            throw new IllegalArgumentException("La pareja ya se encuentra registrada en el torneo.");
        }

        Combatiente nuevo1 = pareja.getCombatiente1();
        Combatiente nuevo2 = pareja.getCombatiente2();

        for (Pareja p : this.parejas) 
        {
            if (p.getCombatiente1().equals(nuevo1) || p.getCombatiente2().equals(nuevo1) ||
                p.getCombatiente1().equals(nuevo2) || p.getCombatiente2().equals(nuevo2) ) 
            {
                throw new IllegalArgumentException("Uno o ambos combatientes ya están inscriptos en otra pareja.");
            }
        }

        this.parejas.add(pareja);
    }

    public Pareja iniciar() 
    {
        int cantParejas = this.parejas.size();
        if (cantParejas != MIN_PAREJAS && cantParejas != MAX_PAREJAS) 
        {
            throw new IllegalStateException("El torneo solo puede iniciar con 4 u 8 parejas inscriptas.");
        }
        if (this.iniciado) 
        {
            throw new IllegalStateException("El torneo ya fue iniciado.");
        }

        this.iniciado = true;

        //copiar lista para no borrar original
        List<Pareja> rondaActual = new ArrayList<>(this.parejas);

        //mete en siguienteRonda la pareja que gana el combate, dps toma de este array para hacer el combate
        while (rondaActual.size() > 1) 
        {
            List<Pareja> siguienteRonda = new ArrayList<>();

            //avanzamos de a 2 para hacer cruces de a 2, tipo 0 y 1, dps 2 y 3
            for (int i = 0; i < rondaActual.size(); i += 2) 
            {
                Pareja pareja1 = rondaActual.get(i);
                Pareja pareja2 = rondaActual.get(i + 1);

                Combate combate = new Combate(pareja1, pareja2, this.observadores);
                Pareja ganadora = combate.desarrollarCombate();

                siguienteRonda.add(ganadora);
            }

            rondaActual = siguienteRonda;
        }

        Pareja campeona = rondaActual.get(0);
        notificarProclamacion(campeona);

        return campeona;
    }


    private void notificarProclamacion(Pareja campeona) 
    {
        //a desarrollar sistema para avisar eventos
    }
}
