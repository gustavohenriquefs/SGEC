package com.casaculturaqxd.sgec.service;

public class ServiceOperationException extends Exception {

    public ServiceOperationException(String message) {
        super(message);
    }

    public ServiceOperationException(Throwable error) {
        super(error);
    }

    public ServiceOperationException(String message, Throwable error) {
        super(message, error);
    }
}
