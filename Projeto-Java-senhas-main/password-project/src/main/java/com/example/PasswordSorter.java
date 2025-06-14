package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException; // Mantido, embora não diretamente usado nas novas comparações de data
import java.text.SimpleDateFormat; // Mantido, embora não diretamente usado nas novas comparações de data
import java.util.Arrays; // Mantido para Arrays.copyOf

// IMPORTAÇÕES DE ESTRUTURAS DE DADOS PERSONALIZADAS
// Estas linhas importam suas classes MyDynamicArray e MySinglyLinkedList.
// MyDynamicArray é usada como a principal estrutura para armazenar e manipular os dados do CSV.
// MySinglyLinkedList é usada internamente no Counting Sort para lidar com colisões.
import com.example.datastructures.MyDynamicArray;
import com.example.datastructures.MySinglyLinkedList;

public class PasswordSorter {

    public static void main(String[] args) throws IOException {
        String inputPath = "passwords_formated_data.csv";
        // ALTERAÇÃO: USO DE MYDYNAMICARRAY
        // 'dados' agora é uma instância do seu MyDynamicArray<String[]>, que irá armazenar
        // todas as linhas do CSV de forma dinâmica.
        // PROBLEMA RESOLVIDO: Armazenamento dinâmico de registros lidos do CSV.
        // JUSTIFICATIVA: MyDynamicArray gerencia o crescimento do array interno para armazenar os dados do CSV,
        // evitando arrays de tamanho fixo e a necessidade de redimensionamento manual.
        MyDynamicArray<String[]> dados = readCSV(inputPath);

        String[] algoritmos = { "insertion", "selection", "merge", "quick", "quickMediana", "counting", "heap" };
        String[] casos = { "melhorCaso", "medioCaso", "piorCaso" };
        String[] criterios = { "length", "month", "data" };

        // O cabeçalho é o primeiro elemento do MyDynamicArray de 'dados'.
        String[] header = dados.get(0);
        
        // ALTERAÇÃO: USO DE MYDYNAMICARRAY
        // 'dataToSort' agora é também um MyDynamicArray, que conterá os dados (sem o cabeçalho)
        // que serão passados para os algoritmos de ordenação.
        MyDynamicArray<String[]> dataToSort = new MyDynamicArray<>();
        for (int i = 1; i < dados.size(); i++) { // Percorre 'dados' do segundo elemento em diante
            dataToSort.add(dados.get(i)); // Adiciona cada linha ao 'dataToSort' MyDynamicArray
        }

        for (String criterio : criterios) {
            for (String algoritmo : algoritmos) {
                if (!isAlgoritmoValido(algoritmo, criterio)) continue;
                
                for (String caso : casos) {
                    // ALTERAÇÃO: USO DE MÉTODO CUSTOMIZADO PARA CÓPIA PROFUNDA
                    // Cria uma cópia profunda (independente) do MyDynamicArray 'dataToSort'
                    // para que cada algoritmo de ordenação opere em um conjunto de dados não modificado
                    // pelas execuções anteriores.
                    // PROBLEMA RESOLVIDO: Evitar que as ordenações subsequentes modifiquem os dados originais.
                    // JUSTIFICATIVA: Deep copy garante que cada algoritmo comece com um conjunto limpo de dados,
                    // mantendo a independência dos testes.
                    MyDynamicArray<String[]> copia = deepCopyMyDynamicArray(dataToSort);

                    // Chama a função de ordenação que agora opera em MyDynamicArray
                    sortData(copia, algoritmo, criterio);

                    // Converte MyDynamicArray de volta para String[][] para o método writeCSV.
                    // Esta conversão é necessária apenas no momento da escrita, pois o CSVWriter
                    // da biblioteca OpenCSV espera um array bidimensional nativo.
                    String[][] finalDataForWrite = new String[copia.size() + 1][];
                    finalDataForWrite[0] = header; // Adiciona o cabeçalho de volta
                    for (int i = 0; i < copia.size(); i++) {
                        finalDataForWrite[i + 1] = copia.get(i); // Copia os dados ordenados
                    }

                    String nome = String.format("passwords_%s_%s_%s.csv", criterio, algoritmo, caso);
                    writeCSV(nome, finalDataForWrite);
                }
            }
        }
        System.out.println("Arquivos gerados com sucesso!");
    }

