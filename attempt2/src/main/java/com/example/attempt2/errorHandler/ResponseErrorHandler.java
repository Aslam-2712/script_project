package com.example.attempt2.errorHandler;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseErrorHandler implements org.springframework.web.client.ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // Indicate whether the response has an error
        return (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Handle the error based on status code
        String responseBody = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
        System.err.println("Error Code: " + response.getStatusCode());
        System.err.println("Error Body: " + responseBody);

        // You can further customize this to handle different status codes differently
        if (response.getStatusCode().is4xxClientError()) {
            // Handle 4xx errors
            System.err.println("Client Error Occurred: " + response.getStatusCode());
        } else if (response.getStatusCode().is5xxServerError()) {
            // Handle 5xx errors
            System.err.println("Server Error Occurred: " + response.getStatusCode());
        }

    }



}

