package Torneo;

import Pareja.Pareja;
import combatiente.Combatiente;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Torneo {
    private LinkedHashSet<Pareja> parejas;
    private Set<Combatiente> combatientes;


    public void inscribir(Pareja pareja)
    {
        if(this.parejas.size() > 8)
            throw new IllegalArgumentException("No se pueden anotar mas parejas");

        try
        {
            this.parejas.add(pareja);
            this.combatientes.add(pareja.getCombatiente1());
            this.combatientes.add(pareja.getCombatiente2());
        } catch(IllegalArgumentException i)
        {
            throw new IllegalArgumentException("Pareja ya registrada");

        }


    }

    public void iniciar() {
        int cantParejas = this.parejas.size();
        int cantCombates;
        if (cantParejas != 4 && cantParejas != 8)
            throw new IllegalArgumentException("Cantidad de parejas incorrectas (solo con 4 u 8 se inicia)");

        cantCombates = cantParejas/2;
        //se producen cantCombates y se eliminan las parejas que perdieron
        while(cantParejas>1)
        {
            cantParejas--;
            cantCombates = cantParejas/2;
        }

        // cuando combate es 1 es la final
        //combate final

    }
}
