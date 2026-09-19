
package estructuras;

import java.util.function.Predicate;

public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private int tamanio;
    private final int capacidad;
    
    public ListaEnlazada(int capacidad){
        this.capacidad = capacidad;
    }
    
    public synchronized void agregar(T dato) throws InterruptedException{
        while( tamanio >= capacidad){
            wait();
        }
        Nodo<T> nuevo = new Nodo<> (dato);
        if(cabeza == null){
            cabeza = nuevo;
        }
        else{
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null){
                actual = actual.getSiguiente();
                actual.setSiguiente(nuevo);
            }
            tamanio ++ ;
            notifyAll();
        }
    }
        
        public synchronized T extraerPrimero() throws InterruptedException{
            while(tamanio == 0){
                wait();
            }
            T dato = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
            tamanio--;
            notifyAll();
            return dato;
        }
        
        public synchronized T extraerMejor(Predicate<T> filtro) throws InterruptedException{
            while(tamanio == 0){
                wait();
            }
            Nodo<T> actual = cabeza;
            Nodo<T> anterior = null;
            Nodo<T> mejor = null;
            Nodo<T> anteriorMejor = null;
            
            while(actual != null){
                if(filtro.test(actual.getDato()) && (mejor == null || prioridad(actual.getDato()) < prioridad(mejor.getDato()))){
                    mejor = actual;
                    anteriorMejor = anterior; 
                }
                anterior = actual;
                actual = actual.getSiguiente();
            }
            if(mejor == null){
                return extraerPrimero();
            }
            if(anteriorMejor == null){
                cabeza = mejor.getSiguiente();
            }
            else{
                anteriorMejor.setSiguiente(mejor.getSiguiente());
            }
            tamanio--;
            notifyAll();
            return mejor.getDato();
        }
        
        private int prioridad(T dato){
            try{
                return ((modelo.Paquete) dato).getPrioridad().ordinal();
            } catch (Exception e){
                return 99;
            }
        }
        
        public synchronized T obtener(int indice){
            if(indice < 0 || indice >= tamanio){
                return null; 
            }
            Nodo<T> actual = cabeza;
                 for (int i = 0; i < indice; i++) {
                     actual = actual.getSiguiente();
                 }
                 return actual.getDato();
        }
        
        public synchronized T buscar(Predicate<T> criterio){
            Nodo<T> a =cabeza;
            while(a != null){
                if(criterio.test(a.getDato())){
                    return a.getDato();
                }
                a = a.getSiguiente();
            }
            return null;
        }
        
        public synchronized boolean eliminar(T dato){
            Nodo<T> a =cabeza;
            Nodo<T> ant = null;
            while(a != null){
                if(a.getDato() == dato || a.getDato().equals(dato)){
                    if(ant == null){
                        cabeza = a.getSiguiente();
                    }
                    else{
                        ant.setSiguiente(a.getSiguiente());
                    }
                    tamanio--;
                    notifyAll();
                    return true;
                }
                ant = a;
                a = a.getSiguiente();
            }
            return false;
        }
        
        public synchronized int tamanio(){
            return tamanio;
        }
        
        public synchronized boolean estaVacia(){
            return tamanio == 0;
        }
        
        public synchronized int getCapacidad(){
            return capacidad;
        }
        
        public synchronized String[] aArregloTexto(){
            String[] r = new String[tamanio];
            Nodo<T> a = cabeza;
            int i= 0;
            while(a != null){
                r[i++] = String.valueOf(a.getDato());
                a = a.getSiguiente();
            }
            return r;
        }
        
        public synchronized void limpiar(){
            cabeza = null;
            tamanio = 0;
            notifyAll();
        }    
    
    
}
