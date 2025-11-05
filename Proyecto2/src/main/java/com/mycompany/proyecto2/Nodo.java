/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.mycompany.proyecto2;

/**
 *
 * @author walte
 */
public class Nodo implements Comparable<Nodo> {

    private  char caracter;
    private  int frecuencia;
    public Nodo izquierda;
    public Nodo derecha;

    public Nodo(char caracter, int frecuencia) {
        this.caracter = caracter;
        this.frecuencia = frecuencia;
    }

    public Nodo(int frecuencia, Nodo izquierda, Nodo derecha) {
        this.caracter = '\0';
        this.frecuencia = frecuencia;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public int compareTo(Nodo o) {
        return this.frecuencia - o.frecuencia;
    }

    public char getCaracter() {
        return caracter;
    }

    public void setCaracter(char caracter) {
        this.caracter = caracter;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }
    
    

    
}
