package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays; // Mantido para Arrays.copyOf e Arrays.toString em mensagens de erro

// IMPORTAÇÃO DE ESTRUTURA DE DADOS PERSONALIZADA
// Esta linha importa a sua classe MyDynamicArray, que substitui o uso de ArrayLists e arrays nativos
// para o armazenamento dinâmico de coleções.
import com.example.datastructures.MyDynamicArray;

public class DateFormatter {

    // ALTERAÇÃO: REMOÇÃO DE CONSTANTE
    // 'INITIAL_CAPACITY' não é mais necessária aqui, pois 'MyDynamicArray' gerencia sua própria capacidade
    // e redimensionamento automaticamente, eliminando a necessidade de pré-definir um tamanho ou
    // de lógica de redimensionamento manual neste arquivo.
    // private static final int INITIAL_CAPACITY = 100;

    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "password_classifier.csv";
        String outputFileFormatted = "passwords_formated_data.csv";
        String outputFileFiltered = "passwords_classifier.csv";

        try (
                CSVReader reader = new CSVReader(new FileReader(inputFile));
                CSVWriter writerFormatted = new CSVWriter(new FileWriter(outputFileFormatted));
                CSVWriter writerFiltered = new CSVWriter(new FileWriter(outputFileFiltered))
        ) {
            // ALTERAÇÃO: USO DE MYDYNAMICARRAY
            // Anteriormente, 'allRecords' era um array bidimensional nativo (String[][]).
            // Agora, ele é uma instância da sua classe 'MyDynamicArray', projetada para armazenar
            // arrays de strings (representando linhas do CSV) dinamicamente.
            //
            // PROBLEMA RESOLVIDO: Armazenamento dinâmico de registros lidos do CSV.
            // JUSTIFICATIVA: MyDynamicArray gerencia automaticamente o redimensionamento do array interno
            // à medida que novos registros são adicionados, eliminando a necessidade de lógica
            // de redimensionamento manual e desperdício de memória de arrays fixos.
            MyDynamicArray<String[]> allRecords = new MyDynamicArray<>();

            String[] header = reader.readNext();
            if (header == null) {
                System.err.println("O arquivo CSV está vazio ou não possui cabeçalho.");
                return;
            }

            writerFormatted.writeNext(header);
            writerFiltered.writeNext(header);

            String[] nextRecord;
            int currentRecordCount = 0; // Contador de registros no MyDynamicArray
            // ALTERAÇÃO: ADIÇÃO DE ELEMENTOS AO MYDYNAMICARRAY
            // O método 'add()' do MyDynamicArray é usado para adicionar cada nova linha lida do CSV.
            // O MyDynamicArray se encarrega de expandir sua capacidade conforme necessário.
            while ((nextRecord = reader.readNext()) != null) {
                allRecords.add(nextRecord);
                currentRecordCount++;
            }

            System.out.println("Total de linhas de dados lidas (excluindo cabeçalho): " + currentRecordCount);

            // ALTERAÇÃO: USO DE MYDYNAMICARRAY
            // 'formattedRecords' e 'filteredRecords' também foram alterados para MyDynamicArray,
            // permitindo que eles cresçam dinamicamente à medida que os registros processados são adicionados.
            MyDynamicArray<String[]> formattedRecords = new MyDynamicArray<>();
            MyDynamicArray<String[]> filteredRecords = new MyDynamicArray<>();

            // ALTERAÇÃO: ITENERAÇÃO E OBTENÇÃO DE ELEMENTOS DO MYDYNAMICARRAY
            // O loop agora itera usando 'allRecords.size()' para obter o número total de elementos
            // e 'allRecords.get(i)' para acessar elementos por índice.
            for (int i = 0; i < allRecords.size(); i++) {
                String[] record = allRecords.get(i);

                if (record == null || record.length < 5) {
                    System.err.println("Linha mal formatada encontrada, com menos colunas do que o esperado. Linha: " + Arrays.toString(record));
                    continue;
                }

                String originalDate = record[3];
                String formattedDate = formatDate(originalDate);

                String[] recordWithFormattedDate = Arrays.copyOf(record, record.length);
                recordWithFormattedDate[3] = formattedDate;

                // ALTERAÇÃO: ADIÇÃO DE ELEMENTOS AO MYDYNAMICARRAY
                formattedRecords.add(recordWithFormattedDate);

                String classification = record[4].toLowerCase();
                if (classification.equals("boa") || classification.equals("muito boa")) {
                    // ALTERAÇÃO: ADIÇÃO DE ELEMENTOS AO MYDYNAMICARRAY
                    filteredRecords.add(recordWithFormattedDate);
                }
            }

            // ALTERAÇÃO: ITENERAÇÃO E ESCRITA DE ELEMENTOS DO MYDYNAMICARRAY
            // Os loops para escrever os registros formatados e filtrados também usam 'size()' e 'get()'
            // do MyDynamicArray. O método 'writerFormatted.writeNext()' espera um 'String[]',
            // que é exatamente o que 'formattedRecords.get(i)' retorna.
            for (int i = 0; i < formattedRecords.size(); i++) {
                writerFormatted.writeNext(formattedRecords.get(i));
            }

            for (int i = 0; i < filteredRecords.size(); i++) {
                writerFiltered.writeNext(filteredRecords.get(i));
            }

            System.out.println("✅ Arquivos gerados com sucesso:");
            System.out.println("- Arquivo com datas formatadas: " + outputFileFormatted);
            System.out.println("- Arquivo de senhas filtradas (boas/muito boas): " + outputFileFiltered);

        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo de entrada não encontrado em '" + inputFile + "'. Certifique-se de que ele existe.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Erro de E/S ao ler/escrever arquivos CSV: " + e.getMessage());
            e.printStackTrace();
        } catch (CsvException e) {
            System.err.println("Erro ao processar o arquivo CSV (biblioteca OpenCSV): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ALTERAÇÃO: REMOÇÃO DE MÉTODO AUXILIAR
    // O método 'resizeArray' foi removido porque sua funcionalidade foi encapsulada
    // e é automaticamente gerenciada pela classe 'MyDynamicArray'.
    // private static String[][] resizeArray(String[][] original, int newCapacity) {
    //     String[][] newArray = new String[newCapacity][];
    //     System.arraycopy(original, 0, newArray, 0, Math.min(original.length, newCapacity));
    //     return newArray;
    // }

    public static String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            System.err.println("Erro ao formatar data '" + date + "': " + e.getMessage());
            return date;
        }
    }
}
