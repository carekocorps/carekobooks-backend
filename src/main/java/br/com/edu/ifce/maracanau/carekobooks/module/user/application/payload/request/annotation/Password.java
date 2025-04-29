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
@Size(min = 5, max = 255)
@Pattern(regexp = "^\\S+$", message = "Password should not contain spaces")
@Schema(example = "string")
public @interface Password {
}
