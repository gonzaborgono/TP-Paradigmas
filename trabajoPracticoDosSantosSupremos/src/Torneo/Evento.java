package Torneo;

import java.time.LocalDateTime;

public abstract class Evento 
{
	private LocalDateTime fecha;
	private String descripcion;
	public Evento(String descripcion)
	{
		this.descripcion = descripcion;
		this.fecha = LocalDateTime.now();
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public String getDescripcion() {
		return descripcion;
	}	
}
