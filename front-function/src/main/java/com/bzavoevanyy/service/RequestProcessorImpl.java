package com.bzavoevanyy.service;

import com.bzavoevanyy.dao.CityDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RequestProcessorImpl implements RequestProcessor {
    private final static Logger log = LoggerFactory.getLogger(RequestProcessorImpl.class);

    private final CityDao cityDao;
    private final CityDataService cityDataService;

    public RequestProcessorImpl(CityDao cityDao, CityDataService cityDataService) {
        this.cityDao = cityDao;
        this.cityDataService = cityDataService;
    }

    @Override
    public String process(String cityName) {
        final var city = cityDao.findById(cityName);
        String message;
        if (city.isPresent()
                && city.get().getUpdatedAt().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 0)))) {
            message = city.get().getMessage();
            log.info("Message from db: {}", message);
        } else if (city.isPresent()) {
            var cityForUpdate = city.get();

            message = cityDataService.getMessage(cityForUpdate.getServiceLink());
            cityForUpdate.setMessage(message);
            cityDao.update(cityForUpdate);
            log.info("Message from website : {}", message);
        } else {
            message = "К сожалению, информация по вашему городу отсутствует";
        }
        return message;
    }
}
