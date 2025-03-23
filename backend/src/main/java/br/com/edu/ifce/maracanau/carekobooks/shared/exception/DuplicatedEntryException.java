package br.com.edu.ifce.maracanau.carekobooks.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedEntryException extends BaseException {

    public DuplicatedEntryException(String message) {
        super(message);
    }

}
