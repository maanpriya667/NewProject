package com.test.demo.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {

    private final Integer status;

    private final String message;

    private final String details;

    private final T response;

    private Map<String,String> errors;


    public ResponseModel(final HttpStatus httpStatus, final String message, final String details, final T response) {
        this(Integer.valueOf(httpStatus.value()), message, details, response);
    }

    public ResponseModel(final Integer httpStatus, final String message, final String details, final T response) {
        this.status = httpStatus;
        this.message = message;
        this.details = details;
        this.response = response;
    }


    public ResponseModel(final HttpStatus httpStatus, final String message, final String details, final T response, final Map<String,String> errors) {
        this(Integer.valueOf(httpStatus.value()), message, details, response);
        this.errors = errors;
    }

    public ResponseModel(final Integer httpStatus, final String message, final String details, final T response, final Map<String,String> errors) {
        this(httpStatus, message, details, response);
        this.errors = errors;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public T getResponse() {
        return response;
    }

    public Map<String,String> getErrors() {
        return errors;
    }
}
