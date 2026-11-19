package com.example.ast;

public class BinariaExp implements Expressao {
    private final Expressao esquerda;
    private final String operador;
    private final Expressao direita;

    public BinariaExp(Expressao esquerda, String operador, Expressao direita) {
        this.esquerda = esquerda;
        this.operador = operador;
        this.direita = direita;
    }

    @Override
    public double avaliar(Ambiente env) {
        double v1 = esquerda.avaliar(env);
        double v2 = direita.avaliar(env);

        switch (operador) {
            case "+":
                return v1 + v2;
            case "-":
                return v1 - v2;
            case "*":
                return v1 * v2;
            case "/":
                if (v2 == 0.0) {
                    throw new ArithmeticException("Divisao por zero.");
                }
                return v1 / v2;
            case "<":
                return (v1 < v2) ? 1.0 : 0.0;
            case "<=":
                return (v1 <= v2) ? 1.0 : 0.0;
            case ">":
                return (v1 > v2) ? 1.0 : 0.0;
            case ">=":
                return (v1 >= v2) ? 1.0 : 0.0;
            case "==":
                return (v1 == v2) ? 1.0 : 0.0;
            case "!=":
                return (v1 != v2) ? 1.0 : 0.0;
            default:
                throw new IllegalArgumentException("Operador desconhecido: " + operador);
        }
    }
}

