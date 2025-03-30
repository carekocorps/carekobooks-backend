package br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.layer.infrastructure.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Image extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(length = 1024, nullable = false)
    private String url;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

}
