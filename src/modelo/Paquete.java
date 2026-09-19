
package modelo;

public class Paquete {
    private final String codigo, cliente, direccion, ciudad;
    private final double peso;
    private final Prioridad prioridad;
    private volatile EstadoPaquete estado;
    private String rutaAsignada;
    private int intentos;
    private final long creado = System.currentTimeMillis();

    public Paquete(String codigo, String cliente, String direccion, String ciudad, double peso, Prioridad prioridad) {
        this.codigo = codigo; 
        this.cliente = cliente;
        this.direccion = direccion;
        this.ciudad = ciudad; 
        this.peso = peso; 
        this.prioridad = prioridad; 
        this.estado = EstadoPaquete.RECIBIDO;
    }
    
    public String getCodigo(){
        return codigo;
    } 
    
    public String getCliente(){
        return cliente;
    } 
    
    public String getDireccion(){
        return direccion;
    } 
    
    public String getCiudad(){
        return ciudad;
    }
    
    public double getPeso(){
        return peso;
    } 
    public Prioridad getPrioridad(){
        return prioridad;
    } 
    
    public EstadoPaquete getEstado(){
        return estado;
    } 
    
    public synchronized void setEstado(EstadoPaquete e){
        estado=e;
    }
    
    public String getRutaAsignada(){
        return rutaAsignada;
    } 
    
    public void setRutaAsignada(String r){
        rutaAsignada=r;
    } 
    
    public int getIntentos(){
        return intentos;
    } 
    
    public synchronized int aumentarIntentos(){
        return ++intentos;
    }
    
    public long getCreado(){
        return creado;
    } 
    
    @Override public String toString(){
        return codigo+" ["+prioridad+"]";
    }
}
