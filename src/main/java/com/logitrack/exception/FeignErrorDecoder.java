package com.logitrack.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Feign error for method {} : Code HTTP {}", methodKey, response.status());

        switch (response.status()) {
            case 400:
                log.error("Malformed request sent to the Notification Service.");
                return new RuntimeException("Invalid request (400) to Notification Service");
            case 404:
                log.error("Notification Service not found.");
                return new RuntimeException("Notification Service not found (404)");
            case 500:
            case 503:
                log.error("The Notification Service encountered an internal error or is unavailable.");
                return new RuntimeException("Notification Service unavailable (" + response.status() + ")");
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}