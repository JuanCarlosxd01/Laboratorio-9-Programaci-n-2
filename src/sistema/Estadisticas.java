package sistema;

public class Estadisticas {

    private int generados;
    private int entregados;
    private int devueltos;
    private long tiempoTotal;

    public synchronized void generado() {
        generados++;
    }

    public synchronized void entregado(long ms) {
        entregados++;
        tiempoTotal += ms;
    }

    public synchronized void devuelto() {
        devueltos++;
    }

    public synchronized int getGenerados() {
        return generados;
    }

    public synchronized int getEntregados() {
        return entregados;
    }

    public synchronized int getDevueltos() {
        return devueltos;
    }

    public synchronized int getEnProceso() {
        int enProceso = generados - entregados - devueltos;

        if (enProceso < 0) {
            return 0;
        }

        return enProceso;
    }

    public synchronized double getPromedioSegundos() {
        if (entregados == 0) {
            return 0;
        }

        return (tiempoTotal / 1000.0) / entregados;
    }

    public synchronized void reiniciar() {
        generados = 0;
        entregados = 0;
        devueltos = 0;
        tiempoTotal = 0;
    }
}