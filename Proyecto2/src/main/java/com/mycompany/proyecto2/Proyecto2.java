/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyecto2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author walte
 */
public class Proyecto2 {

    
    

    //Codificacion sin huffman---Varios archivos
    public static void LZWCompress(List<File> archivosEntrada, File archivoSalida, int maxDiccionario) {
        List<Integer> resultado = new ArrayList<>();

        for (File archivoEntrada : archivosEntrada) {
            try (BufferedReader leer = new BufferedReader(new FileReader(archivoEntrada))) {
                StringBuilder sb = new StringBuilder();
                String linea;

                while ((linea = leer.readLine()) != null) {
                    sb.append(linea).append("\n");
                }

                String cadena = sb.toString();

                // Inicializar diccionario
                Map<String, Integer> tabla = new HashMap<>();
                int count = 256;
                for (int i = 0; i < count; i++) {
                    tabla.put("" + (char) i, i);
                }

                String W = "";
                for (char C : cadena.toCharArray()) {
                    String WC = W + C;
                    if (tabla.containsKey(WC)) {
                        W = WC;
                    } else {
                        resultado.add(tabla.get(W));
                        if (count < maxDiccionario) {
                            tabla.put(WC, count++);
                        }
                        W = Character.toString(C);
                    }
                }

                if (!W.isEmpty()) {
                    resultado.add(tabla.get(W));
                }

                // Agregar separador entre archivos (-1)
                resultado.add(-1);

                System.out.println("Archivo comprimido: " + archivoEntrada.getName());

            } catch (IOException e) {
                System.err.println("Error al leer " + archivoEntrada.getName() + ": " + e.getMessage());
            }
        }

        // Guardar todos los resultados en un solo archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {
            for (int i = 0; i < resultado.size(); i++) {
                writer.write(resultado.get(i).toString());
                if (i < resultado.size() - 1) writer.write(" ");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de salida: " + e.getMessage());
        }

        System.out.println(" Compresión completada. Archivo guardado en: " + archivoSalida.getName());
    }

    //Decodificacion sin huffman---Varios archivos
    public static void LZWdecompress(File archivoEntrada, int maxDiccionario, String Salida) {
        List<Integer> codigos = new ArrayList<>();

        // Leer los códigos comprimidos del archivo
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                for (String num : linea.trim().split("\\s+")) {
                    if (!num.isEmpty()) {
                        codigos.add(Integer.parseInt(num));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        if (codigos.isEmpty()) {
            throw new IllegalArgumentException("El archivo no contiene datos válidos.");
        }

        // --- Variables de control ---
        int archivoIndex = 1; // contador de archivos
        List<Integer> bloqueActual = new ArrayList<>();

        for (int i = 0; i < codigos.size(); i++) {
            int valor = codigos.get(i);

            if (valor == -1) {
                // Descomprimir bloque acumulado
                if (!bloqueActual.isEmpty()) {
                    String texto = descomprimirBloque(bloqueActual, maxDiccionario);

                    // Guardar archivo
                    File salida = new File( Salida+"salida_" + archivoIndex + ".txt");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(salida))) {
                        writer.write(texto);
                    } catch (IOException e) {
                        System.err.println("Error al guardar " + salida.getName());
                    }

                    System.out.println("Archivo descomprimido: " + salida.getName());
                    archivoIndex++;
                    bloqueActual.clear();
                }
            } else {
                bloqueActual.add(valor);
            }
        }

        // Último bloque (si no termina con -1)
        if (!bloqueActual.isEmpty()) {
            String texto = descomprimirBloque(bloqueActual, maxDiccionario);
            File salida = new File( Salida+"salida_" + archivoIndex + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(salida))) {
                writer.write(texto);
            } catch (IOException e) {
                System.err.println("Error al guardar " + salida.getName());
            }
            System.out.println("Archivo descomprimido: " + salida.getName());
        }

        System.out.println("Descompresión múltiple completada.");
    }

    // --- Método auxiliar: descomprime un bloque individual ---
    private static String descomprimirBloque(List<Integer> codigos, int maxDiccionario) {
        Map<Integer, String> tabla = new HashMap<>();
        int count = 256;
        for (int i = 0; i < count; i++) {
            tabla.put(i, "" + (char) i);
        }

        int primero = codigos.get(0);
        String W = tabla.get(primero);
        StringBuilder sb = new StringBuilder(W);

        for (int j = 1; j < codigos.size(); j++) {
            int k = codigos.get(j);
            String entrada;

            if (tabla.containsKey(k)) {
                entrada = tabla.get(k);
            } else if (k == count) {
                entrada = W + W.charAt(0);
            } else {
                throw new IllegalArgumentException("Código inválido: " + k);
            }

            sb.append(entrada);

            if (count < maxDiccionario) {
                tabla.put(count++, W + entrada.charAt(0));
            }

            W = entrada;
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        System.out.println("Hello World!");
        List<File> archivo = new ArrayList<>();
        File nuevo = new File("C:\\Users\\walte\\Desktop\\long\\bigfiles\\prueba_lzw.txt");
        archivo.add(nuevo);
        File nuevo2 = new File("C:\\Users\\walte\\Desktop\\long\\bigfiles\\1bigfile.txt");
        File nuevo3 = new File("C:\\Users\\walte\\Desktop\\long\\bigfiles\\bigfile-rep.txt");
        archivo.add(nuevo2);
        archivo.add(nuevo3);
        String ruta = "C:\\Users\\walte\\Desktop\\long\\";
        File name =  new File("C:\\Users\\walte\\Desktop\\long\\N.txt");
        if (!nuevo.isDirectory() || !nuevo.exists()) {
            LZWCompress(archivo,name,4096);
        }
        File lzw = new File("C:\\Users\\walte\\Desktop\\long\\N.txt");
        if (!lzw.isDirectory() || !nuevo.exists()) {
            LZWdecompress(name, 4096, ruta);
        }

        //
    }
}
