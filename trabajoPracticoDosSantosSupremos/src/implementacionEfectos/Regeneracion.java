package implementacionEfectos;

import combatiente.Combatiente;
import efecto.Efecto;

public class Regeneracion extends Efecto
{
    private int curacionPorTurno;

    public Regeneracion(int curacionPorTurno, int duracionTurnos) 
    {
        if (curacionPorTurno <= 0 || duracionTurnos <= 0) 
            throw new IllegalArgumentException("Ni la duracion ni el daño pueden ser negativos o 0");
        super("Regeneracion", duracionTurnos);
        this.curacionPorTurno = curacionPorTurno;
    }

    @Override
    protected void aplicarEfecto(Combatiente afectado) 
    {
        //cura por cada turno
        afectado.aumentarVidaAct(this.curacionPorTurno);
    }
}
