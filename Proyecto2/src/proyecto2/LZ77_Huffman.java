/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2;
import java.util.*;

/**
 *
 * @author 2andr
 */
/**
 * Archivo: LZ77_Huffman.java
 * --------------------------
 * Contiene:
 * - Implementacion de LZ77 (compresion y descompresion)
 * - Implementacion de Huffman (codificar y decodificar)
 * Ambas integradas para su uso en CompresionDoble y Descompresion.
 */
public class LZ77_Huffman {

    // ================================
    //   CLASE INTERNA: TRIPLETA
    // ================================
    public static class Tripleta {
        public final int offset;
        public final int length;
        public final char next;

        public Tripleta(int offset, int length, char next) {
            this.offset = offset;
            this.length = length;
            this.next = next;
        }

        @Override
        public String toString() {
            return "(" + offset + "," + length + "," + next + ")";
        }
    }

    // ================================
    //   LZ77 - COMPRESION
    // ================================
    public static List<Tripleta> comprimirLZ77(String texto, int ventana) {
        List<Tripleta> resultado = new ArrayList<>();
        int i = 0;

        while (i < texto.length()) {
            int mejorLong = 0;
            int mejorDist = 0;

            int inicioVentana = Math.max(0, i - ventana);

            for (int j = inicioVentana; j < i; j++) {
                int k = 0;
                while (i + k < texto.length() && texto.charAt(j + k) == texto.charAt(i + k)) {
                    k++;
                    if (j + k >= i) break;
                }
                if (k > mejorLong) {
                    mejorLong = k;
                    mejorDist = i - j;
                }
            }

            char siguiente = (i + mejorLong < texto.length()) ? texto.charAt(i + mejorLong) : '\0';
            resultado.add(new Tripleta(mejorDist, mejorLong, siguiente));
            i += mejorLong + 1;
        }

        return resultado;
    }

    // ================================
    //   LZ77 - DESCOMPRESION
    // ================================
    public static String descomprimirLZ77(List<Tripleta> tripletas) {
        StringBuilder salida = new StringBuilder();

        for (Tripleta t : tripletas) {
            if (t.offset > 0 && t.length > 0) {
                int inicio = salida.length() - t.offset;
                for (int k = 0; k < t.length; k++) {
                    salida.append(salida.charAt(inicio + k));
                }
            }
            if (t.next != '\0') {
                salida.append(t.next);
            }
        }
        return salida.toString();
    }

    // ================================
    //   Convertir String ↔ Tripletas
    // ================================
    public static List<Tripleta> stringATripletas(String texto) {
        List<Tripleta> lista = new ArrayList<>();
        int i = 0;
        while (i < texto.length()) {
            if (texto.charAt(i) == '(') {
                int cierre = texto.indexOf(')', i);
                if (cierre > i) {
                    String contenido = texto.substring(i + 1, cierre);
                    String[] partes = contenido.split(",");
                    if (partes.length == 3) {
                        try {
                            int off = Integer.parseInt(partes[0].trim());
                            int len = Integer.parseInt(partes[1].trim());
                            char next = partes[2].trim().charAt(0);
                            lista.add(new Tripleta(off, len, next));
                        } catch (Exception e) {
                            // ignorar errores de formato
                        }
                    }
                    i = cierre + 1;
                    continue;
                }
            }
            i++;
        }
        return lista;
    }

    // ================================
    //   CLASE INTERNA: ARBOL HUFFMAN
    // ================================
    public static class ArbolHuffman {
        
        // Guarda el último árbol usado durante la compresión
        public static ArbolHuffman ultimoArbol;

        // ---------- Nodo ----------
        static class Nodo implements Comparable<Nodo> {
            char caracter;
            int frecuencia;
            Nodo izquierda;
            Nodo derecha;

            Nodo(char c, int f) {
                this.caracter = c;
                this.frecuencia = f;
            }

            Nodo(Nodo izq, Nodo der) {
                this.caracter = '\0';
                this.frecuencia = izq.frecuencia + der.frecuencia;
                this.izquierda = izq;
                this.derecha = der;
            }

            boolean esHoja() {
                return izquierda == null && derecha == null;
            }

            @Override
            public int compareTo(Nodo o) {
                return Integer.compare(this.frecuencia, o.frecuencia);
            }
        }

         // ---------- Atributos ----------
        private final Nodo raiz;
        private final Map<Character, String> codigos;
        
        public ArbolHuffman() {
        this.raiz = null;
        this.codigos = new HashMap<>();
        }

        private ArbolHuffman(Nodo raiz, Map<Character, String> codigos) {
            this.raiz = raiz;
            this.codigos = codigos;
        }

        // ---------- Crear desde frecuencias ----------
        public static ArbolHuffman desdeFrecuencias(int[] frecuencias) {
            PriorityQueue<Nodo> cola = new PriorityQueue<>();
            for (int i = 0; i < 256; i++) {
                if (frecuencias[i] > 0) {
                    cola.add(new Nodo((char) i, frecuencias[i]));
                }
            }

            if (cola.isEmpty()) {
                cola.add(new Nodo((char) 0, 1));
            }

            while (cola.size() > 1) {
                Nodo a = cola.poll();
                Nodo b = cola.poll();
                cola.add(new Nodo(a, b));
            }

            Nodo raiz = cola.poll();
            Map<Character, String> mapa = new HashMap<>();
            construirCodigos(raiz, "", mapa);
            ArbolHuffman nuevo = new ArbolHuffman(raiz, mapa);
            ultimoArbol = nuevo; 
            //Guardamos este árbol para usarlo después
            return nuevo;
            
        }

        // ---------- Calcular frecuencias ----------
        public static int[] frecuencias256(String texto) {
            int[] freqs = new int[256];
            for (int i = 0; i < texto.length(); i++) {
                freqs[texto.charAt(i) & 0xFF]++;
            }
            return freqs;
        }

        // ---------- Construir mapa de codigos ----------
        private static void construirCodigos(Nodo nodo, String prefijo, Map<Character, String> mapa) {
            if (nodo == null) return;

            if (nodo.esHoja()) {
                mapa.put(nodo.caracter, prefijo.length() > 0 ? prefijo : "0");
            } else {
                construirCodigos(nodo.izquierda, prefijo + "0", mapa);
                construirCodigos(nodo.derecha, prefijo + "1", mapa);
            }
        }

        // ---------- Codificar ----------
        public String codificar(String texto) {
            StringBuilder binario = new StringBuilder();
            for (int i = 0; i < texto.length(); i++) {
                binario.append(codigos.get(texto.charAt(i)));
            }
            return binario.toString();
        }

        // ---------- Decodificar ----------
        public String decodificar(String bits) {
            StringBuilder salida = new StringBuilder();
            Nodo actual = raiz;

            for (int i = 0; i < bits.length(); i++) {
                actual = (bits.charAt(i) == '0') ? actual.izquierda : actual.derecha;

                if (actual.esHoja()) {
                    salida.append(actual.caracter);
                    actual = raiz;
                }
            }

            return salida.toString();
        }
    }
}