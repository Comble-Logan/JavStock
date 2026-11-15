package com.logistics.exception;

import java.io.IOException;

public class SavingCsvException extends IOException {
    public SavingCsvException(String message) {
        super(message);
    }
}
