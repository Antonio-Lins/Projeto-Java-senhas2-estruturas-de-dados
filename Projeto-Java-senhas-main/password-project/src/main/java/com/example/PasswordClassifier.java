package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;
import java.util.Arrays; // Mantido para Arrays.copyOf e Arrays.toString em mensagens de erro

// IMPORTAÇÕES DE ESTRUTURAS DE DADOS PERSONALIZADAS
// Estas linhas importam suas classes MyHashMap e MyDynamicArray.
// MyHashMap será usada para contar as classificações de senhas.
// MyDynamicArray é importada porque MyHashMap.keySet() a retorna.
import com.example.datastructures.MyHashMap;
import com.example.datastructures.MyDynamicArray;
// import com.example.datastructures.MySinglyLinkedList; // MySinglyLinkedList não é usada diretamente aqui, mas MyHashMap a utiliza internamente.

public class PasswordClassifier {

    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "passwords.csv";
        String outputFile = "password_classifier.csv";

        // ALTERAÇÃO: USO DE MYHASHMAP
        // Uma instância de MyHashMap é criada para armazenar a contagem de cada classificação de senha.
        // As chaves serão as strings de classificação (ex: "boa", "ruim") e os valores serão as contagens (Integers).
        //
        // PROBLEMA RESOLVIDO: Contagem de Classificações de forma eficiente.
        // JUSTIFICATIVA: MyHashMap permite acesso e atualização eficiente (em média O(1)) das contagens de classificações,
        // sendo ideal para agregar dados categorizados de maneira rápida.
        MyHashMap<String, Integer> classificationCounts = new MyHashMap<>();

        try (InputStream inputStream = PasswordClassifier.class.getClassLoader().getResourceAsStream(inputFile);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {

            if (inputStream == null) {
                System.err.println("arquivo não encontrado: " + inputFile);
                return;
            }

            String[] header = reader.readNext();
            if (header != null) {
                String[] newHeader = new String[header.length + 1];
                System.arraycopy(header, 0, newHeader, 0, header.length);
                newHeader[header.length] = "class";
                writer.writeNext(newHeader);
            } else {
                System.err.println("o arquivo csv está vazio ou não possui cabeçalho.");
                return;
            }
            
            String[] nextRecord;
            int processedCount = 0;

            while ((nextRecord = reader.readNext()) != null) {
                if (nextRecord.length < 3) {
                    System.err.println("linha inválida encontrada. Pulando: " + Arrays.toString(nextRecord));
                    String[] invalidRecordWithClass = Arrays.copyOf(nextRecord, nextRecord.length + 1);
                    invalidRecordWithClass[nextRecord.length] = "erro_processamento";
                    writer.writeNext(invalidRecordWithClass);
                    // ALTERAÇÃO: ATUALIZAÇÃO DA CONTAGEM PARA ERROS
                    // A contagem é atualizada mesmo para registros inválidos.
                    updateClassificationCount(classificationCounts, "erro_processamento");
                    continue;
                }

                String password = nextRecord[1].trim();

                String classification = classifyPassword(password);
                System.out.println("senha: " + password + " | classificação: " + classification);

                String[] classifiedRecord = new String[nextRecord.length + 1];
                System.arraycopy(nextRecord, 0, classifiedRecord, 0, nextRecord.length);
                classifiedRecord[nextRecord.length] = classification;
                
                writer.writeNext(classifiedRecord);
                processedCount++;

                // ALTERAÇÃO: ATUALIZAÇÃO DA CONTAGEM NA MYHASHMAP
                // Para cada senha classificada, a contagem para aquela classificação é incrementada
                // na 'classificationCounts' MyHashMap.
                updateClassificationCount(classificationCounts, classification);
            }

            System.out.println("classificação concluída. arquivo gerado: " + outputFile);
            System.out.println("total de registros processados e escritos (excluindo cabeçalho): " + processedCount);

            // ALTERAÇÃO: EXIBIÇÃO DO RESUMO USANDO MYHASHMAP
            // Após processar todos os registros, o resumo das classificações é impresso
            // Iterando sobre as chaves (classificações) armazenadas na MyHashMap e obtendo suas contagens.
            System.out.println("\n--- Resumo das Classificações ---");
            // 'keySet()' da MyHashMap retorna uma MyDynamicArray das chaves.
            MyDynamicArray<String> classifications = classificationCounts.keySet();
            for (int i = 0; i < classifications.size(); i++) {
                String cls = classifications.get(i);
                Integer count = classificationCounts.get(cls);
                System.out.println("- " + cls + ": " + count + " senhas");
            }
            System.out.println("---------------------------------");

        } catch (IOException | CsvException e) {
            System.err.println("ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * MÉTODO AUXILIAR PARA MYHASHMAP
     * Este método foi adicionado para encapsular a lógica de atualização da contagem
     * de classificações na MyHashMap, verificando se a chave já existe e incrementando
     * ou inicializando a contagem.
     * @param counts MyHashMap para armazenar as contagens.
     * @param classification A classificação a ser atualizada.
     */
    private static void updateClassificationCount(MyHashMap<String, Integer> counts, String classification) {
        Integer currentCount = counts.get(classification);
        if (currentCount == null) {
            counts.put(classification, 1);
        } else {
            counts.put(classification, currentCount + 1);
        }
    }

    public static String classifyPassword(String password) {
        int length = password.length();
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        int types = 0;
        if (hasLetter) types++;
        if (hasNumber) types++;
        if (hasSpecial) types++;

        System.out.println("analisando senha: " + password + " | tamanho: " + length + 
            " | letras: " + hasLetter + " | números: " + hasNumber + " | especiais: " + hasSpecial + 
            " | tipos: " + types);

        if (length < 5 && types == 1) return "muito ruim";
        if (length <= 5 && types == 1) return "ruim";
        if (length <= 6 && types == 2) return "fraca";
        if (length <= 7 && types == 3) return "boa";
        if (length > 8 && types == 3) return "muito boa";

        return "sem classificação";
    }
}
