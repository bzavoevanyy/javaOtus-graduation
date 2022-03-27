package com.bzavoevanyy.controller;

import com.bzavoevanyy.dao.CityDao;
import com.bzavoevanyy.model.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.given;


@WebMvcTest(CityController.class)
@DisplayName("CityController test")
class CityControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CityDao cityDao;

    private final static City TEST_CITY = new City("TEST_CITY", "TEST_MESSAGE", "TEST_LINK", LocalDateTime.now());

    @Test
    @DisplayName(" should return city page with list of cities")
    @WithMockUser(roles = "ADMIN")
    void should_return_city_page_with_list_of_cities() throws Exception {
        given(cityDao.findAll()).willReturn(List.of(TEST_CITY));
        mvc.perform(get("/city"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cities", List.of(TEST_CITY)))
                .andExpect(view().name("city"));

    }

    @Test
    @DisplayName(" should return empty edit page")
    @WithMockUser(roles = "ADMIN")
    void should_return_empty_edit_page() throws Exception {
        mvc.perform(get("/city/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-city"));
    }

    @Test
    @DisplayName(" should return edit page with model")
    @WithMockUser(roles = "ADMIN")
    void should_return_edit_page_with_model() throws Exception {
        given(cityDao.findById("TEST_CITY")).willReturn(Optional.of(TEST_CITY));
        mvc.perform(get("/city/edit").param("cityName", "TEST_CITY"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("city", TEST_CITY))
                .andExpect(view().name("edit-city"));
    }

    @Test
    @DisplayName(" should update city and redirect")
    @WithMockUser(roles = "ADMIN")
    void should_invoke_cityDao_update() throws Exception {
        given(cityDao.findById("TEST_CITY")).willReturn(Optional.of(TEST_CITY));
        mvc.perform(post("/city/edit")
                .param("cityName", "TEST_CITY")
                .param("message", "TEST_MESSAGE")
                .param("serviceLink", "TEST_LINK"))
                .andExpect(status().is3xxRedirection());
        verify(cityDao, times(1)).update(any());
    }

    @Test
    @DisplayName(" should delete city and redirect")
    @WithMockUser(roles = "ADMIN")
    void should_delete_city_and_redirect() throws Exception {
        doNothing().when(cityDao).delete(any());
        given(cityDao.findById("TEST_CITY")).willReturn(Optional.of(TEST_CITY));
        mvc.perform(post("/city/delete")
                .param("cityName", "TEST_CITY"))
                .andExpect(status().is3xxRedirection());
        verify(cityDao, times(1)).delete("TEST_CITY");
    }
}