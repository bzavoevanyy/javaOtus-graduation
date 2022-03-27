package com.bzavoevanyy.service;

import com.jsoniter.JsonIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class CityDataServiceImpl implements CityDataService {

    private final static Logger log = LoggerFactory.getLogger(CityDataServiceImpl.class);

    @Override
    public String getMessage(String url) {
        final var httpClient = HttpClient.newHttpClient();
        final var httpRequest = HttpRequest
                .newBuilder(URI.create(url))
                .GET()
                .build();
        try {
            final var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return JsonIterator.deserialize(response.body()).toString("message");
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return "";
    }
}
