package com.example.datastructures;

import java.util.Arrays; // Usado apenas para Arrays.copyOf para redimensionamento do array interno

/**
 * Uma implementação de um Array Dinâmico personalizado.
 * Esta estrutura armazena elementos em um array interno e redimensiona-o
 * automaticamente quando a capacidade é excedida.
 * Substitui a funcionalidade de ArrayList para armazenamento dinâmico de coleções.
 *
 * @param <T> O tipo de elementos que o array dinâmico armazenará.
 */
public class MyDynamicArray<T> {
    private Object[] data; // Array interno para armazenar os elementos
    private int size;      // Número atual de elementos no array
    private static final int DEFAULT_CAPACITY = 10; // Capacidade inicial padrão

    /**
     * Construtor para MyDynamicArray com capacidade inicial padrão.
     */
    public MyDynamicArray() {
        this.data = new Object[DEFAULT_CAPACITY]; // Inicializa o array com a capacidade padrão
        this.size = 0; // O array começa vazio
    }

    /**
     * Adiciona um elemento ao final do array dinâmico.
     * Se a capacidade for excedida, o array é redimensionado para o dobro do tamanho.
     *
     * @param element O elemento a ser adicionado.
     */
    public void add(T element) {
        // Se o número de elementos for igual à capacidade, redimensiona o array
        if (size == data.length) {
            resize(); // Chama o método de redimensionamento
        }
        data[size++] = element; // Adiciona o elemento e incrementa o tamanho
    }

    /**
     * Retorna o elemento na posição especificada.
     *
     * @param index O índice do elemento a ser retornado.
     * @return O elemento na posição especificada.
     * @throws IndexOutOfBoundsException se o índice estiver fora dos limites (index < 0 ou index >= size).
     */
    @SuppressWarnings("unchecked") // Supressão para o cast seguro de Object para T
    public T get(int index) {
        // Verifica se o índice está dentro dos limites válidos
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fora dos limites: " + index + ", Tamanho: " + size);
        }
        return (T) data[index]; // Retorna o elemento no índice especificado
    }

    /**
     * Define o elemento na posição especificada.
     *
     * @param index O índice do elemento a ser definido.
     * @param element O novo elemento a ser armazenado na posição especificada.
     * @throws IndexOutOfBoundsException se o índice estiver fora dos limites (index < 0 ou index >= size).
     */
    public void set(int index, T element) { // CORREÇÃO: Novo método set para modificar elementos
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fora dos limites para set: " + index + ", Tamanho: " + size);
        }
        data[index] = element;
    }

    /**
     * Remove o elemento na posição especificada.
     * Os elementos subsequentes são deslocados para a esquerda.
     *
     * @param index O índice do elemento a ser removido.
     * @throws IndexOutOfBoundsException se o índice estiver fora dos limites (index < 0 ou index >= size).
     */
    public void remove(int index) {
        // Verifica se o índice está dentro dos limites válidos
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fora dos limites: " + index + ", Tamanho: " + size);
        }
        // Desloca os elementos à direita do índice para a esquerda
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null; // Define o último elemento como null e decrementa o tamanho
    }

    /**
     * Retorna o número de elementos atualmente no array dinâmico.
     *
     * @return O número de elementos no array.
     */
    public int size() {
        return size;
    }

    /**
     * Verifica se o array dinâmico está vazio.
     *
     * @return true se o array não contiver elementos, false caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna uma representação em array dos elementos contidos nesta lista.
     * O array retornado será uma nova instância.
     *
     * @return Um array contendo todos os elementos nesta lista na ordem correta.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        // Retorna uma cópia do array interno contendo apenas os elementos válidos.
        return (T[]) Arrays.copyOf(data, size);
    }

    /**
     * Redimensiona o array interno para o dobro da capacidade atual.
     * Os elementos existentes são copiados para o novo array.
     */
    private void resize() {
        int newCapacity = data.length * 2; // Dobra a capacidade
        data = Arrays.copyOf(data, newCapacity); // Cria um novo array e copia os elementos
    }
}
