package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404,"Member not found"),
    MEMBER_EXISTS(409,"Member exists"),
    COFFEE_NOT_FOUND(404,"Coffee not found"),
    COFFEE_EXISTS(409,"Coffee exists"),
    ORDER_NOT_FOUND(404,"order not found"),
    ORDER_EXISTS(409,"order exists");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int statusCode, String message){
        this.message = message;
        this.status = statusCode;
    }
}
