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
        ID = proxID;
        proxID++;
        if(!combatiente1.equals(combatiente2))
        {
            combatientes = new ArrayList<>();
            combatientes.add(combatiente1);
            combatientes.add(combatiente2);
        }
    }
}
