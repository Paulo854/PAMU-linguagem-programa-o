package com.example.ast;

public class AtribCmd implements Comando {
    private final String identificador;
    private final Expressao expressao;

    public AtribCmd(String identificador, Expressao expressao) {
        this.identificador = identificador;
        this.expressao = expressao;
    }

    @Override
    public void executar(Ambiente env) {
        double valor = expressao.avaliar(env);
        env.definir(identificador, valor);
    }
}

