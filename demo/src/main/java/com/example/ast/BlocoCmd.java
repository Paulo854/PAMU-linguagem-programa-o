package com.example.ast;

import java.util.List;

public class BlocoCmd implements Comando {
    private final List<Comando> comandos;

    public BlocoCmd(List<Comando> comandos) {
        this.comandos = comandos;
    }

    @Override
    public void executar(Ambiente env) {
        for (Comando cmd : comandos) {
            cmd.executar(env);
        }
    }
}