    /**
     * ALTERAÇÃO: NOVO MÉTODO PARA CÓPIA PROFUNDA DE MYDYNAMICARRAY
     * Copia um MyDynamicArray, criando novas instâncias para os arrays internos (deep copy).
     * @param original O MyDynamicArray original.
     * @return Uma nova instância de MyDynamicArray contendo cópias profundas dos elementos.
     */
    public static MyDynamicArray<String[]> deepCopyMyDynamicArray(MyDynamicArray<String[]> original) {
        MyDynamicArray<String[]> copy = new MyDynamicArray<>();
        for (int i = 0; i < original.size(); i++) {
            if (original.get(i) != null) {
                // Cria uma cópia do array interno (linha do CSV) para garantir uma deep copy.
                copy.add(Arrays.copyOf(original.get(i), original.get(i).length));
            } else {
                copy.add(null);
            }
        }
        return copy;
    }

    /**
     * ALTERAÇÃO: MÉTODO readCSV ADAPTADO PARA MYDYNAMICARRAY
     * Implementação manual de leitura de CSV para um MyDynamicArray de arrays de strings.
     * Lê linha por linha e adiciona ao array dinâmico.
     * @param filePath Caminho do arquivo CSV.
     * @return Um MyDynamicArray contendo todas as linhas do CSV (exceto vazias).
     */
    public static MyDynamicArray<String[]> readCSV(String filePath) throws IOException {
        MyDynamicArray<String[]> lines = new MyDynamicArray<>(); // IMPLEMENTAÇÃO: MyDynamicArray
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Pula linhas vazias
                }
                
                String[] parts = parseCsvLine(line);
                
