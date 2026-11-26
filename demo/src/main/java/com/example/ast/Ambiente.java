package com.example.ast;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Ambiente {
    public static final int MAX_ITERACOES = 1_000_000;

    private final Map<String, Double> memoria;
    private final Scanner entrada;
    private final PrintStream saida;

    public Ambiente() {
        this(new HashMap<>(), new Scanner(System.in).useLocale(Locale.US), System.out);
    }

    public Ambiente(Map<String, Double> memoria, InputStream entrada, PrintStream saida) {
        this(memoria, new Scanner(entrada).useLocale(Locale.US), saida);
    }

    public Ambiente(Map<String, Double> memoria, Scanner entrada, PrintStream saida) {
        this.memoria = memoria;
        this.entrada = entrada;
        this.saida = saida;
    }

    public Map<String, Double> getMemoria() {
        return memoria;
    }

    public void definir(String nome, double valor) {
        memoria.put(nome, valor);
    }

    public double obter(String nome) {
        if (memoria.containsKey(nome)) {
            return memoria.get(nome);
        }
        saida.println("[AVISO] Variavel '" + nome + "' nao declarada. Assumindo 0.");
        return 0.0;
    }

    public double lerNumero(String nomeVariavel) {
        saida.print("Digite o valor de " + nomeVariavel + ": ");
        try {
            if (entrada.hasNextDouble()) {
                double valor = entrada.nextDouble();
                definir(nomeVariavel, valor);
                return valor;
            } else if (entrada.hasNext()) {
                String token = entrada.next().replace(',', '.');
                double valor = Double.parseDouble(token);
                definir(nomeVariavel, valor);
                return valor;
            }
        } catch (Exception e) {
            if (entrada.hasNextLine()) {
                entrada.nextLine();
            }
        }
        saida.println("[AVISO] Entrada invalida para '" + nomeVariavel + "'. Assumindo 0.");
        definir(nomeVariavel, 0.0);
        return 0.0;
    }

    public void escrever(String mensagem) {
        saida.println(mensagem);
    }

    public void escreverResultado(double valor) {
        saida.println("[SAIDA ACESSIVEL] O resultado da expressao e: " + formatarNumero(valor));
    }

    public static String formatarNumero(double valor) {
        if (valor == Math.rint(valor) && !Double.isInfinite(valor) && !Double.isNaN(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }
}

