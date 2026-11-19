package combatiente;
import equipamiento.Equipamiento;
import implementacionTecnicas.AtaqueBasico;
import java.util.ArrayList;
import escuela.Escuela;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import tecnica.Tecnica;
import efecto.Efecto;
public abstract class Combatiente implements Comparable<Combatiente>
{
    public static final int MAX_EQUIPAMIENTOS = 3;
    public static final int USOS_TECNICAS_POR_TORNEO = 5;
    

    private final int id;
    private final String nombre;
    private final Escuela escuela;
    private int vidaMax;
    private int vidaAct;
    private int energiaMax;
    private int energiaAct;
    private double multDanoRecibido;
    private double multDanoRealizado;
    private double multGastoEnergia;
    private final List<Equipamiento> equipamientos;
    private final Map<Tecnica, Integer> tecnicas;
    private boolean enCombate;
    private boolean defensaActiva;
    private double multDefensaTemporal = 1.0;
    private final AtaqueBasico basico = new AtaqueBasico();
    //atributo para controlar el bonus al caer compañero
    private boolean reaccionActiva;
    //lista de efectos activos que tiene
    private final List<Efecto> efectosActivos = new ArrayList<>();

    protected Combatiente(String nombre, Escuela escuela, int vidaMax, int energiaMax,
            double multDanoRecibido, double multDanoRealizado, double multGastoEnergia) {
        if (nombre == null || nombre.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("El combatiente debe tener un nombre válido.");
        }
        if (escuela == null)
            throw new IllegalArgumentException("El combatiente debe pertenecer a una escuela.");
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

        this.id = CreadorCombatientes.asignarId();
        this.nombre = nombre;
        this.escuela = escuela;
        this.vidaMax = vidaMax;
        this.vidaAct = vidaMax;
        this.energiaMax = energiaMax;
        this.energiaAct = energiaMax;
        this.multDanoRecibido = multDanoRecibido;
        this.multDanoRealizado = multDanoRealizado;
        this.multGastoEnergia = multGastoEnergia;
        this.equipamientos = new ArrayList<>();
        this.tecnicas = new LinkedHashMap<>();
        this.defensaActiva = false;
        this.reaccionActiva = false;
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
        
        return (int) Math.round(danoBase * getMultDanoRealizado());
    }
    public void recibirDano(int cantidad) 
    {
        if (cantidad < 0)
            throw new IllegalArgumentException("El daño no puede ser negativo.");
        int danoFinal = (int) Math.round(cantidad * getMultDanoRecibido() * multDefensaTemporal);
        if (danoFinal > 0)
            disminuirVidaAct(danoFinal);
    }

    public int calcularCostoEnergia(int cantidad) {
        if (cantidad < 0)
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        return (int) Math.round(cantidad * getMultGastoEnergia());
    }

    public void gastarEnergia(int cantidad) {
        int costo = calcularCostoEnergia(cantidad);
        if (costo > energiaAct)
            throw new IllegalStateException("Energía insuficiente para ejecutar la técnica.");
        if (costo > 0)
            disminuirEnergiaAct(costo);
    }

    public void iniciarCombate() {
        if (enCombate)
            throw new IllegalStateException("El combatiente ya está en un combate.");
        resetearEstado();
        enCombate = true;
    }

    public void finalizarCombate() {
        limpiarEfectosDelCombate();
        enCombate = false;
    }

    private void validarCambioDePreparacion() {
        if (enCombate)
            throw new IllegalStateException("La preparación solo puede cambiar entre combates.");
    }

    public void resetearEstado() 
    {
        if (enCombate)
            throw new IllegalStateException("No se puede restaurar un combatiente durante el combate.");
        this.vidaAct = this.vidaMax;
        this.energiaAct = this.energiaMax;
        limpiarEfectosDelCombate();
    }

