package com.example.ast;

public class NumeroExp implements Expressao {
    private final double valor;

    public NumeroExp(double valor) {
        this.valor = valor;
    }

    @Override
    public double avaliar(Ambiente env) {
        return valor;
    }
}

