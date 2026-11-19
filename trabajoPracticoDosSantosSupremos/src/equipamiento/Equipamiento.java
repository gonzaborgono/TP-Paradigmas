package equipamiento;
import combatiente.Combatiente;

//clase no final, solo referencia para poder hacer combatiente
public abstract class Equipamiento 
{
	private final String nombre;
	private final TipoEquipamiento tipo;
	public Equipamiento(String nombre, TipoEquipamiento tipo)
	{
		if (nombre == null || nombre.trim().isEmpty() || tipo == null)
			throw new IllegalArgumentException("El equipamiento requiere un nombre y una categoría.");
		this.nombre = nombre;
		this.tipo = tipo;
	}
	public abstract void aplicarEfecto(Combatiente combatiente);
	public abstract void retirarEfecto(Combatiente combatiente);
	public TipoEquipamiento getTipoEquipamiento()
	{
		return tipo;
	}
	public String getNombre()
	{
		return nombre;
	}
}
