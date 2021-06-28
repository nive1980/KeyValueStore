package com.kvs.test;

import com.kvs.KeyValueStore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class KVSTest {
    @Test
    public void tesKeyValueStore() {
        System.out.println("Testing key value store");
        KeyValueStore kvs = new KeyValueStore();
        assertEquals("5", kvs.put("key1", "5"));
        assertEquals("6", kvs.put("key2", "6"));
        assertEquals("5", kvs.get("key1"));
        assertEquals("5", kvs.get("key1"));
        assertEquals("6", kvs.get("key2"));
        assertEquals("7", kvs.put("key1", "7"));
        assertEquals("7", kvs.get("key1"));
        assertNull(kvs.get("key4"));
        assertEquals("7", kvs.get("key1"));
    }
}
