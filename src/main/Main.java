/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import RestServices.ServicioAcceso;
import RestServices.ServicioBiblioteca;
import RestServices.ServicioDesconexion;
import RestServices.ServicioRegistro;
import RestServices.ServicioValidador;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Biblioteca;
import modelo.Libro;
import modelo.ListaLibros;
import modelo.Usuario;

/**
 *
 * @author Alex <agutierrezvivancos@gmail.com>
 */
public class Main {

    static String nombreFichero, respuesta, numLibro, contenidoFichero;
    static String nombreFacultad, nombreCiudad, tituloLibro, autorLibro;
    static int numPag;
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
        ServicioValidador sV = new ServicioValidador();
        ServicioDesconexion sD = new ServicioDesconexion();
//        ServicioAduanas sAd = new ServicioAduanas();

        Biblioteca biblioteca = new Biblioteca();
        Libro libro = new Libro();
        ListaLibros listaLibros = new ListaLibros();
        File file = null;

        while (on) {
            int opcion = 0;
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
            System.out.println("| 8.  Mostrar Libros           |");
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

                    case 4:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (comprobarBiblioteca(biblioteca)) {
                            System.out.println("Biblioteca obtenida: " + biblioteca.getFacultad());
                        } else {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        break;

                    case 5:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (comprobarBiblioteca(biblioteca)) {
                            respuesta = sB.getLibrosTexto(token);
                            System.out.println("\n");
                            System.out.println(respuesta);
                        } else {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        break;

                    case 6:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }

                        System.out.println("\nFormulario de creación de libro:");
                        System.out.println("Titulo del libro: ");
                        tituloLibro = consola.readLine();
                        System.out.println("Nombre del autor: ");
                        autorLibro = consola.readLine();
                        System.out.println("Número de páginas: ");
                        numPag = Integer.parseInt(consola.readLine());

                        libro.setTitulo(tituloLibro);
                        libro.setAutor(autorLibro);
                        libro.setNumPag(numPag);

                        Libro libroNuevo = sB.postLibro(libro, Libro.class, token);
                        System.out.println("Libro creado con exito !");
                        System.out.println(libroNuevo);
                        break;

                    case 7:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        List listaLibrosL = biblioteca.getLibros();
                        if (listaLibrosL.isEmpty()) {
                            System.out.println("No hay libros en la biblioteca");
                            break;
                        }
                        listaLibros = sB.getLibros(ListaLibros.class, token);
                        System.out.println(listaLibros);
                        System.out.println("Introduce el [Numero] del libro a obtener: ");
                        numLibro = consola.readLine();
                        libro = sB.getLibro(Libro.class, numLibro, token);
                        System.out.println("\nSe ha devuelto el libro: " + libro.getTitulo());
                        break;

                    case 8:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        listaLibros = (ListaLibros) sB.getLibros(ListaLibros.class, token);
                        System.out.println(listaLibros);
                        break;

                    case 9:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        List listaLibros3 = biblioteca.getLibros();
                        if (listaLibros3.isEmpty()) {
                            System.out.println("No hay libros en la biblioteca");
                            break;
                        }
                        listaLibros = (ListaLibros) sB.getLibros(ListaLibros.class, token);
                        System.out.println(listaLibros);
                        System.out.println("Introduce el [Numero] del libro a eliminar: ");
                        numLibro = consola.readLine();
                        biblioteca = sB.deleteLibro(Biblioteca.class, numLibro, token);
                        System.out.println("\nSe ha borrado con exito !");
                        System.out.println(biblioteca);
                        break;

                    case 10:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        List listaLibros5 = biblioteca.getLibros();
                        if (listaLibros5.isEmpty()) {
                            System.out.println("No hay libros en la biblioteca");
                            break;
                        }
                        ListaLibros listaLibros6 = (ListaLibros) sB.getLibros(ListaLibros.class, token);
                        System.out.println(listaLibros6);
                        System.out.println("Introduce el [Numero] del libro a modificar: ");
                        numLibro = consola.readLine();

                        System.out.println("\nFormulario de modificación de libro:");
                        System.out.println("Titulo del libro: ");
                        tituloLibro = consola.readLine();
                        System.out.println("Nombre del autor: ");
                        autorLibro = consola.readLine();
                        System.out.println("Número de páginas: ");
                        numPag = Integer.parseInt(consola.readLine());

                        libro.setTitulo(tituloLibro);
                        libro.setAutor(autorLibro);
                        libro.setNumPag(numPag);

                        Libro libroNuevo2 = sB.putLibro(libro, Libro.class, numLibro, token);
                        System.out.println("\nLibro: " + libroNuevo2.getTitulo() + " modificado con exito");
                        System.out.println("\n\t" + libroNuevo2);
                        break;

                    case 11:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        System.out.println("Introduzca el nombre del fichero donde va a ser exportada la biblioteca actual");
                        nombreFichero = consola.readLine();
                        respuesta = sB.exportarBiblioteca(nombreFichero, token);
                        descifrarString(respuesta, nombreFichero);
                        System.out.println("La biblioteca " + biblioteca.getFacultad() + ""
                                + " ha sido exportada al fichero " + nombreFichero
                                + " con exito !");
                        break;

                    case 12:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        System.out.println("Se va a sobreescribir la biblioteca actual, ¿Seguro que quiere continuar? [s/n]");
                        respuesta = consola.readLine();
                        if (respuesta.equalsIgnoreCase("n")) {
                            break;
                        }
                        System.out.println("Introduzca el nombre del fichero XML (sin .xml)");
                        nombreFichero = consola.readLine();
                        file = new File("xml/" + nombreFichero + ".xml");
                        contenidoFichero = codificadorString(file);
                        biblioteca = sB.importarBiblioteca(Biblioteca.class, nombreFichero, contenidoFichero, token);
                        System.out.println("La biblioteca ha sido importada con exito !");
                        System.out.println(biblioteca);
                        break;

                    case 13:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        listaLibros = (ListaLibros) sB.getLibros(ListaLibros.class, token);
                        if (listaLibros == null) {
                            System.out.println("No hay libros en la biblioteca");
                            break;
                        }
                        System.out.println(listaLibros);
                        System.out.println("Introduce el [Numero] del libro a exportar: ");
                        numLibro = consola.readLine();
                        System.out.println("Introduzca el nombre del fichero donde va a ser el libro elegido");
                        nombreFichero = consola.readLine();
                        respuesta = sB.exportarLibro(numLibro, nombreFichero, token);
                        descifrarString(respuesta, nombreFichero);
                        libro = sB.getLibro(Libro.class, numLibro, token);
                        System.out.println("El libro " + libro.getTitulo() + ""
                                + " ha sido exportado al fichero " + nombreFichero
                                + " con exito !");
                        break;

                    case 14:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        System.out.println("Introduzca el nombre del fichero XML (sin .xml)");
                        nombreFichero = consola.readLine();
                        file = new File("xml/" + nombreFichero + ".xml");
                        contenidoFichero = codificadorString(file);
                        respuesta = sB.importarLibro(nombreFichero, contenidoFichero, token);
                        System.out.println("\n");
                        System.out.println(respuesta);
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        System.out.println(biblioteca);
                        break;

                    case 15:
                        System.out.println("\nHerramienta de validacion XSD");
                        System.out.println("Introduzca el fichero a comprobar (sin .xml)");
                        System.out.println("Consejo: Hay un fichero preparado llamado bibliotecaXSD y otro bibliotecaXSDMal ");
                        nombreFichero = consola.readLine();
                        file = new File("xml/" + nombreFichero + ".xml");
                        String contenidoFicheroXSD = codificadorStringXSD(file);
                        respuesta = sV.postValidarXSD(contenidoFicheroXSD);
                        System.out.println(respuesta);
                        System.out.println("\n\n");
                        break;

                    case 16:
                        if (!comprobarToken()) {
                            System.out.println("No se ha encontrado ningún token, "
                                    + "por favor inicie sesión");
                            break;
                        }
                        biblioteca = sB.getBiblioteca(Biblioteca.class, token);
                        if (!comprobarBiblioteca(biblioteca)) {
                            System.out.println("No se ha encontrado ninguna biblioteca relacionada con este usuario");
                            break;
                        }
                        respuesta = sD.cerrarSesion(token);
                        token = "";
                        System.out.println(respuesta);
                        System.out.println("Se ha cerrado su sesión !");
                        break;

                }
            } catch (IOException ex) {
                System.out.println("Error al introducir datos por consola: " + ex);
            }
        }
    }

    private static boolean comprobarToken() {
        tokenValidado = !token.equals("");
        return tokenValidado;
    }

    private static boolean comprobarBiblioteca(Biblioteca biblioteca) {
        bibliotecaExiste = !biblioteca.getFacultad().equals("");
        return bibliotecaExiste;
    }

    private static String codificadorString(File file) {
        String contenidoFile = "";
        String STOP = "#";
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                contenidoFile = contenidoFile + line + STOP;
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contenidoFile;
    }

    private static String codificadorStringXSD(File file) {
        String contenidoFile = "";
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                contenidoFile = contenidoFile + line;
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contenidoFile;
    }

    private static void descifrarString(String respuesta, String nombreFichero) {
        String newRespuesta = respuesta.replaceAll("#", "\n");
        System.out.println(newRespuesta);
        BufferedWriter bw = null;
        try {
            File fichero = new File("xml/" + nombreFichero + ".xml");
            bw = new BufferedWriter(new FileWriter(fichero));
            bw.write(newRespuesta);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
