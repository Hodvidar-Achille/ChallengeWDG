package com.hodvidar.wdg.exception;

public class BalanceTooLowException extends Exception {

    public BalanceTooLowException(final String errorMessage) {
        super(errorMessage);
    }
}
