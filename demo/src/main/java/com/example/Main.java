package com.example;

import com.example.parser.CompiladorExp;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            Path arquivo = Path.of(args[0]);
            try {
                CompiladorExp.executarArquivo(arquivo);
            } catch (IOException e) {
                System.err.println("[ERRO] Nao foi possivel ler o arquivo '" + arquivo + "': " + e.getMessage());
            }
            return;
        }

        String programaDemo = """
                // Demonstracao completa da linguagem PAMU
                x = 10;
                y = 5;
                resultado = x + y * 2;
                paescreve(resultado);

                // Condicional pa / mu
                pa (resultado >= 20) {
                    muecreve(resultado - 5);
                } mu {
                    paescreve(0);
                }

                // Repeticao com paretir (contagem de 1 ate 3)
                i = 1;
                paretir (i <= 3) {
                    paescreve(i);
                    i = i + 1;
                }

                // Repeticao com murepeti (contagem regressiva de 3 ate 1)
                j = 3;
                murepeti (j > 0) {
                    muecreve(j);
                    j = j - 1;
                }
                """;

        System.out.println("=== Executando programa PAMU ===");
        CompiladorExp.executar(programaDemo);
    }
}
