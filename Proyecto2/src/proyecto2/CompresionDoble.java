/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author 2andr
 */
/**
/**
/**
 * Archivo 2: CompresionDoble.java
 * ---------------------------------------
 * Aplica doble compresion combinando:
 *   1. LZ77
 *   2. Huffman
 * 
 * Genera un log con:
 *   - Tamaños originales y comprimidos
 *   - Porcentaje de compresion
 *   - Tiempo total del proceso
 */
public class CompresionDoble {

    /**
     * Calcula el porcentaje de compresión.
     */
    private static double calcularPorcentaje(int original, int comprimido) {
        if (original == 0) return 0;
        double ratio = 100 - ((double) comprimido / original * 100);
        return Math.max(0, ratio);
    }

    /**
     * Escribe el log de resultados, sobrescribiendo cada vez.
     */
    private static void escribirLog(String texto, int original, int comprimido, double tiempoSegundos, String rutaLog) {
        double porcentaje = calcularPorcentaje(original, comprimido);
        try (FileWriter log = new FileWriter(rutaLog, false)) {
            log.write("[FECHA: " + LocalDateTime.now() + "]\n");
            log.write("Tamano original: " + original + "\n");
            log.write("Tamano comprimido: " + comprimido + "\n");
            log.write(String.format("Porcentaje de compresion: %.2f %%\n", porcentaje));
            log.write(String.format("Tiempo total: %.3f segundos\n", tiempoSegundos));
            log.write("---------------------------------------\n");
        } catch (IOException e) {
            System.err.println("Error al escribir el log: " + e.getMessage());
        }
    }

    /**
     * Convierte una cadena binaria (0s y 1s) a bytes reales.
     */
    private static byte[] convertirBinarioABytes(String bits) {
        int longitud = bits.length() / 8 + (bits.length() % 8 == 0 ? 0 : 1);
        byte[] bytes = new byte[longitud];
        for (int i = 0; i < longitud; i++) {
            int start = i * 8;
            int end = Math.min(start + 8, bits.length());
            String sub = bits.substring(start, end);
            while (sub.length() < 8) sub += "0";
            bytes[i] = (byte) Integer.parseInt(sub, 2);
        }
        return bytes;
    }

    /**
     * Aplica la doble compresión (LZ77 + Huffman)
     */
    public static void comprimir(String texto, String rutaSalida, String rutaLog) {
        long inicio = System.nanoTime();

        try {
            // Paso 1: LZ77
            List<LZ77_Huffman.Tripleta> tripletas = LZ77_Huffman.comprimirLZ77(texto, 10);
            StringBuilder sb = new StringBuilder();
            for (LZ77_Huffman.Tripleta t : tripletas) {
                sb.append(t.toString());
            }
            String salidaLZ = sb.toString();

            // Paso 2: Construir árbol de Huffman desde frecuencias
            int[] freqs = LZ77_Huffman.ArbolHuffman.frecuencias256(salidaLZ);
            LZ77_Huffman.ArbolHuffman huffman = LZ77_Huffman.ArbolHuffman.desdeFrecuencias(freqs);

            // Paso 3: Codificar
            String salidaFinal = huffman.codificar(salidaLZ);

            // Paso 4: Guardar bytes comprimidos
            byte[] bytes = convertirBinarioABytes(salidaFinal);
            try (FileOutputStream fos = new FileOutputStream(rutaSalida)) {
                fos.write(bytes);
            }

            // Paso 5: Calcular tiempo y registrar
            long fin = System.nanoTime();
            double tiempoSegundos = (fin - inicio) / 1_000_000_000.0;
            escribirLog(texto, texto.length(), bytes.length, tiempoSegundos, rutaLog);

            System.out.println("Archivo comprimido guardado en: " + rutaSalida);
            System.out.println("Log actualizado en: " + rutaLog);

        } catch (Exception e) {
            System.err.println("Error durante la compresion: " + e.getMessage());
        }
    }
}

