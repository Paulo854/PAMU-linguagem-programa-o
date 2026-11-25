# PAMU — linguagem de programação

PAMU é uma linguagem didática para operações numéricas, entrada e saída, condições e repetições. O projeto usa Java 17, JavaCC e Maven. O JavaCC gera o analisador léxico e o sintático; o parser constrói uma árvore sintática abstrata (AST), que é executada por um interpretador Java. Não é gerado um executável nativo nem código de máquina.

```pamu
paler(nota);
pa (nota >= 7) {
    MUESCREVE("Aprovado");
} mu {
    MUESCREVE("Reprovado");
}
```

## Linguagem e comportamento

| Construção | Significado |
| --- | --- |
| `x = expressao;` | Avalia e guarda um número em `x`. A atribuição exige `;`. |
| `pa (expressao) { ... } mu { ... }` | Condição com `mu` opcional; zero é falso e qualquer outro valor é verdadeiro. |
| `paretir (expressao) { ... }` / `murepeti (...) { ... }` | Repete enquanto a condição for verdadeira; limite de 1.000.000 de iterações por execução do laço. |
| `paler(x)` / `muler(x)` | Lê um número em `x`; `;` é opcional. |
| `MUESCREVE(expressao)` / `muescreve(expressao)` / `paescreve(expressao)` | Escreve número ou texto literal; `;` é opcional. |
| `expressao;` | Expressão isolada também é avaliada e impressa; `;` é opcional. |

Números usam `.` como separador decimal no código; na entrada são aceitos `.` e `,`. Variáveis numéricas não inicializadas geram aviso e são lidas como zero. Cadeias entre aspas duplas são aceitas apenas diretamente na escrita, não em variáveis ou expressões; não há escape de aspas. Operadores relacionais retornam `1` ou `0`. Divisão por zero gera erro de execução. Comentários `//` e `/* ... */` são ignorados.

## Gramática formal (EBNF)

Esta gramática corresponde a `demo/src/main/javacc/CompiladorExp.jj`. `[]` significa opcional, `{}` repetição zero ou mais vezes, `|` alternativa e `()` agrupamento. Terminais estão entre aspas; `EOF` é o fim do arquivo.

```ebnf
programa      = comando, { comando }, EOF ;
bloco         = "{", [ comando, { comando } ], "}" ;
comando       = atribuicao | condicional | repeticao | leitura | escrita
              | expressao, [ ";" ] ;
atribuicao    = identificador, "=", expressao, ";" ;
condicional   = "pa", "(", expressao, ")", bloco, [ "mu", bloco ] ;
repeticao     = ( "paretir" | "murepeti" ), "(", expressao, ")", bloco ;
leitura       = ( "muler" | "paler" ), "(", identificador, ")", [ ";" ] ;
escrita       = ( "MUESCREVE" | "muescreve" | "paescreve" ), "(",
                ( texto | expressao ), ")", [ ";" ] ;
expressao     = soma, [ op_relacional, soma ] ;
op_relacional = "<" | "<=" | ">" | ">=" | "==" | "!=" ;
soma          = termo, { ( "+" | "-" ), termo } ;
termo         = fator, { ( "*" | "/" ), fator | "(", expressao, ")" } ;
fator         = "(", expressao, ")" | ( "+" | "-" ), fator
              | numero | identificador ;
numero        = digito, { digito }, [ ".", digito, { digito } ] ;
identificador = ( letra | "_" ), { letra | digito | "_" } ;
letra         = "a" | ... | "z" | "A" | ... | "Z" ;
digito        = "0" | ... | "9" ;
texto         = '"', { caractere_sem_aspas_ou_quebra_de_linha }, '"' ;
```

Palavras reservadas não são identificadores. Espaços, tabulações, quebras de linha e comentários são ignorados pelo analisador léxico. O programa precisa de pelo menos um comando, mas um bloco pode ser vazio. Uma expressão aceita no máximo um operador relacional sem parênteses. A precedência, da maior para a menor, é: parênteses e sinais unários; multiplicação/divisão, incluindo multiplicação implícita como `2(3 + 4)`; adição/subtração; comparação. Operações de mesmo nível associam-se à esquerda.

## Compilação e execução

Pré-requisitos: JDK 17 ou superior e Maven 3.8 ou superior. Na raiz do repositório:

```powershell
cd demo
mvn clean test-compile
java -cp target/classes com.example.Main
java -cp target/classes com.example.Main ../exemplos/validos/condicional.pamu
java -cp "target/classes;target/test-classes" com.example.TestePamu
```

O primeiro comando Maven gera os fontes do parser em `demo/target/generated-sources/javacc`, compila o projeto e compila a classe de testes. A classe de testes é executada explicitamente pelo último comando; `mvn test` sozinho não executa essa suíte manual. Em Linux/macOS, use `:` no lugar de `;` no classpath dos testes. A primeira compilação pode precisar baixar dependências Maven. Para outro arquivo, substitua o caminho passado a `Main`. A demonstração sem argumentos não precisa de entrada; o exemplo de condicional pede uma nota.

Veja `exemplos/validos` e `exemplos/invalidos`. Os arquivos inválidos demonstram erros léxicos, sintáticos e de execução: os dois primeiros não executam nenhum comando, pois a análise é concluída antes da execução. Um erro de execução pode ocorrer depois de comandos anteriores já terem sido executados. As mensagens são `[ERRO LEXICO]`, `[ERRO SINTATICO]` ou `[ERRO DE EXECUCAO]`. O programa exibe o erro na saída e não garante código de saída diferente de zero; verifique a mensagem.

## Estrutura

- `demo/src/main/javacc/CompiladorExp.jj`: tokens, regras, construção da AST e tratamento de erros.
- `demo/src/main/java/com/example/ast/`: nós da AST e ambiente de execução.
- `demo/src/main/java/com/example/Main.java`: entrada de linha de comando e demonstração.
- `demo/src/test/java/com/example/TestePamu.java`: testes executáveis manualmente.
- `exemplos/`: programas válidos e inválidos.

## Matriz de responsabilidade

A matriz abaixo identifica o responsável pelo desenvolvimento de cada parte da linguagem, conforme o trabalho efetivamente realizado. O responsável deve explicar as regras e o código correspondente durante a apresentação.

| Responsável | Parte | Código e regras para apresentar |
| --- | --- | --- |
| Paulo Victor | Análise léxica e erros | `SKIP`, `TOKEN`, `executar` em `CompiladorExp.jj` |
| Paulo Victor | Programa, blocos e atribuição | `programa`, `bloco`, `listaComandos`, `comando`, `atribCmd`; `BlocoCmd`, `AtribCmd` |
| Paulo Victor | Condições e repetição | `condCmd`, `repetCmd`; `CondCmd`, `RepetCmd` |
| Paulo Victor | Entrada e saída | `leitCmd`, `escrCmd`; `LeitCmd`, `EscrCmd`, `Ambiente` |
| Paulo Victor | Expressões e operadores | `exp`, `opRelacional`, `expSimples`, `opSoma`, `termo`, `opMult`, `fator`; classes `*Exp` |
| Paulo Victor | Integração, execução e testes | `Main`, `pom.xml`, `TestePamu` e exemplos |
