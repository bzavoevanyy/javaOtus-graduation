package com.bzavoevanyy;

import com.bzavoevanyy.service.DataProcessorImpl;
import com.bzavoevanyy.service.FetchDataServiceImpl;
import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class GetMessageServlet extends HttpServlet {
    private final static Logger log = LoggerFactory.getLogger(GetMessageServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final var dataProcessor = new DataProcessorImpl();
        final var fetchDataService = new FetchDataServiceImpl(dataProcessor);

        log.info("Preparing response");

        final var message = fetchDataService.fetch();
        final var text = JsonStream.serialize(Map.of("message", message));
        final PrintWriter writer;
        try {
            writer = resp.getWriter();
            resp.setContentType("application/json");
            writer.println(text);
            writer.flush();
        } catch (IOException e) {
            log.error("Error creating response: {}", e.getMessage());
        }
    }
}
