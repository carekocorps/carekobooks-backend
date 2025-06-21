package br.com.edu.ifce.maracanau.carekobooks.integration.common.provider;

import br.com.edu.ifce.maracanau.carekobooks.integration.common.payload.response.MailhogResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Component
public class MailhogProvider {

    private static final RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplate();
        var converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.valueOf("text/json")));
        restTemplate.getMessageConverters().addFirst(converter);
    }

    @Value("${mailhog.host:localhost}")
    private String host;

    @Value("${mailhog.port.http}")
    private Integer httpPort;

    public Integer getTotalMessages() {
        var uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(httpPort)
                .path("/api/v2/messages")
                .build()
                .toUriString();

        var response = restTemplate.getForObject(uri, MailhogResponse.class);
        return response != null
                ? response.getTotal()
                : null;
    }

    public void tearDown() {
        var uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(httpPort)
                .path("/api/v1/messages")
                .build()
                .toUriString();

        restTemplate.delete(uri);
    }

}
