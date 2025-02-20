package com.example.cafelabservice.config

import com.example.cafelabservice.utils.clients.VercelHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class AppConfig(
    private val vercel: VercelConfig,
) {
    @Bean
    fun vercelHttpClient(): VercelHttpClient {
        val client = RestClient.builder().baseUrl(vercel.url).build()
        val factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client)).build()
        return factory.createClient(VercelHttpClient::class.java)
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}