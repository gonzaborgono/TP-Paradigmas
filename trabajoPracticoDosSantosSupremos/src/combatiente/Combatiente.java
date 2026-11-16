package combatiente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import equipamiento.Equipamiento;
import implementacionTecnicas.AtaqueBasico;
import tecnica.Tecnica;
public abstract class Combatiente implements Comparable<Combatiente>
{
    public static final int MAX_EQUIPAMIENTOS = 3;
    public static final int USOS_TECNICAS_POR_TORNEO = 5;
    
    private static int proxId = 1;

    private final int id;
    private final String nombre;
    private int vidaMax;
    private int vidaAct;
    private int energiaMax;
    private int energiaAct;
    private double multDanoRecibido;
    private double multDanoRealizado;
    private double multGastoEnergia;
    private List<Equipamiento> equipamientos;
    private List<Tecnica> ordenAprendidas;
    private Map<Tecnica, Integer> tecnicas;
    private boolean defensaActiva;
    private double multDefensaTemporal = 1.0;
    private AtaqueBasico basico = new AtaqueBasico();
    public Combatiente(String nombre, int vidaMax, int energiaMax, double multDanoRecibido, double multDanoRealizado, double multGastoEnergia) {
        if (nombre == null || nombre.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("El combatiente debe tener un nombre válido.");
        }
        if (vidaMax <= 0) 
        {
            throw new IllegalArgumentException("La vida máxima no puede ser negativa o 0.");
        }
        if (energiaMax <= 0) 
        {
            throw new IllegalArgumentException("La energía máxima no puede ser negativa o 0.");
        }
        if (multDanoRecibido <= 0) 
        {
            throw new IllegalArgumentException("El multiplicador de daño recibido no puede ser negativo o 0.");
        }
        if (multDanoRealizado <= 0) 
        {
            throw new IllegalArgumentException("El multiplicador de daño realizado no puede ser negativo o 0.");
        }
        if (multGastoEnergia <= 0) 
        {
            throw new IllegalArgumentException("El multiplicador de gasto de energía no puede ser negativo o 0.");
        }

        this.id = proxId;
        proxId++;
        this.nombre = nombre;
        this.vidaMax = vidaMax;
        this.vidaAct = vidaMax;
        this.energiaMax = energiaMax;
        this.energiaAct = energiaMax;
        this.multDanoRecibido = multDanoRecibido;
        this.multDanoRealizado = multDanoRealizado;
        this.multGastoEnergia = multGastoEnergia;
        this.equipamientos = new ArrayList<Equipamiento>();
        this.ordenAprendidas = new ArrayList<Tecnica>();
        this.tecnicas = new HashMap<Tecnica, Integer>();
        this.defensaActiva = false;
    }
    public AtaqueBasico getBasico()
    {
    	return basico;
    }
    public int calcularDanoRealizado(int danoBase) 
    {
        if (danoBase < 0) 
        {
            throw new IllegalArgumentException("El daño base no puede ser negativo.");
        }
        
        return (int) Math.round(danoBase * this.multDanoRealizado);
    }
    public void recibirDano(int cantidad) 
    {
        int danoFinal = (int) Math.round(cantidad * multDanoRecibido * multDefensaTemporal);
        this.disminuirVidaAct(danoFinal);
    }
    public void gastarEnergia(int cantidad)
    {
    	int gastoFinal = (int) Math.round(cantidad * multGastoEnergia);
        this.disminuirEnergiaAct(gastoFinal);
    }

    public void resetearEstado() 
    {
        this.vidaAct = this.vidaMax;
        this.energiaAct = this.energiaMax;
        this.defensaActiva = false;
        this.multDefensaTemporal = 1.0;
    }

    public boolean estaFueraDeCombate() 
    {
        return this.vidaAct <= 0;
    }

    private boolean quedaEspacioEquipamiento() 
    {
        return this.equipamientos.size() < MAX_EQUIPAMIENTOS;
    }

