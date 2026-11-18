package implementacionEfectos;

import combatiente.Combatiente;
import efecto.Efecto;

public class Veneno extends Efecto
{
    private int danoPorTurno;
    public Veneno(int danoPorTurno, int duracionTurnos)
    {
        if (duracionTurnos <= 0 || danoPorTurno <= 0) 
            throw new IllegalArgumentException("Ni la duracion ni el daño pueden ser negativos o 0");

        super("Veneno", duracionTurnos);
        this.danoPorTurno = danoPorTurno;
    }
    @Override
    protected void aplicarEfecto(Combatiente combatiente)
    {
        //hace daño ignorando la resistencia
        combatiente.disminuirVidaAct(danoPorTurno);
    }
}
