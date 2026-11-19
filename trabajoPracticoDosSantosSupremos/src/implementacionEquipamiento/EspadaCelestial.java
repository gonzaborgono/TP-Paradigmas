package implementacionEquipamiento;
import combatiente.Combatiente;
import equipamiento.Equipamiento;
import equipamiento.TipoEquipamiento;
public class EspadaCelestial extends Equipamiento
{
    public EspadaCelestial()
    {
        super("Espada Celestial", TipoEquipamiento.ARMA);
    }
    @Override
    public void aplicarEfecto(Combatiente combatiente)
    {
        combatiente.aumentarMultDanoRealizado(0.15);
    }
    @Override
    public void retirarEfecto(Combatiente combatiente)
    {
        combatiente.disminuirMultDanoRealizado(0.15);
    }
}
