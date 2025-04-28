package br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/")
public class RootController implements BaseController {

    @GetMapping
    public String root() {
        return "CarekoBooks";
    }

}
