package br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.annotation.aspect;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth.AuthException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.annotation.AuthenticatedUserMatchRequired;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.IntStream;

@Aspect
@Component
public class AuthenticatedUserMatchRequiredAspect {

    @Around("@annotation(authenticatedUserMatchRequired)")
    public Object checkAuthenticatedUser(ProceedingJoinPoint joinPoint, AuthenticatedUserMatchRequired authenticatedUserMatchRequired) throws Throwable {
        var paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        var args = joinPoint.getArgs();

        IntStream.range(0, paramNames.length)
                .filter(i -> authenticatedUserMatchRequired.target().equals(paramNames[i]))
                .mapToObj(i -> args[i])
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(arg -> {
                    try {
                        var argClass = arg.getClass();
                        var username = arg instanceof String
                                ? String.valueOf(arg)
                                : (String) argClass.getMethod("getUsername").invoke(arg);

                        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(username)) {
                            throw authenticatedUserMatchRequired.exception().getDeclaredConstructor().newInstance();
                        }
                    } catch (Exception e) {
                        throw new AuthException(e.getMessage());
                    }
                });

        return joinPoint.proceed();
    }

}
