package com.bzavoevanyy.controller;

import com.bzavoevanyy.dao.CityDao;
import com.bzavoevanyy.model.City;
import com.bzavoevanyy.controller.exception.CityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CityController {
    private final CityDao cityDao;

    @GetMapping("/city")
    public String getAllCities(Model model) {
        final var cities = cityDao.findAll();
        model.addAttribute("cities", cities);
        return "city";
    }

    @GetMapping("/city/edit")
    public String getCreatePage(@RequestParam(required = false) String cityName, Model model) {
        if (cityName != null) {
            final var city = cityDao.findById(cityName);
            model.addAttribute("city", city.orElseThrow(CityNotFoundException::new));
        }
        return "edit-city";
    }

    @PostMapping("/city/edit")
    public String saveCity(City city) {
        final var optionalCity = cityDao.findById(city.getCityName());
        if (optionalCity.isPresent()) {
            cityDao.update(city);
        } else {
            cityDao.save(city);
        }
        return "redirect:/city";
    }

    @PostMapping("/city/delete")
    public String deleteCity(@RequestParam String cityName) {
        final var city = cityDao.findById(cityName);
        if (city.isPresent()) {
            cityDao.delete(cityName);
        } else {
            throw new CityNotFoundException();
        }
        return "redirect:/city";
    }
}
