package com.kvs;

import com.kvs.listener.KeyValueStoreFileWriter;
import com.kvs.listener.KeyValueStoreListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Time window based key value store.
 * This keystore doesnt permit null keys and null values
 * @param <K>
 * @param <V>
 */

public class KeyValueStore<K, V> extends ConcurrentHashMap<K, V> {
    ConcurrentHashMap<K, TreeMap<Long, V>> ksTimeMap;
    private long expiryTime = 1000;
    private KeyValueStoreListener<K, V> listener;

    /**
     *
     * Constructor to initialize the Key Value Store.
     * It initializes the Time Map which holds the key against
     * a treemap of timestamps.
     * It also starts a Evictor thread
    **/
    public KeyValueStore() {
        ksTimeMap = new ConcurrentHashMap<K, TreeMap<Long, V>>();
        listener = new KeyValueStoreFileWriter();
       new EvictorThread().start();
    }

    /**
     * This method sets the listener
     * @param listener
     */
    public void setListener(KeyValueStoreListener<K, V> listener) {
        this.listener = listener;

    }

    /**
     * This method sets the time in seconds after which the keys would expire
     * @param seconds
     */
    public void setKeyExpiryInSeconds(long seconds) {
        if(seconds >= 0) {
            expiryTime = TimeUnit.SECONDS.toMillis(seconds);
        }
    }

    /**
     * This method sets the time in minutes after which the keys would expire
     * @param minutes
     */
    public void setKeyExpiryInMinutes(long minutes) {
        if(minutes >= 0) {
            expiryTime = TimeUnit.MINUTES.toMillis(minutes);
        }
    }

    /**
     * This method sets the time in hours after which the keys would expire
     * @param hours
     */
    public void setKeyExpiryInHours(long hours) {
        if(hours >= 0) {
            expiryTime = TimeUnit.HOURS.toMillis(hours);
        }
    }

    /**
     * method to put a key against a value in the key store.
     * The treemap ksTimeMap stores map of timestamp vs values.
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {
        if (key == null || value == null) throw new NullPointerException();
        long timestamp = System.currentTimeMillis();
        if(!ksTimeMap.containsKey(key)) {
            ksTimeMap.put(key, new TreeMap());
        }
        (ksTimeMap.get(key)).put(new Long(timestamp),value);
        return value;
    }

    /**
     * This method is used to return the latest value for the key.
     * This value is obtained from the treemap
     * @param key
     * @return
     */
    @Override
    public V get(Object key) {
        if (key == null) throw new NullPointerException();
        if(!ksTimeMap.containsKey(key)) {
            return null;
        }
        TreeMap<Long, V> treeMap = ksTimeMap.get(key);
        V value = treeMap.lastEntry().getValue();
        return value;
    }

    /**
     * This method is used to obtain the average of all the non expired keys' values
     * @return
     */
    public synchronized float average() {
        Long sum = 0l;
        if (!ksTimeMap.isEmpty()) {
            int size = ksTimeMap.size();
            for (K k : ksTimeMap.keySet()) {
                TreeMap<Long, V> tm = ksTimeMap.get(k);
                sum = sum +(Long) (tm.lastEntry().getValue());
            }
            float avg = sum / size;
            return avg;
        }
        return 0l;
    }

    /**
     * This is the evictor class which removes a key once it has expired.
     */
    class EvictorThread extends Thread {
        @Override
        public void run() {
            System.out.println("Initiating ");
            while (true) {
                try {
                    cleanMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(expiryTime / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cleanMap() throws Exception {
            long currentTime = new Date().getTime();
            for (K key : ksTimeMap.keySet()) {
                TreeMap<Long, V> tm = ksTimeMap.get(key);
                long lastTime = tm.lastEntry().getKey();
                V valueRemoved = tm.lastEntry().getValue();
                if (currentTime > (lastTime + expiryTime)) {
                    V value = remove(key);
                    ksTimeMap.remove(key);
                    System.out.println("expired"+key+" val "+valueRemoved);
                    if (listener != null) {
                        listener.onKeyExpired(key, valueRemoved);
                    }
                }
            }
        }
    }
}
