package com.kvs.listener;

/**
 * This is a listener class that needs to be implemented for different
 * states of the keystore
 * @param <K>
 * @param <V>
 */
public interface KeyValueStoreListener<K,V> {
    /**
     * This method needs to be implemented for the action when a key has expired in the keystore
     * @param key
     * @param value
     * @throws Exception
     */
    public void onKeyExpired(K key, V value) throws Exception;
}
