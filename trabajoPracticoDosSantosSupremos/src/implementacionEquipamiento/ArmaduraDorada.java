package implementacionEquipamiento;
import combatiente.Combatiente;
import equipamiento.Equipamiento;
import equipamiento.TipoEquipamiento;
public class ArmaduraDorada extends Equipamiento
{
    public ArmaduraDorada()
    {
        super("Armadura Dorada", TipoEquipamiento.ARMADURA);
    }
    public void aplicarEfecto(Combatiente combatiente)
    {
        combatiente.disminuirMultDanoRecibido(0.15);
    }
    public void retirarEfecto(Combatiente combatiente)
    {
        combatiente.aumentarMultDanoRecibido(0.15);
    }

}
