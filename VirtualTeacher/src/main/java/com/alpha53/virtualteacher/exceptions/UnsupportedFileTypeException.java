package com.alpha53.virtualteacher.exceptions;

public class UnsupportedFileTypeException extends RuntimeException{
    public UnsupportedFileTypeException(String fileType) {
        super(String.format("File type %s not supported.",fileType));
    }
}
