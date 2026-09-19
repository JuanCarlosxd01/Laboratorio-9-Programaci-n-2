
package hilos;

import sistema.CentroLogistico;
import modelo.*;

public class ClasificadorThread extends Thread {

    private final CentroLogistico c;

    public ClasificadorThread(CentroLogistico c, int n) {
        super("Clasificador-" + n);
        this.c = c;
    }

    private String ruta(String ciudad) {
        if (ciudad.equals("Centro") || ciudad.equals("Eixample")) {
            return "Ruta 1";
        }

        if (ciudad.equals("Gràcia")) {
            return "Ruta 2";
        }

        if (ciudad.equals("Sant Martí")) {
            return "Ruta 3";
        }

        return "Ruta 4";
    }

    @Override
    public void run() {
        while (c.isActivo()) {
            try {
                c.esperarSiPausado();

                Paquete p = c.almacen.extraerMejor(x -> true);

                p.setEstado(EstadoPaquete.CLASIFICANDO);
                c.clasificacion.agregar(p);

                c.log(p.getCodigo() + " tomado por " + getName());

                Thread.sleep(700);

                c.clasificacion.eliminar(p);

                p.setRutaAsignada(ruta(p.getCiudad()));
                p.setEstado(EstadoPaquete.CLASIFICADO);

                c.empaquetado.agregar(p);

                c.log(p.getCodigo() + " clasificado → " + p.getRutaAsignada());

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}