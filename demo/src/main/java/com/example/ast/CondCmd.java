package com.example.ast;

public class CondCmd implements Comando {
    private final Expressao condicao;
    private final Comando blocoSe;
    private final Comando blocoSenao;

    public CondCmd(Expressao condicao, Comando blocoSe, Comando blocoSenao) {
        this.condicao = condicao;
        this.blocoSe = blocoSe;
        this.blocoSenao = blocoSenao;
    }

    @Override
    public void executar(Ambiente env) {
        if (condicao.avaliar(env) != 0.0) {
            blocoSe.executar(env);
        } else if (blocoSenao != null) {
            blocoSenao.executar(env);
        }
    }
}

