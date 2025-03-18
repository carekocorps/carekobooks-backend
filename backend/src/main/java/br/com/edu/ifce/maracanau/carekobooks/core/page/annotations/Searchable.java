package br.com.edu.ifce.maracanau.carekobooks.core.page.annotations;

import br.com.edu.ifce.maracanau.carekobooks.core.page.enums.SearchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Searchable {

    SearchType type() default SearchType.VALUE_EQUALS;

}
