package br.com.webstore.handler;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Project: api-webstore
 *
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 23/04/2020
 */
@ControllerAdvice
public class RestResponseExceptionHandler extends DefaultResponseErrorHandler{
    @Override
    public boolean hasError (ClientHttpResponse response) throws IOException {
        System.out.println("Inside hasError ");
        return super.hasError(response);
    }

    @Override
    public void handleError (ClientHttpResponse response) throws IOException {
        System.out.println("Doing something with statusCode " + response.getStatusCode());
        System.out.println("Doing something with body " + IOUtils.toString(response.getBody(), "UTF-8"));
    }
}
