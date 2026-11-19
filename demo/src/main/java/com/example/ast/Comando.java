package com.example.ast;

@FunctionalInterface
public interface Comando {
    void executar(Ambiente env);
}

