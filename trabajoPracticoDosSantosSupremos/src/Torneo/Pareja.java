package Torneo;

import combatiente.Combatiente;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pareja {
    private final int ID;
    private List<Combatiente> combatientes;
    Pareja(int ID, Combatiente combatiente1, Combatiente combatiente2)
    {
        if(combatiente1 == null || combatiente2 == null)
            throw new IllegalArgumentException("La pareja necesita dos combatientes");

        if(combatiente1.equals(combatiente2))
            throw new IllegalArgumentException("Los combatientes de una pareja deben ser distintos");

        this.ID = ID;

        combatientes = new ArrayList<>();
        combatientes.add(combatiente1);
        combatientes.add(combatiente2);
    }

    
    //compara solo por id
    @Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(ID));
	}

	@Override 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pareja other = (Pareja) obj;
		return ID == other.ID;
	}

	public boolean estaDerrotada()
	{
		return combatientes.getFirst().estaFueraDeCombate() && combatientes.getLast().estaFueraDeCombate();
	}

	public int getID()
	{
		return ID;
	}
	public Combatiente getCombatiente1() {
        return combatientes.getFirst();
    }
    public Combatiente getCombatiente2() {
        return combatientes.getLast();
    }
    public int getVidaTotal()
    {
    	return combatientes.getFirst().getVidaAct() + combatientes.getLast().getVidaAct();
    }

	@Override
	public String toString()
	{
		return "Pareja #" + this.ID + " [" + this.getCombatiente1().getNombre() + " y " + this.getCombatiente2().getNombre() + "]";
	}
}

