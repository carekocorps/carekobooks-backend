package br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFactory {

    private MultipartFileFactory() {
    }

    public static MultipartFile validFile() {
        return new MockMultipartFile("file", "test.png", "image/png", "dummy content".getBytes());
    }

}
