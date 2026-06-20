import analizador.AnalizadorExpresiones;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AnalizadorExpresiones analizador = new AnalizadorExpresiones();

        //Mostrar menu principal
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== Analizador de Expresiones Aritmeticas ===");
            System.out.println("1. Ingresar y validar una expresion");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    validarExpresion(scanner, analizador);
                    break;
                case 0:
                    System.out.println("Hasta luego!");
                    break;
                default:
                    System.out.println("Opcion invalida, intente de nuevo.");
            }
        }

        scanner.close();
    }

    //Metodo para que el usuario ingrese una expresion y se valide
    private static void validarExpresion(Scanner scanner, AnalizadorExpresiones analizador) {
        System.out.print("\nIngrese la expresion aritmetica: ");
        String expresion = scanner.nextLine();

        boolean valida = analizador.analizar(expresion);

        if (valida) {
            System.out.println("\nResultado: la expresion '" + expresion + "' es VALIDA.");
            System.out.println("Expresion en notacion postfija: " + analizador.getExpresionPostfija());
        } else {
            System.out.println("Resultado: la expresion '" + expresion + "' es INVALIDA.");
        }
    }
}
