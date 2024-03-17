package com.artem.umbrella.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheManager {

    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    public void put(Class<?> clazz, Long id, Object value) {
        String key = generateKey(clazz, id);
        cache.put(key, value);
    }

    public Object get(Class<?> clazz, Long id) {
        String key = generateKey(clazz, id);
        return cache.get(key);
    }

    public void remove(Class<?> clazz, Long id) {
        String key = generateKey(clazz, id);
        cache.remove(key);
    }

    private String generateKey(Class<?> clazz, Long id) {
        return clazz.getSimpleName() + "-" + id;
    }
}
