package equipamiento;
import combatiente.Combatiente;
import equipamiento.TipoEquipamiento;
//clase no final, solo referencia para poder hacer combatiente
public abstract class Equipamiento 
{
	private final String nombre;
	private final TipoEquipamiento tipo;
	public Equipamiento(String nombre, TipoEquipamiento tipo)
	{
		if(nombre.length() == 0)
			throw new IllegalArgumentException("todo equipamiento debe tener un nombre");
		this.nombre = nombre;
		this.tipo = tipo;
	}
	public abstract void aplicarEfectos(Combatiente combatiente);
	public abstract void retirarEfectos(Combatiente combatiente);
	public TipoEquipamiento getTipoEquipamiento()
	{
		return tipo;
	}
	public String getNombre()
	{
		return nombre;
	}
}
