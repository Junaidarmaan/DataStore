package com.junnu.redis.KVStore.KVServices;

public class KVEntry {

    private String key;
    private String value;
    private Long ttl;
    private Long lastAccessed;

    public KVEntry() {
    }

    public KVEntry(String key, String value,  Long expiry) {
        this.key = key;
        this.value = value;
        if(expiry != null) {
            Long expiryInMillis = expiry * 60 * 1000;
            this.ttl = System.currentTimeMillis() + expiryInMillis;
        }else{
            ttl = null;
        }
        this.lastAccessed = System.currentTimeMillis();
    }
    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(Long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }
}
