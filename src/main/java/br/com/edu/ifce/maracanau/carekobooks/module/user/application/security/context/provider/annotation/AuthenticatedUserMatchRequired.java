package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUserMatchRequired {

    String target();
    Class<? extends RuntimeException> exception();

}
