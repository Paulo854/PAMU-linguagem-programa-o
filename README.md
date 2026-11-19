# PAMU — Linguagem de Programação

**PAMU** é uma linguagem de programação didática interpretada, desenvolvida em **Java 17** com **JavaCC** e **Maven**, construída com análise léxica, análise sintática e execução baseada em Árvore Sintática Abstrata (**AST**).

---

## Especificação Gramatical

A linguagem segue a seguinte gramática livre de contexto:

```bnf
programa       -> lista-comandos

bloco          -> '{' lista-comandos '}'

lista-comandos -> lista-comandos comando | comando

comando        -> atrib-cmd
                | cond-cmd
                | repet-cmd
                | leit-cmd
                | escr-cmd

atrib-cmd      -> identificador '=' exp ';'

cond-cmd       -> 'pa' '(' exp ')' bloco
                | 'pa' '(' exp ')' bloco 'mu' bloco

repet-cmd      -> 'paretir' '(' exp ')' bloco
                | 'murepeti' '(' exp ')' bloco

leit-cmd       -> 'muler' '(' identificador ')'
                | 'paler' '(' identificador ')' ';'

escr-cmd       -> 'muecreve' '(' exp ')'
                | 'paescreve' '(' exp ')' ';'

exp            -> exp-simples op-relacional exp-simples
                | exp-simples

op-relacional  -> '<' | '<=' | '>' | '>=' | '==' | '!='

exp-simples    -> exp-simples op-soma termo
                | termo

op-soma        -> '+' | '-'

termo          -> termo op-mult fator
                | fator

op-mult        -> '*' | '/'

fator          -> '(' exp ')'
                | numero
                | identificador
```

> **Notas léxicas e sintáticas:**
> - Comentários de linha (`// ...`) e de bloco (`/* ... */`) são ignorados.
> - Números suportam inteiros e decimais (ex.: `42`, `3.14`) e sinal unário (`-x`, `+5`).
> - Comandos de escrita (`paescreve` e `muecreve`) aceitam tanto expressões numéricas quanto textos entre aspas duplas (ex.: `paescreve("Aprovado");`).
> - Ponto e vírgula (`;`) em comandos de leitura (`muler`/`paler`) e escrita (`muecreve`/`paescreve`) é aceito como opcional, atendendo às duas formas da especificação.

---

## Palavras-Chave e Semântica

| Comando / Palavra-chave | Equivalente | Descrição |
|---|---|---|
| `identificador = exp;` | Atribuição | Avalia `exp` e armazena o valor numérico na variável `identificador`. |
| `pa (exp) { ... }` | `if` | Executa o bloco se `exp` for verdadeiro (valor diferente de `0`). |
| `pa (exp) { ... } mu { ... }` | `if / else` | Executa o primeiro bloco se `exp != 0`; caso contrário, executa o bloco de `mu`. |
| `paretir (exp) { ... }` | `while` | Repete a execução do bloco enquanto `exp` for verdadeiro (`!= 0`). |
| `murepeti (exp) { ... }` | `while` | Forma alternativa de repetição condicional (mesma semântica de `paretir`). |
| `muler(id)` / `paler(id);` | Leitura (`input`) | Lê um valor numérico da entrada padrão e armazena em `id`. |
| `muecreve(exp)` / `paescreve(exp);` | Escrita (`print`) | Avalia `exp` e exibe o resultado na saída padrão. |

### Operadores

- **Aritméticos:** `+`, `-`, `*`, `/` (com verificação de divisão por zero).
- **Relacionais:** `<`, `<=`, `>`, `>=`, `==`, `!=` (retornam `1` para verdadeiro e `0` para falso).

---

## Arquitetura do Projeto

```text
PAMU-linguagem-programa-o/
├── README.md
└── demo/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── javacc/
        │   │   └── CompiladorExp.jj       # Especificação léxica e sintática (JavaCC)
        │   └── java/com/example/
        │       ├── Main.java              # Ponto de entrada (demo ou execução de arquivo)
        │       └── ast/                   # Árvore Sintática Abstrata (AST) e ambiente
        │           ├── Ambiente.java      # Tabela de símbolos (memória), entrada e saída
        │           ├── Comando.java       # Interface base de comandos
        │           ├── Expressao.java     # Interface base de expressões
        │           ├── BlocoCmd.java      # Bloco sequencial de comandos
        │           ├── AtribCmd.java      # Comando de atribuição
        │           ├── CondCmd.java       # Condicional pa / mu
        │           ├── RepetCmd.java      # Repetição paretir / murepeti
        │           ├── LeitCmd.java       # Leitura muler / paler
        │           ├── EscrCmd.java       # Escrita muecreve / paescreve
        │           ├── NumeroExp.java     # Literal numérico
        │           ├── VariavelExp.java   # Acesso a variável
        │           ├── BinariaExp.java    # Operações aritméticas e relacionais
        │           └── UnariaExp.java     # Operadores unários (+ / -)
        └── test/
            └── java/com/example/
                └── TestePamu.java         # Suite de testes da linguagem PAMU
```

---

## Como Compilar e Executar

### Pré-requisitos

- **Java JDK 17+**
- **Apache Maven 3.8+**

### 1. Compilar o projeto (gerando o parser JavaCC)

```bash
cd demo
mvn clean test-compile
```

### 2. Executar o programa de demonstração

```bash
java -cp target/classes com.example.Main
```

### 3. Executar um arquivo de código PAMU

Crie um arquivo (por exemplo, `programa.pamu`) e passe o caminho como argumento:

```bash
java -cp target/classes com.example.Main programa.pamu
```

### 4. Executar os testes

```bash
java -cp target/classes:target/test-classes com.example.TestePamu
```

> No Windows (PowerShell / CMD), use `;` no lugar de `:` no classpath:
> ```powershell
> java -cp "target/classes;target/test-classes" com.example.TestePamu
> ```

---

## Exemplos de Código em PAMU

### Condicional (`pa` / `mu`)

```pamu
paler(nota);

pa (nota >= 7) {
    paescreve(1);
} mu {
    muecreve(0);
}
```

### Laço de repetição (`paretir` e `murepeti`)

```pamu
// Somatório de 1 até 5 usando paretir
i = 1;
soma = 0;
paretir (i <= 5) {
    soma = soma + i;
    i = i + 1;
}
paescreve(soma);

// Fatorial de 5 usando murepeti
n = 5;
fat = 1;
murepeti (n > 1) {
    fat = fat * n;
    n = n - 1;
}
muecreve(fat)
```

---

## Tratamento de Erros

O interpretador identifica e informa de forma clara:
- **`[ERRO LEXICO]`**: símbolos ou caracteres inválidos no código-fonte.
- **`[ERRO SINTATICO]`**: violação da gramática com linha, coluna e símbolo inesperado.
- **`[ERRO DE EXECUCAO]`**: divisão por zero ou excesso de iterações (proteção contra loops infinitos).
- **`[AVISO]`**: uso de variável não declarada (assume valor `0`).
