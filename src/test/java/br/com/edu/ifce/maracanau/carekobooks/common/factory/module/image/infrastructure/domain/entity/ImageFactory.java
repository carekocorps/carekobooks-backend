package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import net.datafaker.Faker;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class ImageFactory {

    private static final Faker faker = new Faker();

    private ImageFactory() {
    }

    public static Image validImageWithNullId() {
        var image = new Image();
        var contentType = faker.options().option("jpeg", "png");
        image.setId(null);
        image.setName(faker.file().fileName(null, null, contentType, null));
        image.setContentType("image/" + contentType);
        image.setSizeInBytes(faker.number().randomNumber());
        image.setUsers(List.of());
        image.setBooks(List.of());
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(image.getCreatedAt());
        return image;
    }


    public static Image validImage() {
        var image = validImageWithNullId();
        image.setId(faker.number().randomNumber());
        return image;
    }

    public static Image validImage(MultipartFile file) {
        var image = new Image();
        image.setId(faker.number().randomNumber());
        image.setName(file.getName());
        image.setContentType(file.getContentType());
        image.setSizeInBytes(file.getSize());
        image.setUsers(List.of());
        image.setBooks(List.of());
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(image.getCreatedAt());
        return image;
    }

    public static Image validJpegImage() {
        var image = validImage();
        image.setContentType("image/jpeg");
        return image;
    }

    public static Image validPngImage() {
        var image = validImage();
        image.setContentType("image/png");
        return image;
    }

    public static Image invalidImageByContentType() {
        var image = validImage();
        image.setContentType("image/gif");
        return image;
    }

}
