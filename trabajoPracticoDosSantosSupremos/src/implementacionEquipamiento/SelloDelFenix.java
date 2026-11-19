package implementacionEquipamiento;

import combatiente.Combatiente;
import equipamiento.Equipamiento;
import equipamiento.TipoEquipamiento;
public class SelloDelFenix extends Equipamiento{
    public SelloDelFenix()
    {
        super("Sello del fenix", TipoEquipamiento.SELLO);
    }
    @Override
    public void aplicarEfecto(Combatiente combatiente)
    {
        combatiente.aumentarVidaMax(30);
    }
    @Override
    public void retirarEfecto(Combatiente combatiente)
    {
        combatiente.disminuirVidaMax(30);
    }
}
