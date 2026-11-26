package com.example.ast;

public class LeitCmd implements Comando {
    private final String identificador;

    public LeitCmd(String identificador) {
        this.identificador = identificador;
    }

    @Override
    public void executar(Ambiente env) {
        env.lerNumero(identificador);
    }
}

