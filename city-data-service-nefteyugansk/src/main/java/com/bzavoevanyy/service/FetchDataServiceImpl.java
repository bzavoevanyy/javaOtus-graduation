package com.bzavoevanyy.service;

import com.jsoniter.JsonIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FetchDataServiceImpl implements FetchDataService {
    private final static Logger log = LoggerFactory.getLogger(FetchDataServiceImpl.class);
    private final DataProcessor dataProcessor;

    public FetchDataServiceImpl(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    @Override
    public String fetch() {

        final var whetherApiKey = System.getenv("whetherApiKey");
        final var apiUrl = String
                .format("https://api.openweathermap.org/data/2.5/weather?zip=628301,RU&units=metric&appid=%s", whetherApiKey);

        final var httpClient = HttpClient.newHttpClient();
        final var httpRequest = HttpRequest
                .newBuilder(URI.create(apiUrl))
                .GET()
                .build();
        try {
            final var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            final var deserializedResponse = JsonIterator.deserialize(response.body());
            final var temp = deserializedResponse.toString("main", "temp");
            final var wind = deserializedResponse.toString("wind", "speed");

            return dataProcessor.process(Math.round(Float.parseFloat(temp)), Math.round(Float.parseFloat(wind)));

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return "";
    }

}
