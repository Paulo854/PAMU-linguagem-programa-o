package com.example.ast;

public class UnariaExp implements Expressao {
    private final String operador;
    private final Expressao expressao;

    public UnariaExp(String operador, Expressao expressao) {
        this.operador = operador;
        this.expressao = expressao;
    }

    @Override
    public double avaliar(Ambiente env) {
        double valor = expressao.avaliar(env);
        if ("-".equals(operador)) {
            return -valor;
        }
        return valor;
    }
}

