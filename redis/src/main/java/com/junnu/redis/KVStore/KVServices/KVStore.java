package com.junnu.redis.KVStore.KVServices;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.junnu.redis.KVStore.dto.KVStoreRequest;

@Service
public class KVStore {
    ConcurrentHashMap<String, KVEntry> store = new ConcurrentHashMap<>();

    public void add(KVStoreRequest request) {
        KVEntry entry = new KVEntry(request.getKey(), request.getValue(), request.getExpiry());
        store.put(request.getKey(), entry);
    }

    public KVEntry get(String key) {
        KVEntry entry = store.get(key);

        if (entry == null) {
            return null;
        }
        entry.setLastAccessed(System.currentTimeMillis());
        if (entry.getTtl() == null) {
            return entry;
        }

        if (System.currentTimeMillis() > entry.getTtl()) {
            store.remove(key);
            return null;
        }

        return entry;
    }

    public boolean exists(String key) {
        return store.containsKey(key);
    }

    public boolean delete(String key) {
        return store.remove(key) != null;
    }
    public void update(KVStoreRequest request) {
        KVEntry entry = store.get(request.getKey());

        if (entry != null) {
            entry.setValue(request.getValue());
            if (request.getExpiry() != null) {
                Long expiryInMillis = request.getExpiry() * 60 * 1000;
                entry.setTtl(System.currentTimeMillis() + expiryInMillis);
            } else {
                entry.setTtl(null);
            }
            entry.setLastAccessed(System.currentTimeMillis());
        }

    }
}
