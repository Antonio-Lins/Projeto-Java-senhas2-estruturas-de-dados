package com.example.datastructures;

/**
 * Uma implementação de uma Lista Encadeada Simples personalizada.
 * Cada elemento é armazenado em um "nó" que aponta para o próximo nó na sequência.
 * Substitui a funcionalidade de LinkedList para operações eficientes de inserção/remoção
 * em certas posições (uma vez que a posição é encontrada).
 *
 * @param <T> O tipo de elementos que a lista encadeada armazenará.
 */
public class MySinglyLinkedList<T> implements java.lang.Iterable<T> { // CORREÇÃO: Implementa Iterable para permitir o for-each
    private Node<T> head; // O primeiro nó da lista
    private int size;     // O número atual de elementos na lista

    /**
     * Classe interna que representa um nó na lista encadeada.
     * Cada nó contém um elemento e uma referência para o próximo nó.
     *
     * @param <T> O tipo de elemento armazenado no nó.
     */
    private static class Node<T> {
        T data;    // O dado armazenado neste nó
        Node<T> next; // Referência para o próximo nó na lista

        /**
         * Construtor para um novo nó.
         *
         * @param data O dado a ser armazenado no nó.
         */
        public Node(T data) {
            this.data = data;
            this.next = null; // Por padrão, o próximo nó é nulo
        }
    }

    /**
     * Construtor para MySinglyLinkedList.
     * Inicializa uma lista encadeada vazia.
     */
    public MySinglyLinkedList() {
        this.head = null; // A lista começa vazia, então a cabeça é nula
        this.size = 0;    // O tamanho inicial é zero
    }

    /**
     * Adiciona um elemento ao final da lista encadeada.
     *
     * @param element O elemento a ser adicionado.
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(element); // Cria um novo nó com o elemento
        // Se a lista estiver vazia, o novo nó se torna a cabeça
        if (head == null) {
            head = newNode;
        } else {
            // Caso contrário, percorre a lista até o último nó e adiciona o novo nó
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++; // Incrementa o tamanho da lista
    }

    /**
     * Retorna o elemento na posição especificada.
     *
     * @param index O índice do elemento a ser retornado.
     * @return O elemento na posição especificada.
     * @throws IndexOutOfBoundsException se o índice estiver fora dos limites (index < 0 ou index >= size).
     */
    public T get(int index) {
        // Verifica se o índice está dentro dos limites válidos
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fora dos limites: " + index + ", Tamanho: " + size);
        }
        Node<T> current = head;
        // Percorre a lista até o índice desejado
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data; // Retorna os dados do nó no índice especificado
    }

    /**
     * Remove o elemento na posição especificada.
     *
     * @param index O índice do elemento a ser removido.
     * @throws IndexOutOfBoundsException se o índice estiver fora dos limites (index < 0 ou index >= size).
     */
    public void remove(int index) {
        // Verifica se o índice está dentro dos limites válidos
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fora dos limites: " + index + ", Tamanho: " + size);
        }
        // Se o elemento a ser removido for a cabeça
        if (index == 0) {
            head = head.next; // A cabeça passa a ser o próximo nó
        } else {
            // Caso contrário, encontra o nó anterior ao nó a ser removido
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next; // Ignora o nó a ser removido
        }
        size--; // Decrementa o tamanho da lista
    }

    /**
     * Retorna o número de elementos atualmente na lista encadeada.
     *
     * @return O número de elementos na lista.
     */
    public int size() {
        return size;
    }

    /**
     * Verifica se a lista encadeada está vazia.
     *
     * @return true se a lista não contiver elementos, false caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Retorna um iterador para os elementos desta lista encadeada.
     *
     * @return Um iterador para os elementos desta lista.
     */
    @Override // Anotação para indicar que este método sobrescreve um método da interface Iterable
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            private Node<T> currentNode = head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                T data = currentNode.data;
                currentNode = currentNode.next;
                return data;
            }
        };
    }
}
