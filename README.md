# semantic-analyzer

Este problema é um análisador semântico que faz uso deste [analisador léxico](https://github.com/UellingtonDamasceno/lexical-analyzer)
para identificar os padrão estabelecidos por essas [regex](https://github.com/UellingtonDamasceno/lexical-analyzer#lista-de-siglas)
e utiliza essa [grámatica](https://github.com/traozin/grammartica) e este [analisador sintático](https://github.com/traozin/syntax-analyzer) 
para definir a ordem sintática e semântica dos tokens.

## Instalação
Para executar o projeto através do terminal, digite o seguinte comando no diretório
raiz do projeto: 

    mvn clean install
  
Em seguida, acesse a pasta `target` e lá deverá conter um arquivo 
`semantic-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar` que corresponde ao 
executável do projeto.

Para iniciar o jar basta executar o seguinte comando:
  
    java -jar semantic-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar
  
 <details>
 <summary>Deseja executar o projeto pelo netbeans?</summary>
 <br>
 <p>
 Para executar esse <i>software</i> é necessário clonar esse repositório. Após isso, já no netbeans acesse aos menus: 
 `files` > `open project` > selecione o projeto(no diretório onde foi salvo) > Abra o projeto > "Construa" o mesmo.
 </p>
 </details>

## Parâmetros de inicalização
 Por padrão o este _software_ oculta no arquivo de saída os tokens obtidos pelos
 [analisador léxico](https://github.com/UellingtonDamasceno/lexical-analyzer). 
 Porém é possível inserir a flag `-l` como no comando abaixo para indicar que 
 deseja visualizar os tokens. 
 
 
 Assim como os tokens obtidos pelo [analisador léxico](https://github.com/UellingtonDamasceno/lexical-analyzer) com o uso da flag
 `-l`, o software oculta por padrão os erros e os tokens inesperados obtidos pelo [analisador sintático](https://github.com/traozin/syntax-analyzer) utilizando 
 respectivamente as flags `-s` e `-c`.
     
    java -jar syntax-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -l -s -c    
 
 
## Entrada
 
 Este analisador é capaz de ler multiplos arquivos de texto, contanto que respeite
 as seguintes regras: 
 - Está em uma pasta chamada `input` que deve está contida no mesmo diretório do `jar`;
 - Todos os arquivos devem ser nomeados com o préfixo `entrada` seguido de um número;
 - Todos os arquivos serem `.txt`;
 
 > :warning: Vale ressaltar que para uma análise semântica precisa, é necessário ter o mínimo(ou no melhor caso: nenhum) erro sintático
 ou léxico.
 
 ### Exemplo de entrada
 
 ```java
 var{
  int i = 10, j = 10;
 }
procedure start {
  print("HELLO WORLD!");
} 
 ```
 
 ### Extras
 
 Para facilitar a vida do desenvolvedor, alteramos a gramática para permitir que o analisador sintático compreendesse ou permitisse
 a análise de assinaturas de função, a qual tem o seguinte estrutura:
 
 ```code 
 <SignatureFunc> ::= <Type> Identifier '('<TypeArgList>')';
 
 <SignatureProce> ::= Identifier '('<TypeArgList>')';
 
 <TypeArgList> ::= <Type> ',' <TypeArgList>`
               |  <Type> 
```

Graças à essa decisão, não é necessário declarar funções exatamente em cima da `procedure start` e evita de causar erros semânticos
por não encontrar uma variável declarada.

## Saída
 
 Será gerado um conjunto de arquivos de saída, denomidado saidaX.txt, onde X é um
 valor numérico, referente ao arquivo de entrada que ele representa.

 
 
 

 
