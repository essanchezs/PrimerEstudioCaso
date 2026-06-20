package analizador;

import pila.Pila;

import java.util.ArrayList;

//Clase AnalizadorExpresiones: valida una expresion aritmetica y la convierte
//a notacion postfija usando el algoritmo de Shunting Yard (Dijkstra)
public class AnalizadorExpresiones {

    //Atributo para guardar la expresion convertida a postfija
    private String expresionPostfija;

    //Metodo principal de analisis
    public boolean analizar(String expresion) {

        expresionPostfija = "";

        if (expresion == null || expresion.trim().isEmpty()) {
            System.out.println("\nLa expresion esta vacia.");
            return false;
        }

        //Fase 1: tokenizar la expresion
        ArrayList<String> tokens = tokenizar(expresion);
        if (tokens == null) {
            return false;
        }
        if (tokens.isEmpty()) {
            System.out.println("\nLa expresion no contiene tokens validos.");
            return false;
        }

        //Fase 2: validar la secuencia de tokens
        if (!validarSecuencia(tokens)) {
            return false;
        }

        //Fase 3: aplicar Shunting Yard para obtener la expresion postfija
        return shuntingYard(tokens);
    }

    //Metodo getter de la expresion postfija
    public String getExpresionPostfija() {
        return expresionPostfija;
    }

    //Metodo de Shunting Yard: convierte la expresion infija en postfija usando una pila
    private boolean shuntingYard(ArrayList<String> tokens) {
        Pila pila = new Pila();
        StringBuilder salida = new StringBuilder();

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            //Operandos pasan directamente a la salida
            if (esNumero(token) || esVariable(token)) {
                agregarASalida(salida, token);
                continue;
            }

            //Operadores: sacar de la pila los operadores de mayor o igual prioridad
            if (esOperador(token)) {
                while (!pila.esVacia() && esOperador(pila.peek())
                        && prioridad(pila.peek()) >= prioridad(token)) {
                    agregarASalida(salida, pila.pop());
                }
                pila.push(token);
                continue;
            }

            //Parentesis de apertura: siempre se mete en la pila
            if (token.equals("(")) {
                pila.push(token);
                continue;
            }

            //Parentesis de cierre: vaciar la pila hasta el parentesis de apertura
            if (token.equals(")")) {
                while (!pila.esVacia() && !pila.peek().equals("(")) {
                    agregarASalida(salida, pila.pop());
                }
                if (pila.esVacia()) {
                    System.out.println("\nParentesis de cierre sin apertura.");
                    return false;
                }
                pila.pop(); //descartar el parentesis de apertura
            }
        }

        //Vaciar la pila al final pasando los operadores que queden a la salida
        while (!pila.esVacia()) {
            String tope = pila.pop();
            if (tope.equals("(")) {
                System.out.println("\nHay parentesis abiertos sin cerrar.");
                return false;
            }
            agregarASalida(salida, tope);
        }

        expresionPostfija = salida.toString();
        return true;
    }

    //Metodo para agregar un token a la salida postfija separado por espacios
    private void agregarASalida(StringBuilder salida, String token) {
        if (salida.length() > 0) {
            salida.append(" ");
        }
        salida.append(token);
    }

    //Metodo que devuelve la prioridad de un operador
    private int prioridad(String operador) {
        if (operador.equals("+") || operador.equals("-")) {
            return 1;
        }
        if (operador.equals("*") || operador.equals("/")) {
            return 2;
        }
        return 0;
    }

    //Metodo que valida la secuencia de tokens segun reglas de transicion
    private boolean validarSecuencia(ArrayList<String> tokens) {
        String anterior = null;

        for (int i = 0; i < tokens.size(); i++) {
            String actual = tokens.get(i);

            if (!secuenciaValida(anterior, actual)) {
                String previo = (anterior == null) ? "(inicio)" : anterior;
                System.out.println("\nSecuencia invalida: el token '" + actual + "' no puede ir despues de '" + previo + "'");
                return false;
            }
            anterior = actual;
        }

        //La expresion no puede terminar con un operador o con parentesis abierto
        if (esOperador(anterior) || anterior.equals("(")) {
            System.out.println("\nLa expresion no puede terminar con '" + anterior + "'.");
            return false;
        }

        return true;
    }

    //Metodo para dividir la expresion en tokens
    private ArrayList<String> tokenizar(String expresion) {
        ArrayList<String> tokens = new ArrayList<>();
        int i = 0;

        while (i < expresion.length()) {
            char c = expresion.charAt(i);

            //Los espacios se ignoran
            if (c == ' ') {
                i++;
                continue;
            }

            //Numero literal: secuencia de digitos
            if (esDigito(c)) {
                StringBuilder numero = new StringBuilder();
                while (i < expresion.length() && esDigito(expresion.charAt(i))) {
                    numero.append(expresion.charAt(i));
                    i++;
                }
                tokens.add(numero.toString());
                continue;
            }

            //Variable: comienza con letra, puede contener letras o digitos
            if (esLetra(c)) {
                StringBuilder variable = new StringBuilder();
                while (i < expresion.length() && (esLetra(expresion.charAt(i)) || esDigito(expresion.charAt(i)))) {
                    variable.append(expresion.charAt(i));
                    i++;
                }
                tokens.add(variable.toString());
                continue;
            }

            //Operador o parentesis: token de un solo caracter
            if (esOperador(c) || c == '(' || c == ')') {
                tokens.add(String.valueOf(c));
                i++;
                continue;
            }

            //Caracter no permitido
            System.out.println("\nCaracter no permitido en la expresion: '" + c + "'");
            return null;
        }

        return tokens;
    }

    //Metodo que valida la transicion entre el token anterior y el actual
    private boolean secuenciaValida(String anterior, String actual) {
        //Inicio: solo numero, variable o parentesis de apertura
        if (anterior == null) {
            return esNumero(actual) || esVariable(actual) || actual.equals("(");
        }
        //Despues de un operando: operador o parentesis de cierre
        if (esNumero(anterior) || esVariable(anterior)) {
            return esOperador(actual) || actual.equals(")");
        }
        //Despues de un operador: operando o parentesis de apertura
        if (esOperador(anterior)) {
            return esNumero(actual) || esVariable(actual) || actual.equals("(");
        }
        //Despues de parentesis de apertura: operando o parentesis de apertura
        if (anterior.equals("(")) {
            return esNumero(actual) || esVariable(actual) || actual.equals("(");
        }
        //Despues de parentesis de cierre: operador o parentesis de cierre
        if (anterior.equals(")")) {
            return esOperador(actual) || actual.equals(")");
        }
        return false;
    }

    //Metodo que verifica si un caracter es un digito
    private boolean esDigito(char c) {
        return c >= '0' && c <= '9';
    }

    //Metodo que verifica si un caracter es una letra
    private boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    //Metodo que verifica si un caracter es un operador aritmetico
    private boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    //Metodo que verifica si un token es un numero literal
    private boolean esNumero(String token) {
        return esDigito(token.charAt(0));
    }

    //Metodo que verifica si un token es una variable
    private boolean esVariable(String token) {
        return esLetra(token.charAt(0));
    }

    //Metodo que verifica si un token es un operador
    private boolean esOperador(String token) {
        return token.length() == 1 && esOperador(token.charAt(0));
    }
}
