package implementacionEquipamiento;
import combatiente.Combatiente;
import equipamiento.Equipamiento;
import equipamiento.TipoEquipamiento;
public class AnilloEspiritual extends Equipamiento
{
    public AnilloEspiritual()
    {
        super("Anillo espiritual", TipoEquipamiento.ANILLO);
    }
    public void aplicarEfecto(Combatiente combatiente)
    {
        combatiente.aumentarEnergiaMax(30);
    }
    public void retirarEfecto(Combatiente combatiente)
    {
        combatiente.disminuirEnergiaMax(30);
    }
}
