package com.example.ast;

public class EscrCmd implements Comando {
    private final Expressao expressao;

    public EscrCmd(Expressao expressao) {
        this.expressao = expressao;
    }

    @Override
    public void executar(Ambiente env) {
        double valor = expressao.avaliar(env);
        env.escreverResultado(valor);
    }
}