                if (parts != null && parts.length >= 5) {
                    lines.add(parts); // Adiciona as partes ao MyDynamicArray
                } else {
                    System.err.println("Linha CSV inválida ignorada: " + line);
                }
            }
        }
        return lines; // Retorna o MyDynamicArray preenchido
    }

    /**
     * Permanece inalterado.
     * Parsea uma linha CSV manual (sem OpenCSV para esta parte).
     * Lida com aspas duplas e o separador.
     */
    private static String[] parseCsvLine(String line) {
        if (line.startsWith("\"") && line.endsWith("\"")) {
            line = line.substring(1, line.length() - 1);
        }
        
        String[] rawParts = line.split("\",\"");
        
        for (int i = 0; i < rawParts.length; i++) {
            rawParts[i] = rawParts[i].replace("\"", "");
        }
        return rawParts;
    }

    /**
     * Permanece inalterado, pois o CSVWriter espera String[][].
     * Escreve um array bidimensional em um arquivo CSV.
     * Os campos serão unidos por ";".
     */
    public static void writeCSV(String nomeArquivo, String[][] dados) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(nomeArquivo))) {
            for (String[] linha : dados) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < linha.length; i++) {
                    sb.append("\"").append(linha[i].replace("\"", "\"\"")).append("\"");
                    if (i < linha.length - 1) {
                        sb.append(",");
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        }
    }
    
    /**
     * ALTERAÇÃO: PARÂMETRO 'dados' AGORA É MYDYNAMICARRAY
     * Direciona os dados para o algoritmo de ordenação apropriado com base no critério e algoritmo selecionados.
     * @param dados O MyDynamicArray de dados a serem ordenados.
     * @param algoritmo O nome do algoritmo de ordenação a ser usado.
     * @param criterio O critério de ordenação (length, month, data).
     */
    public static void sortData(MyDynamicArray<String[]> dados, String algoritmo, String criterio) {
        switch (algoritmo) {
            case "insertion":
                insertionSort(dados, criterio);
                break;
            case "selection":
                selectionSort(dados, criterio);
                break;
            case "merge":
                mergeSort(dados, criterio);
                break;
            case "quick":
                quickSort(dados, criterio, 0, dados.size() - 1);
                break;
            case "quickMediana":
                quickSortMediana(dados, criterio, 0, dados.size() - 1);
                break;
            case "counting":
                if (criterio.equals("length")) {
                    countingSortLength(dados);
                } else {
                    System.err.println("Counting Sort só é aplicável para o critério 'length'.");
                }
                break;
            case "heap":
                heapSort(dados, criterio);
                break;
            default:
                throw new IllegalArgumentException("Algoritmo de ordenação inválido: " + algoritmo);
        }
    }

    /**
     * Permanece inalterado.
     * Verifica se a combinação de algoritmo e critério é válida.
     */
    public static boolean isAlgoritmoValido(String algoritmo, String criterio) {
        return switch (algoritmo) {
            case "insertion", "selection", "merge", "quick", "heap" -> true;
            case "quickMediana" -> !criterio.equals("data");
            case "counting" -> criterio.equals("length");
            default -> false;
        };
    }

    /** Permanece inalterado. Compara dois registros com base no comprimento da senha. */
    private static int compareLength(String[] o1, String[] o2) {
        int length1 = Integer.parseInt(o1[2]);
        int length2 = Integer.parseInt(o2[2]);
        return Integer.compare(length1, length2);
    }

    /** Permanece inalterado. Compara dois registros com base no mês da data. */
    private static int compareMonth(String[] o1, String[] o2) {
        int month1 = Integer.parseInt(o1[3].split("/")[1]);
        int month2 = Integer.parseInt(o2[3].split("/")[1]);
        return Integer.compare(month1, month2);
    }

    /** Permanece inalterado. Compara dois registros com base na data completa. */
    private static int compareDate(String[] o1, String[] o2) {
        String[] parts1 = o1[3].split("/");
        String dateStr1 = String.format("%s%s%s", parts1[2], parts1[1], parts1[0]);
        
        String[] parts2 = o2[3].split("/");
        String dateStr2 = String.format("%s%s%s", parts2[2], parts2[1], parts2[0]);
        
        return dateStr1.compareTo(dateStr2);
    }
    
    /**
     * ALTERAÇÃO: MÉTODO swap ADAPTADO PARA MYDYNAMICARRAY
     * Troca dois elementos dentro de um MyDynamicArray.
     * @param array O MyDynamicArray onde a troca será realizada.
     * @param i O índice do primeiro elemento.
     * @param j O índice do segundo elemento.
     */
    private static void swap(MyDynamicArray<String[]> array, int i, int j) {
        String[] temp = array.get(i); // Obtém o elemento no índice i
        array.set(i, array.get(j));   // Define o elemento no índice i com o elemento no índice j
        array.set(j, temp);           // Define o elemento no índice j com o elemento temporário
    }

    // === ALGORITMOS DE ORDENAÇÃO (OPERANDO EM MYDYNAMICARRAY) ===

    /**
     * ALTERAÇÃO: insertionSort ADAPTADO PARA MYDYNAMICARRAY
     * Implementa o algoritmo Insertion Sort para um MyDynamicArray.
     * @param array O MyDynamicArray a ser ordenado.
     * @param criterio O critério de ordenação.
     */
    public static void insertionSort(MyDynamicArray<String[]> array, String criterio) {
        for (int i = 1; i < array.size(); i++) { // Usa array.size() para o tamanho
            String[] key = array.get(i); // Obtém o elemento 'chave'
            int j = i - 1;
            // Usa array.get() para comparar elementos e array.set() para mover
            while (j >= 0 && compare(array.get(j), key, criterio) > 0) {
                array.set(j + 1, array.get(j));
                j--;
            }
            array.set(j + 1, key);
        }
    }

    /**
     * ALTERAÇÃO: selectionSort ADAPTADO PARA MYDYNAMICARRAY
     * Implementa o algoritmo Selection Sort para um MyDynamicArray.
     * @param array O MyDynamicArray a ser ordenado.
     * @param criterio O critério de ordenação.
     */
    public static void selectionSort(MyDynamicArray<String[]> array, String criterio) {
        for (int i = 0; i < array.size() - 1; i++) { // Usa array.size()
            int minIdx = i;
            for (int j = i + 1; j < array.size(); j++) { // Usa array.size()
                if (compare(array.get(j), array.get(minIdx), criterio) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                swap(array, i, minIdx); // Usa o método swap customizado
            }
        }
    }

    /**
     * ALTERAÇÃO: mergeSort ADAPTADO PARA MYDYNAMICARRAY
     * Implementa o algoritmo Merge Sort para um MyDynamicArray.
     * A divisão dos sub-arrays agora cria novos MyDynamicArray.
     * @param array O MyDynamicArray a ser ordenado.
     * @param criterio O critério de ordenação.
     */
    public static void mergeSort(MyDynamicArray<String[]> array, String criterio) {
        if (array.size() <= 1) return;
        int mid = array.size() / 2;

        // ALTERAÇÃO: CRIAÇÃO DE SUB-ARRAYS SEM ClassCastException
        // Popula os MyDynamicArray 'left' e 'right' diretamente do 'array' principal
        // Isso evita o ClassCastException que ocorria ao tentar converter Object[] para String[][]
        MyDynamicArray<String[]> left = new MyDynamicArray<>();
        for(int i = 0; i < mid; i++) {
            left.add(array.get(i)); 
        }

        MyDynamicArray<String[]> right = new MyDynamicArray<>();
        for(int i = mid; i < array.size(); i++) { 
            right.add(array.get(i));
        }

        mergeSort(left, criterio);
        mergeSort(right, criterio);
        merge(array, left, right, criterio);
    }

    /**
     * ALTERAÇÃO: merge ADAPTADO PARA MYDYNAMICARRAY
     * Combina dois MyDynamicArray ordenados em um único MyDynamicArray.
     * @param array O MyDynamicArray resultante da fusão.
     * @param left O MyDynamicArray da metade esquerda.
     * @param right O MyDynamicArray da metade direita.
     * @param criterio O critério de ordenação.
     */
    public static void merge(MyDynamicArray<String[]> array, MyDynamicArray<String[]> left, MyDynamicArray<String[]> right, String criterio) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (compare(left.get(i), right.get(j), criterio) <= 0) {
                array.set(k++, left.get(i++));
            } else {
                array.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) array.set(k++, left.get(i++));
        while (j < right.size()) array.set(k++, right.get(j++));
    }

    /**
     * ALTERAÇÃO: quickSort ADAPTADO PARA MYDYNAMICARRAY
     * Implementa o algoritmo Quick Sort para um MyDynamicArray.
     * @param array O MyDynamicArray a ser ordenado.
     * @param criterio O critério de ordenação.
     * @param low O índice inicial.
     * @param high O índice final.
     */
    public static void quickSort(MyDynamicArray<String[]> array, String criterio, int low, int high) {
        if (low < high) {
            int pi = partition(array, criterio, low, high);
            quickSort(array, criterio, low, pi - 1);
            quickSort(array, criterio, pi + 1, high);
        }
    }

    /**
     * ALTERAÇÃO: partition ADAPTADO PARA MYDYNAMICARRAY
     * Implementa a função de partição para o Quick Sort em um MyDynamicArray.
     * @param array O MyDynamicArray a ser particionado.
     * @param criterio O critério de ordenação.
     * @param low O índice inicial.
     * @param high O índice final.
     * @return O índice do pivô após a partição.
     */
    public static int partition(MyDynamicArray<String[]> array, String criterio, int low, int high) {
        String[] pivot = array.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (compare(array.get(j), pivot, criterio) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    /**
     * ALTERAÇÃO: quickSortMediana ADAPTADO PARA MYDYNAMICARRAY
     * Implementa o algoritmo Quick Sort com pivô de mediana de três para um MyDynamicArray.
     * @param array O MyDynamicArray a ser ordenado.
     * @param criterio O critério de ordenação.
     * @param low O índice inicial.
     * @param high O índice final.
     */
    public static void quickSortMediana(MyDynamicArray<String[]> array, String criterio, int low, int high) {
        if (low < high) {
            int pi = partitionMediana(array, criterio, low, high);
            quickSortMediana(array, criterio, low, pi - 1);
            quickSortMediana(array, criterio, pi + 1, high);
        }
    }

    /**
     * ALTERAÇÃO: partitionMediana ADAPTADO PARA MYDYNAMICARRAY
     * Implementa a função de partição com mediana de três para o Quick Sort em um MyDynamicArray.
     * @param array O MyDynamicArray a ser particionado.
     * @param criterio O critério de ordenação.
     * @param low O índice inicial.
     * @param high O índice final.
     * @return O índice do pivô após a partição.
     */
    public static int partitionMediana(MyDynamicArray<String[]> array, String criterio, int low, int high) {
        int mid = (low + high) / 2;
        
        String[] a = array.get(low);
        String[] b = array.get(mid);
        String[] c = array.get(high);
        
        String[] pivot;
        if (compare(a, b, criterio) < 0) {
            if (compare(b, c, criterio) < 0) pivot = b;
            else if (compare(a, c, criterio) < 0) pivot = c;
            else pivot = a;
        } else {
            if (compare(a, c, criterio) < 0) pivot = a;
            else if (compare(b, c, criterio) < 0) pivot = c;
            else pivot = b;
        }

        int pivotIndex = -1;
        for (int i = low; i <= high; i++) {
            if (array.get(i) == pivot) {
                pivotIndex = i;
                break;
            }
        }
        if (pivotIndex != -1) {
            swap(array, pivotIndex, high);
        }
        
        return partition(array, criterio, low, high);
    }

    /**
     * ALTERAÇÃO: countingSortLength ADAPTADO PARA MYDYNAMICARRAY E MYSINGLYLINKEDLIST
     * Implementa o algoritmo Counting Sort para um MyDynamicArray, especificamente para o critério 'length'.
     * Utiliza MySinglyLinkedList para os "buckets".
     * @param array O MyDynamicArray a ser ordenado.
     */
    public static void countingSortLength(MyDynamicArray<String[]> array) {
        // PROBLEMA RESOLVIDO: Counting Sort sem uso de Listas nativas para buckets.
        // JUSTIFICATIVA: MySinglyLinkedList é usada para os buckets, permitindo lidar com múltiplos
        // elementos tendo o mesmo "length" (colisões) de forma eficiente, sem usar ArrayList.
        if (array.size() == 0) return;

        int minLength = Integer.parseInt(array.get(0)[2]);
        int maxLength = Integer.parseInt(array.get(0)[2]);

        for (int i = 1; i < array.size(); i++) {
            int currentLength = Integer.parseInt(array.get(i)[2]);
            if (currentLength < minLength) minLength = currentLength;
            if (currentLength > maxLength) maxLength = currentLength;
        }

        int range = maxLength - minLength + 1;
        
        // IMPLEMENTAÇÃO: MySinglyLinkedList para os "buckets"
        // Cria um array de MySinglyLinkedLists, onde cada MySinglyLinkedList é um "bucket"
        // para armazenar os elementos com um determinado comprimento.
        @SuppressWarnings("unchecked")
        MySinglyLinkedList<String[]>[] buckets = new MySinglyLinkedList[range];
        
        // Inicializa cada MySinglyLinkedList em cada posição do array de buckets.
        for (int i = 0; i < range; i++) {
            buckets[i] = new MySinglyLinkedList<>();
        }
        
        // Distribui os elementos do MyDynamicArray de entrada nos buckets apropriados.
        for (int i = 0; i < array.size(); i++) {
            String[] element = array.get(i);
            int length = Integer.parseInt(element[2]);
            buckets[length - minLength].add(element); // Adiciona o elemento ao MySinglyLinkedList no bucket
        }

        int currentArrayIndex = 0;
        // Recompõe o array original (MyDynamicArray) a partir dos buckets ordenados.
        // O loop 'for (MySinglyLinkedList<String[]> bucket : buckets)' funciona porque
        // MySinglyLinkedList implementa a interface Iterable.
        for (MySinglyLinkedList<String[]> bucket : buckets) {
            // O loop interno também funciona porque MySinglyLinkedList é iterável.
            for (String[] element : bucket) {
                array.set(currentArrayIndex++, element); // Define o elemento no MyDynamicArray
            }
        }
    }
    
    /**
     * ALTERAÇÃO: heapSort ADAPTADO PARA MYDYNAMICARRAY
     * Implementa o algoritmo Heap Sort para um MyDynamicArray.
     * @param array O MyDynamicArray a ser ordenado.
     * @param criterio O critério de ordenação.
     */
    public static void heapSort(MyDynamicArray<String[]> array, String criterio) {
        int n = array.size(); // Usa array.size()
        // Constrói o heap (reorganiza o array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i, criterio);
        }
        // Extrai elementos um por um do heap
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i); // Usa o método swap customizado
            heapify(array, i, 0, criterio);
        }
    }

    /**
     * ALTERAÇÃO: heapify ADAPTADO PARA MYDYNAMICARRAY
     * Função auxiliar para heap sort, que transforma um sub-árvore em um max heap.
     * @param array O MyDynamicArray sendo heapificado.
     * @param n O tamanho atual do heap.
     * @param i O índice raiz da sub-árvore.
     * @param criterio O critério de ordenação.
     */
    public static void heapify(MyDynamicArray<String[]> array, int n, int i, String criterio) {
        int largest = i; // Inicializa largest como raiz
        int l = 2 * i + 1; // filho esquerdo = 2*i + 1
        int r = 2 * i + 2; // filho direito = 2*i + 2

        // Compara com o filho esquerdo
        if (l < n && compare(array.get(l), array.get(largest), criterio) > 0) {
            largest = l;
        }

        // Compara com o filho direito
        if (r < n && compare(array.get(r), array.get(largest), criterio) > 0) {
            largest = r;
        }

        // Se o maior não for a raiz, troca e continua heapificando
        if (largest != i) {
            swap(array, i, largest); // Usa o método swap customizado
            heapify(array, n, largest, criterio);
        }
    }

    /** Permanece inalterado. Método central de comparação. */
    private static int compare(String[] o1, String[] o2, String criterio) {
        switch (criterio) {
            case "length": return compareLength(o1, o2);
            case "month": return compareMonth(o1, o2);
            case "data": return compareDate(o1, o2);
            default: throw new IllegalArgumentException("Critério de comparação inválido: " + criterio);
        }
    }
}
