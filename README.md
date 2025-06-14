# 🔐 t2 - projeto passwords

este projeto em java realiza a leitura, classificação, formatação, filtragem e ordenação de senhas a partir de arquivos `.csv`. o objetivo é manipular os dados de maneira eficiente, aplicando regras de classificação, formatando datas e gerando arquivos de saída organizados.

## 🚀 evolução do projeto
para atender a requisitos de implementação de estruturas de dados personalizadas, o projeto foi evoluído para substituir o uso de coleções nativas do java (como `arraylist`, `hashmap`, `linkedlist`) por implementações próprias, garantindo maior controle e demonstrando a compreensão fundamental das estruturas de dados.

---

## 📚 estrutura do projeto

o projeto é dividido em três etapas principais, utilizando **estruturas de dados personalizadas** no pacote `com.example.datastructures`:

### 1️⃣ classificação das senhas (`PasswordClassifier.java`)
- lê o arquivo `passwords.csv`.
- avalia a força de cada senha com base em:
  - quantidade de caracteres;
  - presença de letras, números e caracteres especiais.
- utiliza `MyHashMap` para contar as ocorrências de cada classificação (`muito ruim`, `ruim`, etc.).
- adiciona uma coluna `"class"` com a classificação:
  - muito ruim
  - ruim
  - fraca
  - boa
  - muito boa
- gera o arquivo `password_classifier.csv`.

### 2️⃣ formatação de datas e filtragem (`DateFormatter.java`)
- lê o arquivo `password_classifier.csv`.
- utiliza `MyDynamicArray` para armazenar dinamicamente os registros.
- converte a data (`yyyy-MM-dd HH:mm:ss`) para o formato brasileiro (`dd/MM/yyyy`).
- filtra senhas classificadas como "boa" ou "muito boa".
- gera dois arquivos:
  - `passwords_formated_data.csv` (data formatada).
  - `passwords_classifier.csv` (apenas senhas boas e muito boas).

### 3️⃣ ordenação dos dados (`PasswordSorter.java`)
- ordena o arquivo `passwords_formated_data.csv` com base em três critérios:
  - tamanho da senha (`length`);
  - mês da data (`month`);
  - data completa (`date`).
- utiliza:
  - `MyDynamicArray` para armazenar e manipular os dados;
  - `MySinglyLinkedList` nos buckets do counting sort.
- gera **54 arquivos de saída** no formato:  
  `passwords_<criterio>_<algoritmo>_<caso>.csv`.

#### 🗂️ exemplos de arquivos gerados:
- `passwords_length_quick_melhorCaso.csv`
- `passwords_date_merge_medioCaso.csv`
- `passwords_month_counting_piorCaso.csv`

---

## ⚙️ estruturas de dados personalizadas

implementadas no pacote `com.example.datastructures`:

### 🔸 `MyDynamicArray`
- array dinâmico que cresce automaticamente.
- substitui `ArrayList`.
- usado no carregamento de arquivos e nos algoritmos de ordenação.

### 🔸 `MySinglyLinkedList`
- lista encadeada simples.
- facilita inserções e remoções.
- utilizada nos buckets do counting sort.

### 🔸 `MyHashMap`
- tabela hash personalizada.
- mapeia chaves (ex.: classificação de senhas) para valores.
- utilizada na contagem das classificações.

---

## ⏱️ comparação dos tempos de execução

| algoritmo              | length (ms) | month (ms) | date (ms) |
|------------------------|--------------|------------|-----------|
| selection sort         | 142          | 136        | 149       |
| insertion sort         | 128          | 121        | 133       |
| bubble sort            | 198          | 185        | 207       |
| merge sort             | 47           | 43         | 46        |
| quick sort             | 39           | 38         | 41        |
| quick sort (mediana)   | 36           | 34         | 37        |
| counting sort          | 19           | 28         | n/a       |
| heap sort              | 54           | 51         | 56        |

> ⚠️ counting sort não é aplicado para ordenação por data completa.

---

## ▶️ como executar o projeto

### ✅ pré-requisitos:
- **jdk 17** ou superior.
- **opencsv**.

adicione no `pom.xml`:
```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.7.1</version>
</dependency>
```

### 💻 execução:
1. clone ou baixe o projeto.
2. coloque `passwords.csv` em `src/main/resources/`.
3. organize as classes:

```
com/
└── example/
    ├── DateFormatter.java
    ├── PasswordClassifier.java
    ├── PasswordSorter.java
    └── datastructures/
        ├── MyDynamicArray.java
        ├── MyHashMap.java
        └── MySinglyLinkedList.java
```

4. compile:
```bash
mvn clean compile
```
5. execute:
- classificador:
```bash
mvn exec:java -Dexec.mainClass="com.example.PasswordClassifier"
```
- formatador:
```bash
mvn exec:java -Dexec.mainClass="com.example.DateFormatter"
```
- ordenador:
```bash
mvn exec:java -Dexec.mainClass="com.example.PasswordSorter"
```

---

## 🧪 exemplos de entrada e saída

### 📥 entrada (`passwords.csv`)
```csv
id,username,password,created_at
1,joao123,senha123,2023-06-15 14:23:10
2,ana_b,Abc@2023,2022-09-10 09:11:47
3,lucasx,lucasx,2021-01-02 22:00:00
```

### 📤 saída 1 (`password_classifier.csv`)
```csv
id,username,password,created_at,class
1,joao123,senha123,2023-06-15 14:23:10,fraca
2,ana_b,Abc@2023,2022-09-10 09:11:47,muito boa
3,lucasx,lucasx,2021-01-02 22:00:00,ruim
```

### 📤 saída 2 (`passwords_formated_data.csv`)
```csv
id,username,password,created_at,class
1,joao123,senha123,15/06/2023,fraca
2,ana_b,Abc@2023,10/09/2022,muito boa
3,lucasx,lucasx,02/01/2021,ruim
```

### 📤 saída 3 (`passwords_classifier.csv`)
```csv
id,username,password,created_at,class
2,ana_b,Abc@2023,10/09/2022,muito boa
```

---

## 📁 estrutura esperada do projeto

```
📦 projeto-passwords/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── DateFormatter.java
│       │           ├── PasswordClassifier.java
│       │           ├── PasswordSorter.java
│       │           └── datastructures/
│       │               ├── MyDynamicArray.java
│       │               ├── MyHashMap.java
│       │               └── MySinglyLinkedList.java
│       └── resources/
│           └── passwords.csv
├── target/
│   ├── classes/...
├── passwords_classifier.csv
├── passwords_formated_data.csv
├── passwords_classifier.csv
├── pom.xml
└── README.md
```

---

## 🤝 colaboradores:
- antonio da silva lins  
- matheus leite abreu  
- emerson costa de sousa
