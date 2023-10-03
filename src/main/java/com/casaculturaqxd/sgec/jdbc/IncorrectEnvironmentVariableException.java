package com.casaculturaqxd.sgec.jdbc;

public class IncorrectEnvironmentVariableException extends Exception{
    IncorrectEnvironmentVariableException(String errorMsg){
        super(errorMsg);
    }
    IncorrectEnvironmentVariableException(){
        super("variavel de ambiente nao encontrada");
    }
}
