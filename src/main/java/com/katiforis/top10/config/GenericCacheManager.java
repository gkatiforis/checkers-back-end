package com.katiforis.top10.config;

import com.katiforis.top10.DTO.game.GameStateDTO;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.Serializable;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;

@Component
public abstract class GenericCacheManager<K extends String, T extends Serializable> {

    private CacheManager cacheManager;

    protected Cache<String, GameStateDTO> cache;

    @PostConstruct
    protected void init(){
         cacheManager = newCacheManagerBuilder()
                .withCache("game1",
                        newCacheConfigurationBuilder(String.class, GameStateDTO.class, heap(1000).offheap(100, MB)))
                .build(true);
        cache = cacheManager.getCache("game1", String.class, GameStateDTO.class);
    }

    @PreDestroy
    protected void close() {
        cacheManager.close();
    }

    protected void saveItem(String key, GameStateDTO itemToSave){
        cache.put(key, itemToSave);
    }

    protected void removeItem(String key){
        cache.remove(key);
    }

    protected GameStateDTO getItem(String itemKey){

        return cache.get(itemKey);
    }
}
