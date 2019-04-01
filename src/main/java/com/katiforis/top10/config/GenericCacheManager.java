package com.katiforis.top10.config;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.Serializable;
import java.util.HashMap;

import java.util.Map;

import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;

@Component
public abstract class GenericCacheManager<K extends String, T extends Serializable> {

    protected CacheManager cacheManager;
    protected Map<String, Cache<K, T>> caches = new HashMap<>();

    @PostConstruct
    protected void init(){
        cacheManager = newCacheManagerBuilder().build(true);
    }

    @PreDestroy
    protected void close() {
        cacheManager.close();
    }


    protected void saveItem(K key, T itemToSave){
        Cache<K, T> cache = caches.get(key);
        cache.put(key, itemToSave);
    }

    protected void removeItem(K key){
        caches.get(key).remove(key);
    }

    protected T getItem(K itemKey){
        if( caches.get(itemKey) == null) return null;
        return caches.get(itemKey).get(itemKey);
    }

    protected Cache<K, T> getCache(K key){
        return caches.get(key);
    }
}
