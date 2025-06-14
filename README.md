# 🔐 t2 - projeto passwords

este projeto em java realiza a leitura, classificação, formatação e filtragem de senhas a partir de um arquivo `.csv`. o objetivo é manipular os dados de maneira eficiente, aplicando regras de classificação de senhas, formatando datas e gerando arquivos de saída organizados.

---

## 📚 estrutura do projeto

o projeto é dividido em duas etapas principais:

### 1. classificação das senhas (`PasswordClassifier.java`)
- lê o arquivo `passwords.csv`
- avalia a força de cada senha com base em:
  - quantidade de caracteres
  - presença de letras, números e caracteres especiais
- adiciona uma nova coluna `"class"` com a classificação:
  - muito ruim
  - ruim
  - fraca
  - boa
  - muito boa
- gera o arquivo `password_classifier.csv`

### 2. formatação de datas e filtragem (`DateFormatter.java`)
- lê o arquivo `password_classifier.csv`
- converte a data da coluna de criação da senha (`yyyy-MM-dd HH:mm:ss`) para o formato brasileiro (`dd/MM/yyyy`)
- filtra apenas senhas classificadas como "boa" ou "muito boa"
- gera dois novos arquivos:
  - `passwords_formated_data.csv` (com a data formatada)
  - `passwords_classifier.csv` (apenas senhas boas e muito boas)


---

## 🔃 ordenação dos dados (`PasswordSorter.java`)

esta etapa realiza a ordenação das senhas presentes no arquivo `passwords_formated_data.csv` com base em três critérios diferentes:

- **length**: tamanho da senha  
- **month**: mês de criação da senha  
- **date**: data completa de criação da senha (dd/mm/yyyy)

foram utilizados os seguintes algoritmos de ordenação, implementados manualmente com **arrays**:

- selection sort  
- insertion sort  
- merge sort  
- quick sort  
- quick sort com mediana de 3  
- counting sort  
- heap sort

além disso, para cada combinação de critério e algoritmo, três casos foram gerados:

- **melhor caso**: entrada ordenada em ordem crescente  
- **pior caso**: entrada ordenada em ordem decrescente  
- **caso médio**: entrada original aleatória

são gerados **54 arquivos de saída** no total, com o seguinte padrão de nome:


### 📝 exemplos de nomes de arquivos gerados:

- `sorted_length_quick_melhor.csv`  
- `sorted_date_merge_medio.csv`  
- `sorted_month_counting_pior.csv`

os arquivos são salvos na raiz do projeto e podem ser usados para análise de desempenho e comparação entre os algoritmos de ordenação.

---

## ⏱️ comparação dos tempos de execução

a tabela abaixo apresenta uma comparação dos tempos de execução (em milissegundos) dos algoritmos de ordenação utilizados no projeto, para os três critérios: **tamanho da senha (length)**, **mês da data (month)** e **data completa (date)**.

> 🧪 *os tempos apresentados são ilustrativos. para obter valores reais, execute os testes em sua máquina com o mesmo conjunto de dados.*

| algoritmo             | length (ms) | month (ms) | date (ms) |
|-----------------------|-------------|------------|-----------|
| selection sort        |     142     |    136     |   149     |
| insertion sort        |     128     |    121     |   133     |
| bubble sort           |     198     |    185     |   207     |
| merge sort            |      47     |     43     |    46     |
| quick sort            |      39     |     38     |    41     |
| quick sort (mediana)  |      36     |     34     |    37     |
| counting sort         |      19     |     28     |    n/a    |
| heap sort             |      54     |     51     |    56     |

**observações**:
- o algoritmo **counting sort** só foi aplicado em critérios numéricos (ex: length e mês), por isso não há valor para a data completa.
- os tempos podem variar conforme a máquina e o volume de dados — por exemplo, na minha máquina, a execução completa levou cerca de **8 horas**.

---

## 🧠 arrays utilizados no projeto

neste projeto foram utilizados arrays do tipo `String[]` para representar as linhas dos arquivos csv:

### no `PasswordClassifier.java`
- `String[] header`: representa o cabeçalho original do arquivo.
- `String[] newHeader`: novo cabeçalho com a coluna `"class"`.
- `String[] record`: cada linha lida do csv.
- `String[] newRecord`: cópia da linha com a classificação adicionada.

