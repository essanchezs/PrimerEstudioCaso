package pila;

public class Nodo {

    //Atributos
    private String dato;
    private Nodo siguiente;

    //Metodo constructor
    public Nodo(String dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    //Metodo getter
    public String getDato() {
        return dato;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    //Metodo setter
    public void setDato(String dato) {
        this.dato = dato;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    //Metodo to String
    public String toString() {
        return dato;
    }
}
