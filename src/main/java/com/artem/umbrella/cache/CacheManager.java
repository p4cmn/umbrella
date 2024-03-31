package com.artem.umbrella.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CacheManager {

    @Value("${cache.limit}")
    private int limit;
    private final Map<String, Object> cache =
            new LinkedHashMap<String, Object>(limit + 1, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(final Map.Entry<String, Object> eldest) {
                    return size() > limit;
                }
            };

    public synchronized void put(final Class<?> clazz, final Long id, final Object value) {
        String key = generateKey(clazz, id);
        cache.put(key, value);
    }

    public synchronized Object get(final Class<?> clazz, final Long id) {
        String key = generateKey(clazz, id);
        return cache.get(key);
    }

    public synchronized void remove(final Class<?> clazz, final Long id) {
        String key = generateKey(clazz, id);
        cache.remove(key);
    }

    public synchronized void update(final Class<?> clazz, final Long id, final Object value) {
        remove(clazz, id);
        put(clazz, id, value);
    }

    private String generateKey(final Class<?> clazz, final Long id) {
        return clazz.getSimpleName() + "-" + id;
    }
}
