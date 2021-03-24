package com.google.drive.api.exception;


import com.google.drive.api.dto.response.ErrorResponse;
import com.google.drive.api.dto.response.ValidationErrorDTO;
import com.google.drive.api.service.impl.FileServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class RestControllerExceptionHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RestControllerExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex, Locale locale) {
        List<ValidationErrorDTO> details = new ArrayList<>();
        details.add(new ValidationErrorDTO(ex.getLocalizedMessage()));
        //   String errorMessage = messageSource.getMessage(ExceptionMessages.SOMETHING_WENT_WRONG, null, locale);
        ErrorResponse error = new ErrorResponse("Something went wrong", details);
        logger.error("\n status: " + HttpStatus.BAD_REQUEST + "\n message :" + Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Locale locale) {
        List<ValidationErrorDTO> listErrors = new ArrayList<>();
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getDefaultMessage());
            listErrors.add(new ValidationErrorDTO(error.getField(), error.getDefaultMessage()));
        }

        //   String errorMessage = messageSource.getMessage(ExceptionMessages.FAIL_VALIDATION, null, locale);
        ErrorResponse error = new ErrorResponse("Validation fail", listErrors);

        logger.error("\n status: " + HttpStatus.BAD_REQUEST + "\n message :" + Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
