
package sistema;

import estructuras.ListaEnlazada;
import modelo.Paquete;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class CentroLogistico {
    public final ListaEnlazada<Paquete> recepcion = new ListaEnlazada<>(10);
    public final ListaEnlazada<Paquete> almacen = new ListaEnlazada<>(20);
    public final ListaEnlazada<Paquete> clasificacion = new ListaEnlazada<>(10);
    public final ListaEnlazada<Paquete> empaquetado = new ListaEnlazada<>(8);
    public final ListaEnlazada<Paquete> expedicion = new ListaEnlazada<>(15);
    public final ListaEnlazada<Paquete> reparto = new ListaEnlazada<>(30);
    public final ListaEnlazada<Paquete> entregados = new ListaEnlazada<>(999);
    public final ListaEnlazada<Paquete> devueltos = new ListaEnlazada<>(999);
    public final Estadisticas estadisticas = new Estadisticas();
    private volatile boolean activo;
    private volatile boolean pausado;
    private Consumer<String> listenerLog;
    
    public boolean isActivo(){
        return activo;
    } 
    
    public void setActivo(boolean v){
        activo=v;
    } 
    
    public boolean isPausado(){
        return pausado;
    } 
    
    public void setPausado(boolean v){
        pausado=v;
    }
    
    public void setListenerLog(Consumer<String> l){
        listenerLog=l;
    } 
    
    public void log(String m){
        String x = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+" | "+m; 
        if(listenerLog!=null){
            listenerLog.accept(x);
        }
    }
    
    public void esperarSiPausado() throws InterruptedException { 
        while(pausado && activo) Thread.sleep(100); 
    
    }
    public void limpiar(){
        recepcion.limpiar();
        almacen.limpiar();
        clasificacion.limpiar();
        empaquetado.limpiar();
        expedicion.limpiar();
        reparto.limpiar();
        entregados.limpiar();
        devueltos.limpiar();
        estadisticas.limpiar();
    }
    
}
