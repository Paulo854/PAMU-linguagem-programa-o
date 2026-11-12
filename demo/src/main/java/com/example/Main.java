package com.example;

import com.example.parser.CompiladorExp;

public class Main {
    public static void main(String[] args) {
        String expressao = "22+12-2";

        System.out.println("Entrada: " + expressao);
        CompiladorExp.executar(expressao);
    }
}
