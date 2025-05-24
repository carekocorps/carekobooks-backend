package br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ImageFactory {

    private ImageFactory() {
    }

    public static Image validImage(MultipartFile file) {
        var image = new Image();
        image.setId(new Random().nextLong());
        image.setName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSizeInBytes(file.getSize());
        image.setUsers(List.of());
        image.setBooks(List.of());
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(image.getCreatedAt());
        return image;
    }

    public static Image validImage() {
        var random = new Random();
        var contentTypes = List.of("jpeg", "png");
        var contentType = contentTypes.get(random.nextInt(contentTypes.size()));

        var image = new Image();
        image.setId(random.nextLong());
        image.setName(UUID.randomUUID() + "." + contentType);
        image.setContentType("image/" + contentType);
        image.setSizeInBytes(random.nextLong());
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
