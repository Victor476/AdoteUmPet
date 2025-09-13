package com.adoteumpet.adoteumpetapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração do RestTemplate para consumir APIs externas
 * (TheDogAPI e TheCatAPI)
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Bean do RestTemplate com timeouts configurados
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    /**
     * Configuração de timeouts para as chamadas HTTP
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // Timeout de conexão: 10 segundos
        factory.setConnectTimeout(10000);
        
        // Timeout de leitura: 15 segundos
        factory.setReadTimeout(15000);
        
        return factory;
    }
}