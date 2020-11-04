package com.banking.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

  @Value("${api.timeout.read}")
  private int readTimeout;

  @Value("${api.timeout.connection}")
  private int connectionTimeout;

  @Autowired private ObjectMapper objectMapper;

  @Bean
  RestTemplate restTemplate() {

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setReadTimeout(readTimeout*1000);
    requestFactory.setConnectTimeout(connectionTimeout*1000);
    return new RestTemplate(requestFactory);

    /*
    final RestTemplate restTemplate = new RestTemplate();

    final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setReadTimeout(readTimeout * 1000); // in m seconds
    factory.setConnectTimeout(connectionTimeout * 1000); // in m seconds
    restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));

    // add mappers
    final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    final MappingJackson2HttpMessageConverter jsonMessageConverter =
        new MappingJackson2HttpMessageConverter();
    jsonMessageConverter.setObjectMapper(objectMapper);
    messageConverters.add(new StringHttpMessageConverter());
    messageConverters.add(new ByteArrayHttpMessageConverter());
    messageConverters.add(new ResourceHttpMessageConverter());
    messageConverters.add(jsonMessageConverter);
    restTemplate.setMessageConverters(messageConverters);

    return restTemplate;*/
  }
}
