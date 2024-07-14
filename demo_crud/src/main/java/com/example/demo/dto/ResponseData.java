package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ResponseData <T>{
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
