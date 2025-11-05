/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
/**
 *
 * @author 2andr
 *
 * Archivo principal: Proyecto2.java
 * -----------------------------------------
 * Muestra un menú en consola para controlar:
 *   1. Compresión múltiple (LZ77 + Huffman)
 *   2. Descompresión múltiple
 *   3. Ver log del proceso
 *   4. Salir del programa
 */
public class Proyecto2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Rutas base
        String carpetaEntrada = System.getProperty("user.dir") + File.separator + "archivosEntrada";
        String carpetaSalida  = System.getProperty("user.dir") + File.separator + "archivosSalida";

        // Asegurar carpetas
        new File(carpetaEntrada).mkdirs();
        new File(carpetaSalida).mkdirs();

        int opcion = 0;

        while (opcion != 4) {
            System.out.println("\n=============================");
            System.out.println("  SISTEMA DE COMPRESION DOBLE");
            System.out.println("=============================");
            System.out.println("1. Comprimir archivo (LZ77 + Huffman)");
            System.out.println("2. Descomprimir archivo resultado.cmp");
            System.out.println("3. Ver log de procesos");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Opcion invalida. Intente de nuevo.");
                continue;
            }

            switch (opcion) {
                case 1 -> {
                    System.out.println("\nIniciando compresion...");
                    String rutaEntrada = carpetaEntrada + File.separator + "texto1.txt";
                    String rutaSalida  = carpetaSalida  + File.separator + "resultado.cmp";
                    String rutaLog     = carpetaSalida  + File.separator + "log.txt";

                    try {
                        String texto = Files.readString(Paths.get(rutaEntrada));
                        // AHORA se pasan 3 parametros: texto, salida y log
                        CompresionDoble.comprimir(texto, rutaSalida, rutaLog);
                    } catch (IOException e) {
                        System.err.println("Error al leer el archivo de entrada: " + e.getMessage());
                    }
                }

                case 2 -> {
                    System.out.println("\nIniciando descompresion...");
                    String rutaEntrada = carpetaSalida + File.separator + "resultado.cmp";
                    String rutaSalida  = carpetaSalida + File.separator + "texto_restaurado.txt";

                    File cmp = new File(rutaEntrada);
                    if (!cmp.exists()) {
                        System.out.println("No se encontro el archivo resultado.cmp. Ejecute primero la compresion.");
                    } else {
                        Descompresion.descomprimir(rutaEntrada, rutaSalida);
                    }
                }

                case 3 -> {
                    System.out.println("\nMostrando contenido del log.txt:\n");
                    String rutaLog = carpetaSalida + File.separator + "log.txt";
                    File logFile = new File(rutaLog);

                    if (!logFile.exists()) {
                        System.out.println("No hay log disponible todavia.");
                    } else {
                        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
                            String linea;
                            while ((linea = br.readLine()) != null) {
                                System.out.println(linea);
                            }
                        } catch (IOException e) {
                            System.err.println("Error al leer el log: " + e.getMessage());
                        }
                    }
                }

                case 4 -> {
                    System.out.println("\nSaliendo del sistema...");
                }

                default -> {
                    System.out.println("Opcion invalida. Intente de nuevo.");
                }
            }
        }

        sc.close();
    }
}