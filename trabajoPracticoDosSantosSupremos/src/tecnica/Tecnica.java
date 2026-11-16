package tecnica;

import combatiente.Combatiente;

public abstract class Tecnica implements Accion 
{
    private final String nombre;
    private final int costoEnergia;
    private final TipoTecnica tipo;

    public Tecnica(String nombre, int costoEnergia, TipoTecnica tipo) 
    {
        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
        this.tipo = tipo;
    }

    public String getNombre() 
    {
        return nombre;
    }

    public int getCostoEnergia() 
    {
        return costoEnergia;
    }

    public TipoTecnica getTipo() 
    {
        return tipo;
    }

    @Override
    public abstract void ejecutar(Combatiente ejecutor, Combatiente objetivo);

    @Override 
    public String toString() 
    {
        return nombre + ", Tipo: " + tipo;
    }
}
