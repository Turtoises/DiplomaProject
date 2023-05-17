package by.tms.tmsmyproject.controllers.handler;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.Files;
import java.nio.file.Path;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpClientErrorException.BadRequest.class})
    public String handleError405() {
        return "error/errors";
    }
}
