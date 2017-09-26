package com.xgame.service.common.exception;

public class CardChargeException extends Exception {
    public CardChargeException(String message) {
        super(message);
    }

    public CardChargeException(String message, Throwable cause) {
        super(message, cause);
    }
}
