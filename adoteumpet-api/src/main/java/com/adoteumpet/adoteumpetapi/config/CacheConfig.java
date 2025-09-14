package com.adoteumpet.adoteumpetapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuração do Cache usando Caffeine
 * 
 * Configura cache para o endpoint /breeds/:species com:
 * - TTL de 10 minutos (dados de raças são relativamente estáticos)
 * - Máximo 1000 entradas (suficiente para combinar especies + filtros)
 * - Métricas habilitadas para monitoramento
 */
@Configuration
public class CacheConfig {

    /**
     * Configura o CacheManager com Caffeine
     * 
     * @return CacheManager configurado com TTL de 10 minutos e máximo 1000 entradas
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineCacheBuilder());
        cacheManager.setCacheNames(java.util.Arrays.asList("breeds")); // Define os nomes dos caches disponíveis
        return cacheManager;
    }

    /**
     * Configura o builder do Caffeine com as especificações de cache
     * 
     * @return Caffeine builder configurado
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                // TTL: Cache expira após 10 minutos de escrita
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // Tamanho máximo: 1000 entradas (species + filtros combinados)
                .maximumSize(1000)
                // Habilita estatísticas para monitoramento
                .recordStats();
    }
}