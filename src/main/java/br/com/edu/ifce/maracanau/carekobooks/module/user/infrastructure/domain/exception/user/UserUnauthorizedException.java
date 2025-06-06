package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user;

public class UserUnauthorizedException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "User is not authenticated";

    public UserUnauthorizedException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
