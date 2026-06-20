package pila;

//Clase Pila: pila enlazada de tokens
public class Pila {

    //Atributo
    private Nodo top;

    //Metodo constructor
    public Pila() {
        top = null;
    }

    //Metodo estaVacia
    private boolean estaVacia() {
        return top == null;
    }

    //Metodo push: inserta un token en la cima
    public void push(String dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.setSiguiente(top);
        top = nuevo;
    }

    //Metodo pop: extrae el token de la cima
    public String pop() {
        if (estaVacia()) {
            return null;
        }
        String dato = top.getDato();
        top = top.getSiguiente();
        return dato;
    }

    //Metodo peek: consulta el token de la cima sin extraerlo
    public String peek() {
        if (estaVacia()) {
            return null;
        }
        return top.getDato();
    }

    //Metodo publico para consultar si la pila quedo vacia
    public boolean esVacia() {
        return estaVacia();
    }
}
