package com.example;

import com.example.parser.CompiladorExp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class TestePamu {

    private static String rodar(String codigo, String entrada) {
        CompiladorExp.limparMemoria();
        ByteArrayInputStream in = new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBytes, true, StandardCharsets.UTF_8);
        CompiladorExp.executar(codigo, in, out);
        return outBytes.toString(StandardCharsets.UTF_8);
    }

    private static void verificar(String nome, boolean condicao, String detalhe) {
        if (!condicao) {
            throw new AssertionError("FALHA no teste '" + nome + "': " + detalhe);
        }
        System.out.println("[OK] " + nome);
    }

    public static void main(String[] args) {
        // 1. Atribuicao e expressoes aritmeticas com precedencia e parenteses
        String saida1 = rodar("a = 2 + 3 * 4; b = (2 + 3) * 4; paescreve(a); MUESCREVE(b)", "");
        verificar("Aritmetica e precedencia",
                saida1.contains(": 14") && saida1.contains(": 20"),
                saida1);

        // 2. Condicional pa (verdadeiro) sem mu
        String saida2 = rodar("x = 10; pa (x == 10) { paescreve(1); }", "");
        verificar("Condicional pa verdadeiro",
                saida2.contains(": 1"),
                saida2);

        // 3. Condicional pa (falso) com mu - nao deve executar bloco pa!
        String saida3 = rodar("x = 5; pa (x > 10) { paescreve(100); } mu { MUESCREVE(200); }", "");
        verificar("Condicional pa/mu ramo falso",
                !saida3.contains(": 100") && saida3.contains(": 200"),
                saida3);

        // 4. Condicional pa (verdadeiro) com mu - nao deve executar bloco mu!
        String saida4 = rodar("x = 15; pa (x > 10) { paescreve(100); } mu { MUESCREVE(200); }", "");
        verificar("Condicional pa/mu ramo verdadeiro",
                saida4.contains(": 100") && !saida4.contains(": 200"),
                saida4);

        // 5. Repeticao paretir (somatorio de 1 a 5 = 15)
        String saida5 = rodar("i = 1; soma = 0; paretir (i <= 5) { soma = soma + i; i = i + 1; } paescreve(soma);", "");
        verificar("Repeticao paretir",
                saida5.contains(": 15"),
                saida5);

        // 6. Repeticao murepeti (fatorial de 5 = 120)
        String saida6 = rodar("n = 5; fat = 1; murepeti (n > 1) { fat = fat * n; n = n - 1; } MUESCREVE(fat)", "");
        verificar("Repeticao murepeti",
                saida6.contains(": 120"),
                saida6);

        // 7. Leitura muler e paler
        String saida7 = rodar("muler(x) paler(y); z = x + y; paescreve(z);", "7 8\n");
        verificar("Leitura muler e paler",
                saida7.contains(": 15"),
                saida7);

        // 8. Todos os operadores relacionais
        String saida8 = rodar(
                "paescreve(1 < 2); paescreve(2 <= 2); paescreve(3 > 2); " +
                "paescreve(3 >= 3); paescreve(4 == 4); paescreve(4 != 5);", "");
        long countOnes = saida8.lines().filter(l -> l.endsWith(": 1")).count();
        verificar("Operadores relacionais",
                countOnes == 6,
                saida8);

        // 9. Divisao por zero tratada com erro de execucao
        String saida9 = rodar("x = 10 / 0; paescreve(x);", "");
        verificar("Divisao por zero",
                saida9.contains("[ERRO DE EXECUCAO]"),
                saida9);

        // 10. Erro sintatico
        String saida10 = rodar("x = ;", "");
        verificar("Erro sintatico",
                saida10.contains("[ERRO SINTATICO]"),
                saida10);

        // 11. Escrita de texto (string) em paescreve e MUESCREVE
        String saida11 = rodar("paler(nota); pa (nota >= 7) { paescreve(\"Aprovado\"); } mu { MUESCREVE(\"Reprovado\"); }", "8\n");
        verificar("Escrita de string em paescreve/MUESCREVE",
                saida11.contains("Aprovado") && !saida11.contains("Reprovado"),
                saida11);

        String saida12 = rodar("MUESCREVE(42); muescreve(\"ok\");", "");
        verificar("Nova palavra de escrita em maiusculas e minusculas",
                saida12.contains(": 42") && saida12.contains("ok"), saida12);

        System.out.println("\nTodos os 12 testes da linguagem PAMU passaram com sucesso!");
    }
}

