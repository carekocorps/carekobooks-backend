package br.com.edu.ifce.maracanau.carekobooks.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public abstract class BaseController {

    protected URI getHeaderLocation(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

}
