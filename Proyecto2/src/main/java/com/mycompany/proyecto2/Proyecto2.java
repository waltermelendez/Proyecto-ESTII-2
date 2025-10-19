/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyecto2;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author walte
 */
public class Proyecto2 {

    public static void LZW(List<File> Archivos, int N) {
        List<Integer> resultado = new ArrayList<>();
        try {
            for (File entrada : Archivos) {
                StringBuilder sb = new StringBuilder();
                BufferedReader leer = new BufferedReader(new FileReader(entrada));
                String lectura;
                while ((lectura = leer.readLine()) != null) {
                    sb.append(lectura).append("\n");
                }
                String archivo = sb.toString();

                HashMap<String, Integer> tabla = new HashMap<>();

                int count = 256;
                for (int i = 0; i < count; i++) {
                    tabla.put("" + (char) i, i);
                }

//                List<Character> Caracter= new ArrayList<>();
//                for (char c :archivo.toCharArray()) {
//                    Caracter.add(c);
//                }
//                for (char c:Caracter) {
//                    
//                    if (!tabla.containsKey(c)) {
//                        tabla.put(""+c, count++);
//                    }
//                    
//                }
                String P = "";

                for (char C : archivo.toCharArray()) {
                    String PC = P + C;
                    if (tabla.containsKey(PC)) {
                        P = PC;
                    } else {
                        resultado.add(tabla.get(P));
                        tabla.put(PC, count++);
                        P = "" + C;
                    }
                }
                if (!P.equals("")) {
                    resultado.add(tabla.get(P));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter salida = new FileWriter("salida.txt");
            BufferedWriter writer = new BufferedWriter(salida);
            for (Integer numero : resultado) {
                writer.write(numero.toString());
                writer.write(" ");

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void lzw(File entrada) {
        StringBuilder sb = new StringBuilder();
        try {

            BufferedReader leer = new BufferedReader(new FileReader(entrada));
            String lectura;
            while ((lectura = leer.readLine()) != null) {
                sb.append(lectura).append("\n");
            }

        } catch (Exception e) {
        }
        String archivo = sb.toString();

        int dictSize = 256;
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.put("" + (char) i, i);
        }

        String w = "";
        List<Integer> result = new ArrayList<>();

        for (char c : archivo.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc)) {
                w = wc;
            } else {
                result.add(dictionary.get(w));
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }

        if (!w.equals("")) {
            result.add(dictionary.get(w));
        }

        try {
            FileWriter salida = new FileWriter("salida.txt");
            BufferedWriter writer = new BufferedWriter(salida);
            for (Integer numero : result) {
                writer.write(numero.toString());
                writer.write(" ");

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LZ78(File entrada) {
        List<String> resultado = new ArrayList<>();
        try {

            StringBuilder sb = new StringBuilder();
            BufferedReader leer = new BufferedReader(new FileReader(entrada));
            String lectura;
            while ((lectura = leer.readLine()) != null) {
                sb.append(lectura).append("\n");
            }
            String archivo = sb.toString();

            HashMap<String, Integer> tabla = new HashMap<>();
            List<Character> letra = new ArrayList<>();
            for (char c : archivo.toCharArray()) {
                letra.add(c);
            }
            int index = 1;
            String P = "";
            for (char c : letra) {
                String PC = P + c;
                if (tabla.containsKey(PC)) {
                    P = P + c;
                } else {
                    int pos = P.equals("") ? 0 : tabla.get(P);
                    resultado.add("(" + pos + "," + c + ",)");
                    tabla.put(PC, index++);
                    P = "";
                }

            }
            if (!P.equals("")) {
                int pos = tabla.get(P);
                resultado.add("(" + pos + ",)");
            }
            try {
                FileWriter salida = new FileWriter("salida.txt");
                BufferedWriter writer = new BufferedWriter(salida);
                for (String numero : resultado) {
                    writer.write(numero);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
            }

        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");

//        int op = 0;
//        boolean ver = true;
//        do {
//
//            System.out.println("1...Comprimir");
//            System.out.println("2...Encriptar");
//            System.out.println("3...Comprimir e imcriptar");
//            switch (op) {
//                case 1 -> {
//                }
//                case 2 -> {
//
//                }
//                case 3 -> {
//
//                }
//                default ->
//                    ver = false;
//            }
//        } while (ver);
        //File entrada = new File("C:\\Users\\walte\\Desktop\\LZW.txt");
        //lzw(entrada);
        String entrada = "C:\\Users\\walte\\Desktop\\Manejo e implementacion de archivos\\Implentacion de archivos";
        Path ruta = Paths.get(entrada);
        File ver = new File(entrada);
       
        List<File> Tofiles = new ArrayList<>();
        int i = 0;
        if (ver.exists()) {
            if (ver.isFile()) {
                Tofiles.add(ver);
               LZW(Tofiles,Tofiles.size());

            } else if (ver.isDirectory()) {
                try (DirectoryStream<Path> steam = Files.newDirectoryStream(ruta)) {
                    for (Path file : steam) {
                        Tofiles.add(file.toFile());
                        i++;
                    }
                    LZW(Tofiles, Tofiles.size());
                } catch (Exception e) {
                }
            } else {

            }
        }
        
        //
    }
}
