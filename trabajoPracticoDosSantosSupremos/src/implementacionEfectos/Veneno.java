package implementacionEfectos;

import combatiente.Combatiente;
import efecto.Efecto;

public class Veneno extends Efecto
{
    private int danoPorTurno;
    public Veneno(int danoPorTurno, int duracionTurnos)
    {
        super("Veneno", duracionTurnos);
        if (danoPorTurno <= 0)
            throw new IllegalArgumentException("La intensidad por turno debe ser positiva.");
        this.danoPorTurno = danoPorTurno;
    }
    @Override
    protected void aplicarEfecto(Combatiente combatiente)
    {
        //hace daño ignorando la resistencia
        combatiente.disminuirVidaAct(danoPorTurno);
    }
}
