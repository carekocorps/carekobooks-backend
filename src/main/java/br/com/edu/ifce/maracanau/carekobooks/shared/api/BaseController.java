package br.com.edu.ifce.maracanau.carekobooks.shared.api;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public interface BaseController {

    default URI getHeaderLocation(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

}