    private boolean hayDelMismoTipo(Equipamiento nuevo) 
    {
        for (Equipamiento leido : equipamientos) 
        {
            if (leido.getTipoEquipamiento().equals(nuevo.getTipoEquipamiento())) 
            {
                return true;
            }
        }
        return false;
    }

    public void equipar(Equipamiento nuevoEquipamiento) 
    {
        if (nuevoEquipamiento == null) 
        {
            throw new IllegalArgumentException("El equipamiento no puede ser nulo.");
        }
        if (hayDelMismoTipo(nuevoEquipamiento)) 
        {
            throw new IllegalStateException("Ya hay un equipamiento de esa categoría equipado.");
        }
        if (!quedaEspacioEquipamiento()) 
        {
            throw new IllegalStateException("Ya se llegó al límite de equipamientos permitidos.");
        }
        equipamientos.add(nuevoEquipamiento);
        nuevoEquipamiento.aplicarEfecto(this);
    }

    public void desequipar(Equipamiento sacarEquipamiento) 
    {
        if (!equipamientos.contains(sacarEquipamiento)) 
        {
            throw new IllegalArgumentException("El combatiente no tiene el equipamiento que se quiere sacar.");
        }
        equipamientos.remove(sacarEquipamiento);
        sacarEquipamiento.retirarEfecto(this);
    }

    public void agregarTecnica(Tecnica nuevaTecnica) 
    {
        if (nuevaTecnica == null) 
        {
            throw new IllegalArgumentException("La técnica no puede ser nula.");
        }
        if (tecnicas.containsKey(nuevaTecnica)) 
        {
            throw new IllegalStateException("El combatiente ya posee esta técnica.");
        }
        if (nuevaTecnica == this.basico)
        {
            throw new IllegalArgumentException("La técnica no puede ser el ataque basico.");
        }
        tecnicas.put(nuevaTecnica, USOS_TECNICAS_POR_TORNEO);
        ordenAprendidas.add(nuevaTecnica);
    }

    //modificar vida
    public void aumentarVidaMax(int aumento) 
    {
        if (aumento <= 0) 
        {
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0.");
        }
        this.vidaMax += aumento;
        //vida actual no aumenta 
    }

    public void disminuirVidaMax(int disminucion) 
    {
        if (disminucion <= 0) 
        {
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0.");
        }
        this.vidaMax -= disminucion;
        if (this.vidaMax <= 0) 
        	throw new IllegalStateException("la vida maxima nunca puede ser 0 o negativa");
        if (this.vidaAct > this.vidaMax) 
        {
            this.vidaAct = this.vidaMax; //recortar vida si esta supera el maximo
        }
    }

    public void aumentarVidaAct(int aumento) 
    {
        if (aumento <= 0) 
        {
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0.");
        }
        this.vidaAct += aumento;
        if (this.vidaAct > this.vidaMax) //que no se pase del max
        {
            this.vidaAct = this.vidaMax;
        }
    }

    public void disminuirVidaAct(int disminucion) 
    {
        if (disminucion <= 0) 
        {
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0.");
        }
        this.vidaAct -= disminucion;
        if (this.vidaAct < 0) 
        {
            this.vidaAct = 0;
        }
    }

    //modiicar energia
    public void aumentarEnergiaMax(int aumento) 
    {
        if (aumento <= 0) 
        {
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0.");
        }
        this.energiaMax += aumento;
        //no modificar energia actual
    }

    public void disminuirEnergiaMax(int disminucion) 
    {
        if (disminucion <= 0) 
        {
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0.");
        }
        this.energiaMax -= disminucion;
        if (this.energiaMax < 0) 
        	throw new IllegalStateException("la energia maxima nunca puede ser 0 o negativa");
        if (this.energiaAct > this.energiaMax) 
        {
            this.energiaAct = this.energiaMax;
        }
    }

