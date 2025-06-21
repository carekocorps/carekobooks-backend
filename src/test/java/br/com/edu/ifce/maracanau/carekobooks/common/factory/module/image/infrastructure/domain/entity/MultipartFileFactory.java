package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity;

import net.datafaker.Faker;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFactory {

    private static final Faker faker = new Faker();

    private MultipartFileFactory() {
    }

    public static MultipartFile validFile() {
        var contentType = "image/png";
        var content = faker.lorem().paragraph().getBytes();
        var name = faker.file().fileName(null, null, "png", null);
        return new MockMultipartFile(name, name, contentType, content);
    }

}
