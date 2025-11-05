/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 *
 * @author 2andr
 */
/**

/**
 * Archivo 3: Descompresion.java
 * ------------------------------
 * Lee el archivo comprimido (.cmp) en formato binario,
 * convierte los bytes de nuevo a la secuencia binaria original,
 * decodifica Huffman y luego aplica la descompresion LZ77.
 */
public class Descompresion {

    public static void descomprimir(String rutaEntrada, String rutaSalida) {
        long inicio = System.nanoTime();

        try {
            // Paso 1: leer archivo comprimido
            FileInputStream fis = new FileInputStream(rutaEntrada);
            byte[] bytes = fis.readAllBytes();
            fis.close();

            // Paso 2: convertir bytes a bits
            StringBuilder binario = new StringBuilder();
            for (byte b : bytes) {
                String bits = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
                binario.append(bits);
            }

            // Paso 3: usar el árbol guardado en la sesión
            LZ77_Huffman.ArbolHuffman huffman = LZ77_Huffman.ArbolHuffman.ultimoArbol;
            if (huffman == null) {
                throw new IllegalStateException("No hay arbol Huffman disponible. Ejecute la compresion antes de descomprimir.");
            }

            // Paso 4: decodificar Huffman
            String textoLZ77 = huffman.decodificar(binario.toString());

            // Paso 5: aplicar LZ77 inverso
            List<LZ77_Huffman.Tripleta> tripletas = LZ77_Huffman.stringATripletas(textoLZ77);
            String textoFinal = LZ77_Huffman.descomprimirLZ77(tripletas);

            // Paso 6: guardar resultado
            try (FileWriter writer = new FileWriter(rutaSalida)) {
                writer.write(textoFinal);
            }

            long fin = System.nanoTime();
            double tiempoSegundos = (fin - inicio) / 1_000_000_000.0;

            System.out.println("Archivo restaurado guardado en: " + rutaSalida);
            System.out.println(String.format("Tiempo de descompresion: %.3f segundos", tiempoSegundos));

        } catch (IOException e) {
            System.err.println("Error al leer o escribir archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error durante la descompresion: " + e.getMessage());
        }
    }
}