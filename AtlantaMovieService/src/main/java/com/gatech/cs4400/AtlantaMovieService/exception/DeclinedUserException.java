package com.gatech.cs4400.AtlantaMovieService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DeclinedUserException extends RuntimeException {

    public DeclinedUserException(String message) {
        super(message);
    }

    public DeclinedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
