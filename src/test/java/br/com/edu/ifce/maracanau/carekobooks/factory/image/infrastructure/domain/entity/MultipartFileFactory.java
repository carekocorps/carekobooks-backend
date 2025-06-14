package br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity;

import com.github.javafaker.Faker;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFactory {

    private static final Faker faker = new Faker();

    private MultipartFileFactory() {
    }

    public static MultipartFile validFile(String name) {
        var contentType = "image/png";
        var content = faker.lorem().paragraph().getBytes();
        return new MockMultipartFile(name, name, contentType, content);
    }

    public static MultipartFile validFile() {
        return validFile("test.png");
    }

}
