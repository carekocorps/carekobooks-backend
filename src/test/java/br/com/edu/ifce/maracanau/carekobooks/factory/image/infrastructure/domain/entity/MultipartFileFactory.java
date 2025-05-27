package br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFactory {

    private MultipartFileFactory() {
    }

    public static MultipartFile validFile(String name) {
        return new MockMultipartFile(name, "test.png", "image/png", "dummy content".getBytes());
    }

    public static MultipartFile validFile() {
        return validFile("test.png");
    }

}
