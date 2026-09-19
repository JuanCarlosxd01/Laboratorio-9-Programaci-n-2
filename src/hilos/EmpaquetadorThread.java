package hilos;

import sistema.CentroLogistico;
import modelo.*;

public class EmpaquetadorThread extends Thread {

    private final CentroLogistico c;

    public EmpaquetadorThread(CentroLogistico c, int n) {
        super("Empaquetador-" + n);
        this.c = c;
    }

    @Override
    public void run() {
        while (c.isActivo()) {
            try {
                c.esperarSiPausado();

                Paquete p = c.empaquetado.extraerMejor(x -> true);

                p.setEstado(EstadoPaquete.EMPAQUETANDO);

                c.log(getName() + " empaquetando " + p.getCodigo());

                long t;

                if (p.getPeso() <= 2) {
                    t = 1000;
                } else if (p.getPeso() <= 5) {
                    t = 2000;
                } else {
                    t = 3000;
                }

                Thread.sleep(t);

                p.setEstado(EstadoPaquete.EMPAQUETADO);

                c.expedicion.agregar(p);

                c.log(p.getCodigo() + " empaquetado");

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}