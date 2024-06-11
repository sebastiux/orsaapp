/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Carlos
 */
public class Cache {
    private Map<String, CacheEntry> cache;
    private long expirationTime;

    public Cache(long expirationTime) {
        this.cache = new HashMap<>();
        this.expirationTime = expirationTime;
    }

    public synchronized Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.value;
    }

    public synchronized void put(String key, Object value) {
        cache.put(key, new CacheEntry(value, expirationTime));
    }

    private class CacheEntry {
        private Object value;
        private long expiryTime;

        public CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + expirationTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}