    public void aumentarEnergiaAct(int aumento) 
    {
        if (aumento <= 0) 
        {
            throw new IllegalArgumentException("El aumento no puede ser negativo o 0.");
        }
        this.energiaAct += aumento;
        if (this.energiaAct > this.energiaMax) 
        {
            this.energiaAct = this.energiaMax;
        }
    }

    public void disminuirEnergiaAct(int disminucion) 
    {
        if (disminucion <= 0) 
        {
            throw new IllegalArgumentException("La disminución no puede ser negativa o 0.");
        }
        this.energiaAct -= disminucion;
        if (this.energiaAct < 0) 
        {
            this.energiaAct = 0;
        }
    }

    //modificar multiplicadores
    public void aumentarMultDanoRecibido(double valor) 
    {
        if (valor <= 0) throw new IllegalArgumentException("El valor no puede ser negativo o 0.");
        this.multDanoRecibido += valor;
    }

    public void disminuirMultDanoRecibido(double valor) 
    {
        if (valor <= 0) throw new IllegalArgumentException("El valor no puede ser negativo o 0.");
        this.multDanoRecibido -= valor;
    }

    public void aumentarMultDanoRealizado(double valor) 
    {
        if (valor <= 0) throw new IllegalArgumentException("El valor no puede ser negativo o 0.");
        this.multDanoRealizado += valor;
    }

    public void disminuirMultDanoRealizado(double valor) 
    {
        if (valor <= 0) throw new IllegalArgumentException("El valor no puede ser negativo o 0.");
        this.multDanoRealizado -= valor;
    }

    public void aumentarMultGastoEnergia(double valor) 
    {
        if (valor <= 0) throw new IllegalArgumentException("El valor no puede ser negativo o 0.");
        this.multGastoEnergia += valor;
    }

    public void disminuirMultGastoEnergia(double valor) 
    {
        if (valor <= 0) throw new IllegalArgumentException("El valor no puede ser negativo o 0.");
        this.multGastoEnergia -= valor;
    }
    

    //estado defensivo
    public boolean tieneDefensivaActiva() {
        return defensaActiva;
    }

    public void activarDefensaTemporal(double reduccionDano) 
    {
        if (reduccionDano <= 0 || reduccionDano > 1) 
        {
            throw new IllegalArgumentException("El factor de reducción debe estar entre 0 y 1.");
        }
        this.defensaActiva = true;
        this.multDefensaTemporal -= reduccionDano; 
        if (this.multDefensaTemporal < 0) 
        {
            this.multDefensaTemporal = 0.0;
        }
    }

    //comparar basado en id
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Combatiente that = (Combatiente) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaMax() {
        return vidaMax;
    }

    public int getVidaAct() {
        return vidaAct;
    }

    public int getEnergiaMax() {
        return energiaMax;
    }

    public int getEnergiaAct() {
        return energiaAct;
    }

    public double getMultDanoRecibido() {
        return multDanoRecibido;
    }

    public double getMultDanoRealizado() {
        return multDanoRealizado;
    }

    public double getMultGastoEnergia() {
        return multGastoEnergia;
    }

    public List<Equipamiento> getEquipamientos() {
        return equipamientos;
    }

    public Map<Tecnica, Integer> getTecnicas() {
        return tecnicas;
    }
    
    public void usarTecnica(Tecnica tecnica)
    {
    	Integer usos = tecnicas.get(tecnica);
    	if(usos == null)
    		throw new IllegalArgumentException("el combatiente no tiene la tecnica que se quiere usar");
    	if(usos <= 0)
    		throw new IllegalArgumentException("la tecnica ya no tiene usos");
    	tecnicas.put(tecnica, usos-1);
    }
    
    public int compareTo(Combatiente otro)
    {
    	return this.id - otro.id;
    }
    
    public List<Tecnica> getOrdenAprendidas()
    {
    	return ordenAprendidas;
    }
    
    @Override
    public String toString()
    {
    	return nombre + " ID: " + id;
    }
    
}