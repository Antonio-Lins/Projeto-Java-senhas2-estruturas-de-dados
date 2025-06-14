package com.example.datastructures;

/**
 * Uma implementação de uma Tabela Hash (Mapa) personalizada.
 * Mapeia chaves para valores, permitindo recuperação eficiente de valores
 * com base em suas chaves.
 * Usa MySinglyLinkedList para lidar com colisões (encadeamento separado).
 * Substitui a funcionalidade de HashMap para mapeamento chave-valor.
 *
 * @param <K> O tipo da chave.
 * @param <V> O tipo do valor.
 */
public class MyHashMap<K, V> {
    // Usamos MySinglyLinkedList para as buckets para evitar List nativas
    private MySinglyLinkedList<Entry<K, V>>[] buckets;
    private int size; // Número total de pares chave-valor no mapa
    private static final int DEFAULT_CAPACITY = 16; // Capacidade inicial padrão de buckets

    /**
     * Classe interna que representa uma entrada (par chave-valor) na Tabela Hash.
     *
     * @param <K> O tipo da chave.
     * @param <V> O tipo do valor.
     */
    private static class Entry<K, V> {
        K key;   // A chave da entrada
        V value; // O valor associado à chave

        /**
         * Construtor para uma nova entrada.
         *
         * @param key   A chave.
         * @param value O valor.
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return key.equals(entry.key); // Compara entradas com base em suas chaves
        }

        @Override
        public int hashCode() {
            return key.hashCode(); // O hash da entrada é o hash da chave
        }
    }

    /**
     * Construtor para MyHashMap com capacidade inicial padrão.
     */
    @SuppressWarnings("unchecked") // Cast seguro para array de MySinglyLinkedList
    public MyHashMap() {
        // Inicializa o array de buckets. Cada bucket é uma MySinglyLinkedList.
        this.buckets = (MySinglyLinkedList<Entry<K, V>>[]) new MySinglyLinkedList[DEFAULT_CAPACITY];
        // Inicializa cada MySinglyLinkedList em cada bucket
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            buckets[i] = new MySinglyLinkedList<>();
        }
        this.size = 0; // O mapa começa vazio
    }

    /**
     * Calcula o índice do bucket para uma dada chave.
     *
     * @param key A chave.
     * @return O índice do bucket correspondente.
     */
    private int getBucketIndex(K key) {
        // Garante que o hash não seja negativo e esteja dentro dos limites da capacidade.
        return Math.abs(key.hashCode()) % buckets.length;
    }

    /**
     * Associa o valor especificado à chave especificada neste mapa.
     * Se o mapa já contiver um mapeamento para a chave, o valor antigo é substituído.
     *
     * @param key   A chave com a qual o valor especificado deve ser associado.
     * @param value O valor a ser associado à chave especificada.
     */
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key); // Obtém o índice do bucket
        MySinglyLinkedList<Entry<K, V>> bucket = buckets[bucketIndex]; // Obtém o bucket
        
        // Verifica se a chave já existe no bucket
        for (Entry<K, V> entry : bucket) { // Utiliza o iterator da MySinglyLinkedList
            if (entry.key.equals(key)) {
                entry.value = value; // Atualiza o valor se a chave já existe
                return;
            }
        }
        // Se a chave não existe, adiciona uma nova entrada ao bucket
        bucket.add(new Entry<>(key, value));
        size++; // Incrementa o tamanho total do mapa
    }

    /**
     * Retorna o valor ao qual a chave especificada está mapeada,
     * ou null se este mapa não contiver um mapeamento para a chave.
     *
     * @param key A chave cujo valor associado deve ser retornado.
     * @return O valor ao qual a chave especificada está mapeada, ou null.
     */
    public V get(K key) {
        int bucketIndex = getBucketIndex(key); // Obtém o índice do bucket
        MySinglyLinkedList<Entry<K, V>> bucket = buckets[bucketIndex]; // Obtém o bucket
        
        // Percorre o bucket procurando pela chave
        for (Entry<K, V> entry : bucket) { // Utiliza o iterator da MySinglyLinkedList
            if (entry.key.equals(key)) {
                return entry.value; // Retorna o valor se a chave for encontrada
            }
        }
        return null; // Retorna null se a chave não for encontrada
    }

    /**
     * Retorna true se este mapa contiver um mapeamento para a chave especificada.
     *
     * @param key A chave cuja presença neste mapa deve ser testada.
     * @return true se este mapa contiver um mapeamento para a chave especificada.
     */
    public boolean containsKey(K key) {
        int bucketIndex = getBucketIndex(key); // Obtém o índice do bucket
        MySinglyLinkedList<Entry<K, V>> bucket = buckets[bucketIndex]; // Obtém o bucket
        
        // Percorre o bucket procurando pela chave
        for (Entry<K, V> entry : bucket) { // Utiliza o iterator da MySinglyLinkedList
            if (entry.key.equals(key)) {
                return true; // Retorna true se a chave for encontrada
            }
        }
        return false; // Retorna false se a chave não for encontrada
    }

    /**
     * Retorna o número de mapeamentos chave-valor neste mapa.
     *
     * @return O número de mapeamentos chave-valor neste mapa.
     */
    public int size() {
        return size;
    }

    /**
     * Retorna uma MyDynamicArray contendo todas as chaves contidas neste mapa.
     *
     * @return Uma MyDynamicArray de chaves.
     */
    public MyDynamicArray<K> keySet() {
        MyDynamicArray<K> keys = new MyDynamicArray<>();
        // Percorre todos os buckets e adiciona as chaves à MyDynamicArray
        for (MySinglyLinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) { // Utiliza o iterator da MySinglyLinkedList
                keys.add(entry.key);
            }
        }
        return keys;
    }
}
