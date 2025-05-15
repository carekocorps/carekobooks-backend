package br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/")
public class LoginStatusController implements BaseController {

    @GetMapping("/login/success")
    public ResponseEntity<Void> success() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/failure")
    public ResponseEntity<Void> failure() {
        return ResponseEntity.ok().build();
    }

}
