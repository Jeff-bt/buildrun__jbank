package dev.jeff.jbank.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExpectionHandler {

    @ExceptionHandler(JBankException.class)
    public ProblemDetail handleJBankException(JBankException e) {
        return e.toProblemDetail();
    }
}