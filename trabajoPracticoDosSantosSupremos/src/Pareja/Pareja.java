package Pareja;

import combatiente.Combatiente;

import java.util.ArrayList;
import java.util.List;

public class Pareja {
    private final int ID;
    private static int proxID = 1;
    private List<Combatiente> combatientes;
    Pareja(Combatiente combatiente1, Combatiente combatiente2)
    {
        if(combatiente1 == null && combatiente2 == null)
            throw new IllegalArgumentException("La pareja necesita dos combatientes");

        if(combatiente1.equals(combatiente2))
            throw new IllegalArgumentException("Los combatientes de una pareja deben ser distintos");

        ID = proxID;
        proxID++;
        combatientes = new ArrayList<>();
        combatientes.add(combatiente1);
        combatientes.add(combatiente2);
    }
}
