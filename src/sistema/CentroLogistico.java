package sistema;

import estructuras.ListaEnlazada;
import modelo.Paquete;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class CentroLogistico {

    public final ListaEnlazada<Paquete> recepcion = new ListaEnlazada<>(10);
    public final ListaEnlazada<Paquete> almacen = new ListaEnlazada<>(20);
    public final ListaEnlazada<Paquete> clasificacion = new ListaEnlazada<>(10);
    public final ListaEnlazada<Paquete> empaquetado = new ListaEnlazada<>(8);
    public final ListaEnlazada<Paquete> expedicion = new ListaEnlazada<>(15);
    public final ListaEnlazada<Paquete> reparto = new ListaEnlazada<>(30);
    public final ListaEnlazada<Paquete> entregados = new ListaEnlazada<>(10000);
    public final ListaEnlazada<Paquete> devueltos = new ListaEnlazada<>(10000);

    public final Estadisticas estadisticas = new Estadisticas();

    private volatile boolean activo;
    private volatile boolean pausado;
    private volatile int ciclo;

    private Consumer<String> listenerLog;

    public synchronized int iniciarNuevoCiclo() {
        ciclo++;
        activo = true;
        pausado = false;
        notifyAll();

        return ciclo;
    }

    public synchronized void detener() {
        activo = false;
        pausado = false;
        ciclo++;

        notifyAll();
        despertarListas();
    }

    public synchronized void pausar() {
        if (activo) {
            pausado = true;
        }
    }

    public synchronized void reanudar() {
        pausado = false;
        notifyAll();
    }

    public synchronized void esperarSiPausado() throws InterruptedException {
        while (pausado && activo) {
            wait();
        }

        if (!activo) {
            throw new InterruptedException();
        }
    }

    public boolean perteneceAlCiclo(int cicloHilo) {
        return activo && cicloHilo == ciclo;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isPausado() {
        return pausado;
    }

    public void setListenerLog(Consumer<String> listenerLog) {
        this.listenerLog = listenerLog;
    }

    public void log(String mensaje) {
        String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String linea = hora + " | " + mensaje;

        if (listenerLog != null) {
            listenerLog.accept(linea);
        }
    }

    private void despertarListas() {
        recepcion.despertarTodos();
        almacen.despertarTodos();
        clasificacion.despertarTodos();
        empaquetado.despertarTodos();
        expedicion.despertarTodos();
        reparto.despertarTodos();
        entregados.despertarTodos();
        devueltos.despertarTodos();
    }

    public void limpiar() {
        recepcion.limpiar();
        almacen.limpiar();
        clasificacion.limpiar();
        empaquetado.limpiar();
        expedicion.limpiar();
        reparto.limpiar();
        entregados.limpiar();
        devueltos.limpiar();

        estadisticas.reiniciar();
    }
}