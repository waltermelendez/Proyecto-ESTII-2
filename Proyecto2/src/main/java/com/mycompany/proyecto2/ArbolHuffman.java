/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.mycompany.proyecto2;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author walte
 */
public class ArbolHuffman {

    Nodo Raiz;

    public ArbolHuffman(String texto) {
        this.Raiz = construirArbol(texto);
    }

    public  Map<Character, Integer> CalcularFrecuencias(String texto) {
        Map<Character, Integer> frecuencia = new HashMap<>();
        for (char i : texto.toCharArray()) {
            frecuencia.put(i, frecuencia.getOrDefault(i, 0) + 1);
        }
        return frecuencia;
    }

    private Nodo construirArbol(String texto) {
        Map<Character, Integer> frecuencia = new HashMap<>();
        frecuencia = CalcularFrecuencias(texto);

        PriorityQueue<Nodo> arbol = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> nuevo : frecuencia.entrySet()) {
            arbol.offer(new Nodo(nuevo.getKey(), nuevo.getValue()));
        }
        while (arbol.size() > 1) {
            Nodo auxiliar1 = arbol.poll();
            Nodo auxiliar2 = arbol.poll();
            Nodo padre = new Nodo(auxiliar1.getFrecuencia() + auxiliar2.getFrecuencia(), auxiliar1, auxiliar2);
            arbol.offer(padre);
        }
        return arbol.poll();
    }

    private Map<Character, String> GenerarCodigo(Nodo raiz) {
        Map<Character, String> mapa = new HashMap<>();
        CrearCodigo(raiz, "", mapa);
        return mapa;
    }

    private void CrearCodigo(Nodo raiz, String cadena, Map<Character, String> codigo) {
        if (raiz == null) {
            return;
        }

        if (raiz.getCaracter() != '\0') {
            codigo.put(raiz.getCaracter(), cadena);
        } else {
            CrearCodigo(raiz.izquierda, cadena + "0", codigo);
            CrearCodigo(raiz.derecha, cadena + "1", codigo);
        }
    }

    public String Codificar(String texto) {
        Map<Character, String> codigo = new HashMap<>();

        codigo = GenerarCodigo(Raiz);
        StringBuilder write = new StringBuilder();

        for (char c : texto.toCharArray()) {
            write.append(codigo.get(c));
        }
        return write.toString();
    }

    public void Imprimir(Nodo Raiz,String texto) {
        String resultado=Codificar(texto);
       // System.out.println("El texto codificado es:"+resultado);
        if (Raiz == null) {
            return;
        }
        if (Raiz.getCaracter() != '\0') {
            //Nodo con el caratcer y su frecuencia
            System.out.println( Raiz.getCaracter() + "'=>(" + Raiz.getFrecuencia() + ")");
        } else {
            //Nodo con la frecuencia
            System.out.println( "'->(" + Raiz.getFrecuencia() + ")");
        }
        Imprimir(Raiz.izquierda, resultado + "     ");
        Imprimir(Raiz.derecha, resultado + "     ");

    }

}
