package br.com.edu.ifce.maracanau.carekobooks.module.file.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class StoredFile extends BaseModel {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String url;

    public StoredFile(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }
}
