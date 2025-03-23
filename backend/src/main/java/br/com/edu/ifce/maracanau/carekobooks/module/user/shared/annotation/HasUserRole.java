package br.com.edu.ifce.maracanau.carekobooks.module.user.shared.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ROLE_' + T(br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole).USER.name())")
public @interface HasUserRole {
}
