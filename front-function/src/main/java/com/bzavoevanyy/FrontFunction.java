package com.bzavoevanyy;

import com.bzavoevanyy.dao.CityDaoImpl;
import com.bzavoevanyy.model.response.ComposeResponse;
import com.bzavoevanyy.model.response.Response;
import com.bzavoevanyy.service.CityDataServiceImpl;
import com.bzavoevanyy.service.RequestProcessorImpl;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class FrontFunction extends HttpServlet {
    private final static Logger log = LoggerFactory.getLogger(FrontFunction.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        log.info("Starting process request from Yandex Dialog");

        final var reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        final var cityName = JsonIterator.deserialize(reqBody).get("request", "nlu", "entities")
                .get(0).toString("value", "city");

        final var processor = new RequestProcessorImpl(new CityDaoImpl(), new CityDataServiceImpl());
        final var message = processor.process(cityName);

        log.info("Creating request with message : {}", message);

        final var response = new Response(message);
        final var composeResponse = ComposeResponse
                .builder()
                .response(response)
                .version("1.0")
                .build();
        final var text = JsonStream.serialize(composeResponse);
        final var writer = resp.getWriter();
        resp.setContentType("application/json");
        writer.println(text);
        writer.flush();
    }
}
