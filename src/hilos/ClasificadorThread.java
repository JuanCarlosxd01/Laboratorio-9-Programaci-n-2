package hilos;

import sistema.CentroLogistico;
import modelo.EstadoPaquete;
import modelo.Paquete;

public class ClasificadorThread extends Thread {

    private final CentroLogistico c;
    private final int ciclo;

    private volatile String procesando = "-";

    public ClasificadorThread(CentroLogistico c, int n, int ciclo) {
        super("Clasificador-" + n);
        this.c = c;
        this.ciclo = ciclo;
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

    public String getProcesando() {
        return procesando;
    }

    @Override
    public void run() {
        while (c.perteneceAlCiclo(ciclo)) {
            try {
                c.esperarSiPausado();

                Paquete p = c.almacen.extraerMejor(x -> true);

                procesando = p.getCodigo();

                p.setEstado(EstadoPaquete.CLASIFICANDO);

                c.clasificacion.agregar(p);

                c.log(p.getCodigo() + " tomado por " + getName());

                Thread.sleep(1800);

                c.clasificacion.eliminar(p);

                p.setRutaAsignada(ruta(p.getCiudad()));
                p.setEstado(EstadoPaquete.CLASIFICADO);

                c.empaquetado.agregar(p);

                c.log(p.getCodigo() + " clasificado → " + p.getRutaAsignada());

                procesando = "-";

            } catch (InterruptedException e) {
                procesando = "-";
                break;
            }
        }
    }
}