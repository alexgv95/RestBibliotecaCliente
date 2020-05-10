/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import RestServices.ServicioAcceso;
import RestServices.ServicioBiblioteca;
import RestServices.ServicioRegistro;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import modelo.Biblioteca;
import modelo.Usuario;

/**
 *
 * @author Alex <agutierrezvivancos@gmail.com>
 */
public class Main {

    String inputString, respuesta;
    static String nombreFacultad, nombreCiudad, tituloLibro, autorLibro;
    int numPag;
    static String token = "";
    static boolean tokenValidado = false;
    static boolean bibliotecaExiste = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean on = true;
        BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
        ServicioRegistro sR = new ServicioRegistro();
        ServicioAcceso sA = new ServicioAcceso();
        ServicioBiblioteca sB = new ServicioBiblioteca();

        Biblioteca biblioteca = new Biblioteca();

        while (on) {
            int opcion = 0;
            String inputString, respuesta;
            String user, password;
            Usuario usuario = new Usuario();

            System.out.println("\n\n");
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
            System.out.println("| 12. Importar Biblioteca      |");
            System.out.println("| 13. Exportar Libro           |");
            System.out.println("| 14. Importar Libro           |");
            System.out.println("| 15. Validar XSD              |");
            System.out.println("| 16. Cerrar Sesión            |");
            System.out.println("|                              |");
            System.out.println("| 0.  (SALIR)                  |");
            System.out.println("<------------------------------>");

            try {
                System.out.print("\n-> Opcion: ");
                opcion = Integer.parseInt(consola.readLine());
            } catch (IOException ex) {
                System.out.println("Introduzca un numero por favor");
            }
            try {
                switch (opcion) {
                    case 0:
                        on = false;
                        System.out.println("Adios !");
                        break;
                    case 1:
                        System.out.println("\n");
                        System.out.println("SIGN UP (Registro)");
                        System.out.println("Nombre: ");
                        user = consola.readLine();
                        System.out.println("Palabra de paso: ");
                        password = consola.readLine();
                        usuario.setNombre(user);
                        usuario.setPassword(password);
                        respuesta = sR.signUp(usuario);
                        System.out.println(respuesta);
                        break;
                    case 2:
                        System.out.println("\n");
                        System.out.println("LOG IN (Acceso Usuarios)");
                        System.out.println("Nombre: ");
                        user = consola.readLine();
                        System.out.println("Palabra de paso: ");
                        password = consola.readLine();
                        usuario.setNombre(user);
                        usuario.setPassword(password);
                        respuesta = sA.acceder(usuario);
                        if (respuesta.equals("no valido")) {
                            System.out.println("Usuario o contraseña inccorectas, por favor intentelo de nuevo");
                        } else {
                            System.out.println("\nACCESS GRANTED !");
                            System.out.println("Su token es: " + respuesta);
                            token = respuesta;
                        }
                        break;
                    case 3:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        System.out.println("Introduzca el nombre de la facultad: ");
                        nombreFacultad = consola.readLine();
                        System.out.println("Introduzcae el nombre de la ciudad");
                        nombreCiudad = consola.readLine();
                        biblioteca.setFacultad(nombreFacultad);
                        biblioteca.setCiudad(nombreCiudad);
                        Biblioteca bibNueva = sB.postBiblioteca(biblioteca, Biblioteca.class, token);
                        System.out.println("\nBiblioteca creada con éxito !");
                        System.out.println(bibNueva.toString());
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Error al introducir datos por consola: " + ex);
            }
        }
    }

    private static boolean comprobarToken() {
        if (token.equals("")) {
            tokenValidado = false;
        } else {
            tokenValidado = true;
        }
        return tokenValidado;
    }

}
