package com.example.ast;

public class VariavelExp implements Expressao {
    private final String nome;

    public VariavelExp(String nome) {
        this.nome = nome;
    }

    @Override
    public double avaliar(Ambiente env) {
        return env.obter(nome);
    }
}

