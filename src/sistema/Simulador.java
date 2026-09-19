package sistema;

import hilos.AlmacenThread;
import hilos.ClasificadorThread;
import hilos.EmpaquetadorThread;
import hilos.RecepcionThread;
import hilos.RepartidorThread;

public class Simulador {

    private final CentroLogistico c;

    private RecepcionThread recepcion;
    private AlmacenThread almacen;

    private ClasificadorThread[] clasificadores;
    private EmpaquetadorThread[] empaquetadores;
    private RepartidorThread[] repartidores;

    public Simulador(CentroLogistico c) {
        this.c = c;
    }

    public synchronized void iniciar() {
        if (c.isActivo()) {
            return;
        }

        int ciclo = c.iniciarNuevoCiclo();

        recepcion = new RecepcionThread(c, ciclo);
        almacen = new AlmacenThread(c, ciclo);

        clasificadores = new ClasificadorThread[3];

        for (int i = 0; i < clasificadores.length; i++) {
            clasificadores[i] = new ClasificadorThread(c, i + 1, ciclo);
        }

        empaquetadores = new EmpaquetadorThread[2];

        for (int i = 0; i < empaquetadores.length; i++) {
            empaquetadores[i] = new EmpaquetadorThread(c, i + 1, ciclo);
        }

        repartidores = new RepartidorThread[4];

        repartidores[0] = new RepartidorThread(c, 1, 5, "Ruta 1", ciclo);
        repartidores[1] = new RepartidorThread(c, 2, 4, "Ruta 2", ciclo);
        repartidores[2] = new RepartidorThread(c, 3, 6, "Ruta 3", ciclo);
        repartidores[3] = new RepartidorThread(c, 4, 5, "Ruta 4", ciclo);

        recepcion.start();
        almacen.start();

        for (ClasificadorThread clasificador : clasificadores) {
            clasificador.start();
        }

        for (EmpaquetadorThread empaquetador : empaquetadores) {
            empaquetador.start();
        }

        for (RepartidorThread repartidor : repartidores) {
            repartidor.start();
        }

        c.log("Simulación iniciada");
    }

    public void pausar() {
        c.pausar();
        c.log("Simulación pausada");
    }

    public void reanudar() {
        c.reanudar();
        c.log("Simulación reanudada");
    }

    public synchronized void detener() {
        c.detener();

        interrumpirHilos();

        c.log("Simulación detenida");
    }

    public synchronized void reiniciar() {
        detener();

        c.limpiar();

        iniciar();

        c.log("Simulación reiniciada");
    }

    private void interrumpirHilos() {
        if (recepcion != null) {
            recepcion.interrupt();
        }

        if (almacen != null) {
            almacen.interrupt();
        }

        if (clasificadores != null) {
            for (ClasificadorThread clasificador : clasificadores) {
                clasificador.interrupt();
            }
        }

        if (empaquetadores != null) {
            for (EmpaquetadorThread empaquetador : empaquetadores) {
                empaquetador.interrupt();
            }
        }

        if (repartidores != null) {
            for (RepartidorThread repartidor : repartidores) {
                repartidor.interrupt();
            }
        }
    }

    public ClasificadorThread[] getClasificadores() {
        return clasificadores;
    }

    public EmpaquetadorThread[] getEmpaquetadores() {
        return empaquetadores;
    }

    public RepartidorThread[] getRepartidores() {
        return repartidores;
    }
}