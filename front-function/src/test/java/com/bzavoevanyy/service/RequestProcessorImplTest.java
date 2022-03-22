package com.bzavoevanyy.service;

import com.bzavoevanyy.dao.CityDaoImpl;
import com.bzavoevanyy.model.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("RequestProcessorImpl test")
class RequestProcessorImplTest {

    private final CityDaoImpl cityDao = mock(CityDaoImpl.class);
    private final CityDataServiceImpl cityDataService = mock(CityDataServiceImpl.class);
    private final RequestProcessor processor = new RequestProcessorImpl(cityDao, cityDataService);

    private final static City TEST_CITY = new City("TEST CITY", "TEST MESSAGE", "TEST LINK", LocalDateTime.now());

    @Test
    @DisplayName(" should return correct string for known city")
    void should_return_correct_string_for_known_city() {
        when(cityDao.findById("TEST CITY")).thenReturn(Optional.of(TEST_CITY));
        assertThat(processor.process("TEST CITY")).isEqualTo("TEST MESSAGE");
    }

    @Test
    @DisplayName(" should return correct string for unknown city")
    void should_return_correct_string_for_unknown_city() {
        when(cityDao.findById("TEST CITY")).thenReturn(Optional.empty());
        assertThat(processor.process("TEST CITY")).isEqualTo("К сожалению, информация по вашему городу отсутствует");
    }

    @Test
    @DisplayName(" should invoke external service if date expired")
    void should_invoke_external_service_if_date_expired() {
        final var expiredCity = new City("TEST CITY", "TEST MESSAGE", "TEST LINK",
                LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 0)));

        when(cityDao.findById("TEST CITY")).thenReturn(Optional.of(expiredCity));
        doNothing().when(cityDao).update(any());
        when(cityDataService.getMessage("TEST LINK")).thenReturn("TEST MESSAGE");
        processor.process("TEST CITY");
        verify(cityDataService, times(1)).getMessage("TEST LINK");
    }
}