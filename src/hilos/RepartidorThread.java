package hilos;

import estructuras.ListaEnlazada;
import sistema.CentroLogistico;
import modelo.EstadoPaquete;
import modelo.EstadoRepartidor;
import modelo.Paquete;
import java.util.Random;

public class RepartidorThread extends Thread {

    private final CentroLogistico c;
    private final int capacidad;
    private final String ruta;
    private final int ciclo;

    private final Random r = new Random();
    private final ListaEnlazada<Paquete> carga;

    private volatile EstadoRepartidor estado = EstadoRepartidor.DISPONIBLE;

    private int entregados;

    public RepartidorThread(CentroLogistico c, int n, int capacidad, String ruta, int ciclo) {
        super("Repartidor-" + n);

        this.c = c;
        this.capacidad = capacidad;
        this.ruta = ruta;
        this.ciclo = ciclo;

        carga = new ListaEnlazada<>(capacidad);
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

    public int getCargaActual() {
        return carga.tamanio();
    }

    public String getRuta() {
        return ruta;
    }

    @Override
    public void run() {
        while (c.perteneceAlCiclo(ciclo)) {
            try {
                cargarVehiculo();

                if (!c.perteneceAlCiclo(ciclo)) {
                    break;
                }

                realizarRuta();

            } catch (InterruptedException e) {
                break;
            }
        }

        estado = EstadoRepartidor.FUERA_DE_SERVICIO;
    }

    private void cargarVehiculo() throws InterruptedException {
        estado = EstadoRepartidor.CARGANDO;

        while (carga.tamanio() < capacidad && c.perteneceAlCiclo(ciclo)) {
            c.esperarSiPausado();

            Paquete p = c.expedicion.extraerMejor(x -> ruta.equals(x.getRutaAsignada()));

            p.setEstado(EstadoPaquete.EN_REPARTO);

            carga.agregar(p);
            c.reparto.agregar(p);

            c.log(p.getCodigo() + " cargado en " + getName() + " (" + carga.tamanio() + "/" + capacidad + ")");
        }
    }

    private void realizarRuta() throws InterruptedException {
        estado = EstadoRepartidor.EN_RUTA;

        c.log(getName() + " inicia " + ruta + " con " + carga.tamanio() + " paquetes");

        Thread.sleep(1000);

        while (!carga.estaVacia() && c.perteneceAlCiclo(ciclo)) {
            c.esperarSiPausado();

            Paquete p = carga.extraerPrimero();

            estado = EstadoRepartidor.ENTREGANDO;

            Thread.sleep(600 + r.nextInt(600));

            c.reparto.eliminar(p);

            if (r.nextInt(100) < 75) {
                p.setEstado(EstadoPaquete.ENTREGADO);

                c.entregados.agregar(p);

                entregados++;

                c.estadisticas.entregado(System.currentTimeMillis() - p.getCreado());

                c.log(p.getCodigo() + " entregado por " + getName());

            } else {
                int intento = p.aumentarIntentos();

                if (intento >= 3) {
                    p.setEstado(EstadoPaquete.DEVUELTO);

                    c.devueltos.agregar(p);

                    c.estadisticas.devuelto();

                    c.log(p.getCodigo() + " devuelto tras 3 intentos");

                } else {
                    p.setEstado(EstadoPaquete.NUEVO_INTENTO);
                    p.setEstado(EstadoPaquete.EN_EXPEDICION);

                    c.expedicion.agregar(p);

                    c.log(p.getCodigo() + " cliente ausente, intento " + intento);
                }
            }
        }

        estado = EstadoRepartidor.REGRESANDO;

        Thread.sleep(800);

        estado = EstadoRepartidor.DISPONIBLE;
    }
}