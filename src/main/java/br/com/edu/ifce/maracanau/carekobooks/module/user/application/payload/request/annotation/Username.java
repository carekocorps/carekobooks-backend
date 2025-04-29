package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.annotation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank
@Size(max = 50)
@Pattern(regexp = "^[a-z_0-9]+$", message = "Username should only contain letters and numbers")
@Schema(example = "string")
public @interface Username {
}
