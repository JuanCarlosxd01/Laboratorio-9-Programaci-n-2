package modelo;

public class Paquete {

    private final String codigo;
    private final String cliente;
    private final String direccion;
    private final String ciudad;
    private final double peso;
    private final Prioridad prioridad;
    private final long creado;

    private EstadoPaquete estado;
    private String rutaAsignada;
    private int intentos;

    public Paquete(String codigo, String cliente, String direccion, String ciudad, double peso, Prioridad prioridad) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.peso = peso;
        this.prioridad = prioridad;
        this.estado = EstadoPaquete.RECIBIDO;
        this.creado = System.currentTimeMillis();
        this.intentos = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public double getPeso() {
        return peso;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public synchronized EstadoPaquete getEstado() {
        return estado;
    }

    public String getRutaAsignada() {
        return rutaAsignada;
    }

    public int getIntentos() {
        return intentos;
    }

    public long getCreado() {
        return creado;
    }

    public void setRutaAsignada(String rutaAsignada) {
        this.rutaAsignada = rutaAsignada;
    }

    public synchronized int aumentarIntentos() {
        intentos++;
        return intentos;
    }

    public synchronized void setEstado(EstadoPaquete nuevoEstado) {
        if (estado == nuevoEstado) {
            return;
        }

        if (!transicionValida(estado, nuevoEstado)) {
            throw new IllegalStateException("Transición inválida: " + estado + " -> " + nuevoEstado);
        }

        estado = nuevoEstado;
    }

    private boolean transicionValida(EstadoPaquete actual, EstadoPaquete nuevo) {
        switch (actual) {
            case RECIBIDO:
                return nuevo == EstadoPaquete.ALMACENADO;

            case ALMACENADO:
                return nuevo == EstadoPaquete.CLASIFICANDO;

            case CLASIFICANDO:
                return nuevo == EstadoPaquete.CLASIFICADO;

            case CLASIFICADO:
                return nuevo == EstadoPaquete.EMPAQUETANDO;

            case EMPAQUETANDO:
                return nuevo == EstadoPaquete.EMPAQUETADO;

            case EMPAQUETADO:
                return nuevo == EstadoPaquete.EN_EXPEDICION;

            case EN_EXPEDICION:
                return nuevo == EstadoPaquete.EN_REPARTO;

            case EN_REPARTO:
                return nuevo == EstadoPaquete.ENTREGADO
                        || nuevo == EstadoPaquete.NUEVO_INTENTO
                        || nuevo == EstadoPaquete.DEVUELTO;

            case NUEVO_INTENTO:
                return nuevo == EstadoPaquete.EN_EXPEDICION;

            case ENTREGADO:
            case DEVUELTO:
                return false;

            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return codigo + " | " + prioridad + " | " + estado;
    }
}