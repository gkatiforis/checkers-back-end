package com.katiforis.checkers.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.Serializable;
import java.util.HashMap;

import java.util.Map;

import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;

@Slf4j
@Component
public abstract class GenericCacheManager<K extends String, T extends Serializable> {

    protected CacheManager cacheManager;
    protected Map<String, Cache<K, T>> caches = new HashMap<>();

    @PostConstruct
    protected void init(){
        log.debug("Start GenericCacheManager.saveItem");
        cacheManager = newCacheManagerBuilder().build(true);
        log.debug("Start GenericCacheManager.saveItem");
    }

    @PreDestroy
    protected void close() {
        log.debug("Start GenericCacheManager.saveItem");
        cacheManager.close();
        log.debug("Start GenericCacheManager.saveItem");
    }


    protected void saveItem(K key, T itemToSave){
        log.debug("Start GenericCacheManager.saveItem");
        Cache<K, T> cache = caches.get(key);
        cache.put(key, itemToSave);
        log.debug("Start GenericCacheManager.saveItem");
    }

    protected void removeItem(K key){
        log.debug("Start GenericCacheManager.removeItem");
        caches.get(key).remove(key);
        cacheManager.removeCache(key);
        caches.remove(key);
        log.debug("Start GenericCacheManager.removeItem");
    }

    protected T getItem(K itemKey){
        log.debug("Start GenericCacheManager.getItem");
        if( caches.get(itemKey) == null) return null;
        log.debug("Start GenericCacheManager.getItem");
        return caches.get(itemKey).get(itemKey);
    }

    protected Cache<K, T> getCache(K key){
        log.debug("Start GenericCacheManager.getCache");
        return caches.get(key);
    }
}
