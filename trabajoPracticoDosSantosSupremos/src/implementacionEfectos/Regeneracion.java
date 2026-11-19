package implementacionEfectos;

import combatiente.Combatiente;
import efecto.Efecto;

public class Regeneracion extends Efecto
{
    private int curacionPorTurno;

    public Regeneracion(int curacionPorTurno, int duracionTurnos) 
    {
        super("Regeneracion", duracionTurnos);
        if (curacionPorTurno <= 0)
            throw new IllegalArgumentException("La intensidad por turno debe ser positiva.");
        this.curacionPorTurno = curacionPorTurno;
    }

    @Override
    protected void aplicarEfecto(Combatiente afectado) 
    {
        //cura por cada turno
        afectado.aumentarVidaAct(this.curacionPorTurno);
    }
}
