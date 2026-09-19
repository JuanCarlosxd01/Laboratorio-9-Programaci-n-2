package hilos;

import sistema.CentroLogistico;
import modelo.EstadoPaquete;
import modelo.Paquete;

public class EmpaquetadorThread extends Thread {

    private final CentroLogistico c;
    private final int ciclo;
    private volatile String procesando = "-";

    public EmpaquetadorThread(CentroLogistico c, int n, int ciclo) {
        super("Empaquetador-" + n);
        this.c = c;
        this.ciclo = ciclo;
    }

    public String getProcesando() {
        return procesando;
    }

    @Override
    public void run() {
        while (c.perteneceAlCiclo(ciclo)) {
            try {
                c.esperarSiPausado();

                Paquete p = c.empaquetado.extraerMejor(x -> true);

                procesando = p.getCodigo();

                p.setEstado(EstadoPaquete.EMPAQUETANDO);

                c.log(getName() + " empaquetando " + p.getCodigo());

                long tiempo;

                if (p.getPeso() <= 2) {
                    tiempo = 1000;
                } else if (p.getPeso() <= 5) {
                    tiempo = 2000;
                } else {
                    tiempo = 3000;
                }

                Thread.sleep(tiempo);

                p.setEstado(EstadoPaquete.EMPAQUETADO);
                p.setEstado(EstadoPaquete.EN_EXPEDICION);

                c.expedicion.agregar(p);

                c.log(p.getCodigo() + " terminó de ser empaquetado");

                procesando = "-";

            } catch (InterruptedException e) {
                procesando = "-";
                break;
            }
        }
    }
}