package com.example.ast;

public class RepetCmd implements Comando {
    private final Expressao condicao;
    private final Comando bloco;

    public RepetCmd(Expressao condicao, Comando bloco) {
        this.condicao = condicao;
        this.bloco = bloco;
    }

    @Override
    public void executar(Ambiente env) {
        int iteracoes = 0;
        while (condicao.avaliar(env) != 0.0) {
            iteracoes++;
            if (iteracoes > Ambiente.MAX_ITERACOES) {
                throw new RuntimeException("Limite maximo de iteracoes (" + Ambiente.MAX_ITERACOES + ") excedido (possivel loop infinito).");
            }
            bloco.executar(env);
        }
    }
}

