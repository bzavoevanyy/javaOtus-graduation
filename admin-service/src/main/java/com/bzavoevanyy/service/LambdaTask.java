package com.bzavoevanyy.service;

import com.bzavoevanyy.dao.CityDao;
import com.bzavoevanyy.model.request.*;
import com.bzavoevanyy.model.response.ComposeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class LambdaTask {
    private final RestTemplate restTemplate;
    private final CityDao cityDao;
    @Value("${FRONT_FUNCTION}")
    private String frontFunction;

    @Scheduled(fixedRate = 10000)
    public void scheduleLambdaTask() {
        final var cities = cityDao.findAll();

        cities.forEach(city -> {
            final var value = new ValueObject(city.getCityName());
            final var entity = new Entity(value);
            final var nlu = new Nlu();
            nlu.getEntities().add(entity);
            final var request = new ComplexRequest(new Request(nlu));
            final var responseEntity = restTemplate
                    .exchange(frontFunction, HttpMethod.POST, new HttpEntity<>(request), ComposeResponse.class);
            if (responseEntity.getBody() != null && responseEntity.getBody().getResponse() != null) {
                log.info(responseEntity.getBody().getResponse().getText());
            }
        });
    }
}
