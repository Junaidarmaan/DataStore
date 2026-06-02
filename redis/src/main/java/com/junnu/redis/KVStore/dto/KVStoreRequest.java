package com.junnu.redis.KVStore.dto;

public class KVStoreRequest {
    private String key;
    private String value;
    private Long expiry;
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
    public Long getExpiry() {
        return expiry;
    }
    public void setExpiry(Long expiry) {    
        this.expiry = expiry;
    }
}