### no `DateFormatter.java`
- `String[] header`: linha de cabeçalho lida do csv.
- `String[] record`: cada linha de dados que será processada.
- `Arrays.toString(record)`: usado para imprimir linhas mal formatadas no console.

### no `PasswordSorter.java`
- `String[] header`: representa o cabeçalho do arquivo `passwords_formated_data.csv`.
- `String[][] data`: matriz com todas as linhas do arquivo, exceto o cabeçalho.
- `String[] record`: linha individual sendo manipulada nos métodos de ordenação.
- `String[][] sortedData`: matriz com os dados já ordenados.
- `String[] temp`: array auxiliar utilizado em algoritmos como merge sort e heap sort.

---

## ▶️ como executar o projeto

### ✅ pré-requisitos

- **jdk 17** ou superior
- **biblioteca opencsv**

caso utilize maven, adicione a dependência no `pom.xml`:

```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.7.1</version>
</dependency>
```

---

## 💻 execução passo a passo

1. clone ou baixe o projeto.
2. certifique-se de ter o arquivo `passwords.csv` no diretório raiz.
3. compile os arquivos java com:

```bash
javac -cp ".:path/to/opencsv.jar" src/main/java/com/example/*.java
```

4. execute o classificador:

```bash
java -cp ".:path/to/opencsv.jar:src/main/java" com.example.PasswordClassifier
```

5. execute o formatador:

```bash
java -cp ".:path/to/opencsv.jar:src/main/java" com.example.DateFormatter
```

🔁 substitua `path/to/opencsv.jar` pelo caminho da lib opencsv no seu sistema.

---

## 🧪 exemplos de entrada e saída

### 📥 entrada (`passwords.csv`)

```csv
id,username,password,created_at
1,joao123,senha123,2023-06-15 14:23:10
2,ana_b,Abc@2023,2022-09-10 09:11:47
3,lucasx,lucasx,2021-01-02 22:00:00
```

---

## 📤 saída 1: `password_classifier.csv`

arquivo com coluna adicional `"class"`:

```csv
id,username,password,created_at,class
1,joao123,senha123,2023-06-15 14:23:10,fraca
2,ana_b,Abc@2023,2022-09-10 09:11:47,muito boa
3,lucasx,lucasx,2021-01-02 22:00:00,ruim
```

---

## 📤 saída 2: `passwords_formated_data.csv`

arquivo com data formatada:

```csv
id,username,password,created_at,class
1,joao123,senha123,15/06/2023,fraca
2,ana_b,Abc@2023,10/09/2022,muito boa
3,lucasx,lucasx,02/01/2021,ruim
```

---

## 📤 saída 3: `passwords_classifier.csv`

apenas senhas boas ou muito boas:

```csv
id,username,password,created_at,class
2,ana_b,Abc@2023,10/09/2022,muito boa
```

---

## 📁 estrutura esperada

```bash
📦 projeto-passwords/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   ├── DateFormatter.java         # formata datas e filtra senhas boas/muito boas
│                   └── PasswordClassifier.java    # classifica senhas do arquivo passwords.csv
│                   └── PasswordSorter.java        # Ordenação das senhas do arquivo passwords_classifier.csv
├── target/
│   ├── classes/
│   │   └── com/
│   │       └── example/
│   │           ├── DateFormatter.class
│   │           └── PasswordClassifier.class
│   │           └── PasswordSorter.class
│   │
│   ├── generated-sources/
│   │   └── annotations/
│   ├── maven-status/
│   │   └── maven-compiler-plugin/
│   │       └── compile/
│   │           └── default-compile/
│   │               ├── createdFiles.lst
│   │               └── inputFiles.lst
│   └── test-classes/                             # classes compiladas dos testes (caso existam)
├── passwords.csv                       # arquivo original com as senhas (obtido via kaggle)
├── password_classifier.csv             # saída com classificação das senhas
├── passwords_formated_data.csv         # saída com datas formatadas
├── passwords_classifier.csv            # saída com senhas "boa" e "muito boa"
├── pom.xml                             # configuração do projeto com Maven (inclui dependência OpenCSV)
└── README.md                           
```

---

Colaboradores: [Antonio da Silva Lins, Matheus Leite Abreu, Emerson Costa de Sousa]
