package com.example.ast;

public class EscrCmd implements Comando {
    private final Expressao expressao;
    private final String texto;

    public EscrCmd(Expressao expressao) {
        this.expressao = expressao;
        this.texto = null;
    }

    public EscrCmd(String texto) {
        this.expressao = null;
        this.texto = texto;
    }

    @Override
    public void executar(Ambiente env) {
        if (texto != null) {
            env.escrever(texto);
        } else {
            double valor = expressao.avaliar(env);
            env.escreverResultado(valor);
        }
    }
}
