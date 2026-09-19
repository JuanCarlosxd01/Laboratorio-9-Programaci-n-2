package hilos;

import sistema.CentroLogistico;
import modelo.EstadoPaquete;
import modelo.Paquete;

public class AlmacenThread extends Thread {

    private final CentroLogistico c;
    private final int ciclo;

    public AlmacenThread(CentroLogistico c, int ciclo) {
        super("Almacen");
        this.c = c;
        this.ciclo = ciclo;
    }

    @Override
    public void run() {
        while (c.perteneceAlCiclo(ciclo)) {
            try {
                c.esperarSiPausado();

                Paquete p = c.recepcion.extraerMejor(x -> true);

                p.setEstado(EstadoPaquete.ALMACENADO);

                c.almacen.agregar(p);

                c.log(p.getCodigo() + " almacenado");

                Thread.sleep(800);

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}