package com.junnu.redis.KVStore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junnu.redis.KVStore.KVServices.KVStore;
import com.junnu.redis.KVStore.KVServices.KVEntry;
import com.junnu.redis.KVStore.dto.KVStoreRequest;

@RequestMapping("/keyvalue")
@Controller
public class Home {
    
    @Autowired
    KVStore storeService;

    @GetMapping("/connection")
    ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the Key-Value Store API!");
    }

    @PostMapping
    ResponseEntity<String> add(@RequestBody KVStoreRequest request) {
        storeService.add(request);
        return ResponseEntity.ok("data added to store.");   
    }

    @GetMapping("/{key}")
    ResponseEntity<?> get(@PathVariable String key) {
        KVEntry entry = storeService.get(key);

        if (entry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("key not found.");
        }

        return ResponseEntity.ok(entry);
    }

    @GetMapping("/{key}/exists")
    ResponseEntity<String> exists(@PathVariable String key) {
        return ResponseEntity.ok(Boolean.toString(storeService.exists(key)));
    }

    @DeleteMapping("/{key}")
    ResponseEntity<String> delete(@PathVariable String key) {
        boolean deleted = storeService.delete(key);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("key not found.");
        }

        return ResponseEntity.ok("key deleted.");
    }
}
