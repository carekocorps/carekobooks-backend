package br.com.edu.ifce.maracanau.carekobooks.common.annotation;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Tag("unit")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnitTest {
}
