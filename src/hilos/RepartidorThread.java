package hilos;

import sistema.CentroLogistico;
import modelo.*;
import java.util.Random;

public class RepartidorThread extends Thread {

    private final CentroLogistico c;
    private final int capacidad;
    private final Random r = new Random();

    private volatile EstadoRepartidor estado = EstadoRepartidor.DISPONIBLE;
    private int entregados;

    public RepartidorThread(CentroLogistico c, int n, int cap) {
        super("Repartidor-" + n);
        this.c = c;
        capacidad = cap;
    }

    public EstadoRepartidor getEstadoRepartidor() {
        return estado;
    }

    public int getEntregados() {
        return entregados;
    }

    public int getCapacidad() {
        return capacidad;
    }

    @Override
    public void run() {
        while (c.isActivo()) {
            try {
                c.esperarSiPausado();

                estado = EstadoRepartidor.CARGANDO;

                Paquete p = c.expedicion.extraerMejor(x -> true);

                p.setEstado(EstadoPaquete.EN_EXPEDICION);

                c.reparto.agregar(p);

                c.log(p.getCodigo() + " asignado a " + getName());

                estado = EstadoRepartidor.EN_RUTA;

                Thread.sleep(1000 + r.nextInt(1000));

                estado = EstadoRepartidor.ENTREGANDO;

                c.reparto.eliminar(p);

                if (r.nextInt(100) < 75) {
                    p.setEstado(EstadoPaquete.ENTREGADO);

                    c.entregados.agregar(p);

                    entregados++;

                    c.estadisticas.entregado(System.currentTimeMillis() - p.getCreado());

                    c.log(p.getCodigo() + " entregado por " + getName());

                } else {
                    int i = p.aumentarIntentos();

                    if (i >= 3) {
                        p.setEstado(EstadoPaquete.DEVUELTO);

                        c.devueltos.agregar(p);

                        c.estadisticas.devuelto();

                        c.log(p.getCodigo() + " devuelto tras 3 intentos");

                    } else {
                        p.setEstado(EstadoPaquete.NUEVO_INTENTO);

                        c.expedicion.agregar(p);

                        c.log(p.getCodigo() + " cliente ausente, intento " + i);
                    }
                }

                estado = EstadoRepartidor.REGRESANDO;

                Thread.sleep(500);

                estado = EstadoRepartidor.DISPONIBLE;

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}