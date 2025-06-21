package br.com.edu.ifce.maracanau.carekobooks.common.factory.common.layer.api.controller.form;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MultipartFormDataRequestFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private MultipartFormDataRequestFactory() {
    }

    public static <T extends BaseRequest> MultiValueMap<String, Object> validRequestWithImage(T request, MultipartFile image) throws IOException {
        var jsonPayload = objectMapper.writeValueAsString(request);
        var jsonPartHeaders = new HttpHeaders();
        jsonPartHeaders.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        multipartBody.add("request", new HttpEntity<>(jsonPayload, jsonPartHeaders));
        if (image != null && !image.isEmpty()) {
            multipartBody.add("image", createImagePart(image));
        }

        return multipartBody;
    }

    public static MultiValueMap<String, Object> validRequestWithImage(MultipartFile image) throws IOException {
        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        if (image != null && !image.isEmpty()) {
            multipartBody.add("image", createImagePart(image));
        }

        return multipartBody;
    }

    private static HttpEntity<ByteArrayResource> createImagePart(MultipartFile image) throws IOException {
        var resource = new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };

        return new HttpEntity<>(resource);
    }

}
