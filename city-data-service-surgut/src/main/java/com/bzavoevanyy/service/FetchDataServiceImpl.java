package com.bzavoevanyy.service;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FetchDataServiceImpl implements FetchDataService {
    private final static Logger log = LoggerFactory.getLogger(FetchDataServiceImpl.class);

    @Override
    public String fetch() {

        final var url = System.getenv("url");
        final var connect = Jsoup.connect(url);
        try {
            log.info("Start connecting, url: {}", url);
            final var document = connect.get();
            return document.getElementsByClass("activ").eachText().get(0);

        } catch (IOException e) {
            log.error("Error get data from web resource: {}", e.getMessage());
        }
        return "";
    }

}