    private void limpiarEfectosDelCombate() {
        this.defensaActiva = false;
        if (this.reaccionActiva)
            acomodarFinCompaneroFueraDeCombate();
        
        this.multDefensaTemporal = 1.0;
        this.efectosActivos.clear(); //limpiar lista de estados
    }
    //reaccion (es abstracta pq depende de cada tipo de combatiente)
    public abstract void reaccionarCompaneroFueraDeCombate();

    public void acomodarFinCompaneroFueraDeCombate() {
        reaccionActiva = false;
    }
    public boolean tieneReaccionActiva()
    {
        return reaccionActiva;
    }
    protected void activarReaccion()
    {
        this.reaccionActiva = true;
    }
    //metodos para manejar efectos
    public void agregarEfectoTemporal(Efecto efecto) 
    {
        if (efecto == null) 
        {
            throw new IllegalArgumentException("El efecto temporal no puede ser nulo");
        }
        this.efectosActivos.add(efecto);
    }
    public void procesarEfectosTemporales() 
    { //se hace con iterador para poder hacer el remove si ya se acabo, con un foreach no se puede
        Iterator<Efecto> it = this.efectosActivos.iterator();
        //si cae fuera de combate cortar el loop de los efectos
        while (it.hasNext() && !this.estaFueraDeCombate())
        {
            Efecto efecto = it.next();
            efecto.realizarTurno(this);
            if (efecto.termino())
            {
                it.remove();
            }
        }
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
        validarCambioDePreparacion();
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
        nuevoEquipamiento.aplicarEfecto(this);
        equipamientos.add(nuevoEquipamiento);
    }

    public void desequipar(Equipamiento sacarEquipamiento) 
    {
        validarCambioDePreparacion();
        if (!equipamientos.contains(sacarEquipamiento)) 
        {
            throw new IllegalArgumentException("El combatiente no tiene el equipamiento que se quiere sacar.");
        }
        sacarEquipamiento.retirarEfecto(this);
        equipamientos.remove(sacarEquipamiento);
    }

    public void agregarTecnica(Tecnica nuevaTecnica) 
    {
        validarCambioDePreparacion();
        if (nuevaTecnica == null) 
        {
            throw new IllegalArgumentException("La técnica no puede ser nula.");
        }
        if (tecnicas.containsKey(nuevaTecnica)) 
        {
            throw new IllegalStateException("El combatiente ya posee esta técnica.");
        }
        tecnicas.put(nuevaTecnica, USOS_TECNICAS_POR_TORNEO);
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
        if (disminucion >= this.vidaMax)
            throw new IllegalStateException("La vida máxima debe ser positiva.");
        this.vidaMax -= disminucion;
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
        if (enCombate && estaFueraDeCombate())
            return;
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
        if (disminucion >= this.energiaMax)
            throw new IllegalStateException("La energía máxima debe ser positiva.");
        this.energiaMax -= disminucion;
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
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Combatiente)) return false;
        Combatiente that = (Combatiente) obj;
        return id == that.id;
    }

    @Override
    public final int hashCode() {
        return Integer.hashCode(id);
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

    public Escuela getEscuela() {
        return escuela;
    }

    public List<Equipamiento> getEquipamientos() {
        return List.copyOf(equipamientos);
    }

    public Map<Tecnica, Integer> getTecnicas() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(tecnicas));
    }

    public int getUsosRestantes(Tecnica tecnica) {
        return tecnicas.getOrDefault(tecnica, 0);
    }

    public void reiniciarUsosTecnicas() {
        validarCambioDePreparacion();
        for (Map.Entry<Tecnica, Integer> entrada : tecnicas.entrySet()) {
            entrada.setValue(USOS_TECNICAS_POR_TORNEO);
        }
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
    @Override
    public int compareTo(Combatiente otro)
    {
        return Integer.compare(this.id, otro.id);
    }
    
    public List<Tecnica> getOrdenAprendidas()
    {
        return new ArrayList<>(tecnicas.keySet());
    }
    
    @Override
    public String toString()
    {
    	return nombre + " ID: " + id;
    }
    
}
