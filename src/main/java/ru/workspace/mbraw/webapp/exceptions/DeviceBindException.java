package ru.workspace.mbraw.webapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DeviceBindException extends Exception {

    public DeviceBindException() {
        super();
    }

    public DeviceBindException(String message) {
        super(message);
    }
}
