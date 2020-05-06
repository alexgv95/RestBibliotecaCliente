/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import modelo.Usuario;

/**
 *
 * @author Alex <agutierrezvivancos@gmail.com>
 */
public class Main {

    String inputString, respuesta;
    String tituloLibro, autorLibro;
    int numPag;
    String token = "";
    boolean tokenValidado = false;
    boolean bibliotecaExiste = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean estado = false;
        BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

        while (!estado) {
            int opcion = 0;
            String inputString, respuesta;
            String user, password;

            System.out.println("<----MENU CLIENTE BIBLIOTECA--->");
            System.out.println("| 1.  Registrarse              |");
            System.out.println("| 2.  Iniciar Sesión           |");
            System.out.println("| 3.  Crear Biblioteca         |");
            System.out.println("| 4.  Obtener Biblioteca       |");
            System.out.println("| 5.  Mostrar Biblioteca       |");
            System.out.println("| 6.  Añadir Libro             |");
            System.out.println("| 7.  Obtener Libro            |");
            System.out.println("| 8.  Mostrar Libro            |");
            System.out.println("| 9.  Borrar Libro             |");
            System.out.println("| 10. Modificar Libro          |");
            System.out.println("| 11. Exportar Biblioteca      |");
            System.out.println("| 12. Importar Biblitoeca      |");
            System.out.println("| 13. Exportar Libro           |");
            System.out.println("| 14. Importar Libro           |");
            System.out.println("| 15. Validar XSD              |");
            System.out.println("| 16. Cerrar Sesión            |");
            System.out.println("|                              |");
            System.out.println("| 0.  (SALIR)                  |");
            System.out.println("<------------------------------>");

            try {
                opcion = Integer.parseInt(consola.readLine());
            } catch (IOException ex) {
                System.out.println("Introduzca un numero por favor");
            }
            try {
                switch (opcion) {
                    case 1:
                        System.out.println("SIGN UP");
                        System.out.println("Nombre: ");
                        user = consola.readLine();
                        password = consola.readLine();
                        Usuario usuario = new Usuario();
                        usuario.setNombre(user);
                        usuario.setPassword(password);
                        //respuesta = 
                }
            } catch (IOException ex) {
                System.out.println("Error al introducir datos por consola: " + ex);
            }
        }
    }

}
