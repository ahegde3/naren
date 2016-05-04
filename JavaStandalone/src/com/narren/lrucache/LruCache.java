package com.narren.lrucache;

import java.util.LinkedHashMap;


public class LruCache<K,V> {

    private LinkedHashMap<K, V> map;
    private int maxSize;
    
    public LruCache(int size) {
        if(size <= 0) {
            throw new IllegalArgumentException("size <= 0!!");
        }
        this.maxSize = size;
        map = new LinkedHashMap<K,V>(maxSize, 0.75f, true);
    }
    
    public V get(K key) {
        if(key == null) {
            throw new NullPointerException("key == null !!!");
        }
        V value = null;
        synchronized (this) {
            value = map.get(key);
        }
        return value;
    }
    
    public void put(K k, V v) {
        if(k == null || v == null) {
            throw new IllegalArgumentException();
        }
        synchronized (this) {
            map.put(k, v);
        }
    }
}
