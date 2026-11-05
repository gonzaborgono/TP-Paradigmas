package combatiente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import equipamiento.Equipamiento;
import tecnica.Tecnica;
public abstract class Combatiente 
{
	//constantes
	public static final int maxEquipamientos = 3;
	public static final int usosTecnicasPorTorneo = 5;
	//id
	private final int ID;
	public static int proxID = 1;
	//nombre
	private final String nombre;
	//vida
	private float vidaMax;
	private float vidaAct;
	//energia
	private float energiaMax;
	private float energiaAct;
	//daño
	private float multDañoRecibido;
	private float multDañoRealizado;
	//equipamientos
	private List<Equipamiento> equipamientos = new ArrayList<Equipamiento>();
	//tecnicas
	private Map<Tecnica, Integer> tecnicas = new HashMap<Tecnica, Integer>();
	
	public Combatiente(String nombre, float vidaMax, float energiaMax, float multDañoRecibido, float multmultDañoRealizado)
	{
		if(vidaMax <= 0)
			throw new IllegalArgumentException("la vida no puede ser negativa o 0");
		if(energiaMax <= 0)
			throw new IllegalArgumentException("la energia no puede ser negativa o 0");
		if(multDañoRecibido <= 0)
			throw new IllegalArgumentException("el mutiplicador de daño recibido no puede ser negativo o 0");
		if(multmultDañoRealizado <= 0)
			throw new IllegalArgumentException("el mutiplicador de daño realizado no puede ser negativo o 0");
		if(nombre.length() == 0)
			throw new IllegalArgumentException("el combatiente debe tener un nombre");
		this.ID = this.proxID;
		this.proxID++;
		this.nombre = nombre;
		this.vidaMax = vidaMax;
		this.energiaMax = energiaMax;
		this.multDañoRealizado = multmultDañoRealizado;
		this.multDañoRecibido = multDañoRecibido;
		this.vidaAct = this.vidaMax;
		this.energiaAct = this.energiaMax;
	}
	//modificar vidaMax
	public void aumentarVidaMax(float aumento)
    {
        if(aumento <= 0)
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0");
        this.vidaMax *= aumento;
    }
    
    public void decrementarVidaMax(float disminucion)
    {
        if(disminucion <= 0)
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0");
        this.vidaMax /= disminucion;
        if(this.vidaAct > this.vidaMax)
            this.vidaAct = this.vidaMax;
    }
    //modificar vida
    public void aumentarVida(float aumento)
    {
        if(aumento <= 0)
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0");
        this.vidaAct += aumento;
        if(this.vidaAct > this.vidaMax)
            this.vidaAct = this.vidaMax;
    }
    
    public void decrementarVida(float disminucion)
    {
        if(disminucion <= 0)
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0");
        this.vidaAct -= disminucion;
        if(this.vidaAct < 0)
            this.vidaAct = 0;
    }
	
	//modificar energia max
    public void aumentarEnergiaMax(float aumento)
    {
        if(aumento <= 0)
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0");
        this.energiaMax *= aumento;
    }
    
    public void decrementarEnergiaMax(float disminucion)
    {
        if(disminucion <= 0)
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0");
        this.energiaMax /= disminucion;
        if(this.energiaAct > this.energiaMax)
            this.energiaAct = this.energiaMax;
    }
    //modificar energia
    public void aumentarEnergia(float aumento)
    {
        if(aumento <= 0)
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0");
        this.energiaAct += aumento;
        if(this.energiaAct > this.energiaMax)
            this.energiaAct = this.energiaMax;
    }
    
    public void decrementarEnergia(float disminucion)
    {
        if(disminucion <= 0)
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0");
        this.energiaAct -= disminucion;
        if(this.energiaAct < 0)
            this.energiaAct = 0;
    }
	
	//multiplicador de daño recibido (acordarse de que es un multiplicador)
    public void aumentarMultDañoRecibido(float aumento)
    {
        if(aumento <= 0)
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0");
        this.multDañoRecibido += aumento;
    }
    
    public void disminuirMultDañoRecibido(float disminucion)
    {
        if(disminucion <= 0)
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0");
        this.multDañoRecibido -= disminucion;
    }
    
    public void aumentarMultDañoRealizado(float aumento)
    {
        if(aumento <= 0)
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0");
        this.multDañoRealizado += aumento;
    }
  //multiplicador de daño realizado (acordarse de que es un multiplicador)
    public void disminuirMultDañoRealizado(float disminucion)
    {
        if(disminucion <= 0)
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0");
        this.multDañoRealizado -= disminucion;
    }

	//equipamiento
	private boolean hayDelMismoTipo(Equipamiento nuevo)
	{
		for(Equipamiento leido : equipamientos)
		{
			if(leido.getTipoEquipamiento() == nuevo.getTipoEquipamiento())
				return true;
		}
		return false;
	}
	private boolean quedaEspacioEquipamiento()
	{
		if(equipamientos.size() < maxEquipamientos)
			return true;
		return false;
	}
	public void equipar(Equipamiento nuevoEquipamiento)
	{
		if(hayDelMismoTipo(nuevoEquipamiento))
			throw new IllegalStateException("ya hay un equipamiento de ese tipo equipado");
		if(!quedaEspacioEquipamiento())
			throw new IllegalStateException("ya se llego al limite de equipamientos permitidos");
		equipamientos.add(nuevoEquipamiento);
		nuevoEquipamiento.aplicarEfectos(this);
	}
	public void desequipar(Equipamiento sacarEquipamiento)
	{
		if(!equipamientos.contains(sacarEquipamiento))
			throw new IllegalArgumentException("el combatiente no tiene el equipamiento que se quiere sacar");
		equipamientos.remove(sacarEquipamiento);
		sacarEquipamiento.retirarEfectos(this);
	}
	
	//tecnicas
	//sin tope de cantidad de tecncias (aunque no se pueden repetir)
	//no hago sacar ya que no se puede "olvidar" una tecnica un combatiente
	public void agregarTecnica(Tecnica nuevaTecnica)
	{
		if(tecnicas.containsKey(nuevaTecnica))
			throw new IllegalStateException("el combatiente ya posee esta tecnica");
		tecnicas.put(nuevaTecnica, usosTecnicasPorTorneo);
	}
	
	//solo valida por id
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Combatiente that = (Combatiente) obj;
        return ID == that.ID;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }
	
}
