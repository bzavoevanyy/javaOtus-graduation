package com.bzavoevanyy.dao;

import com.bzavoevanyy.model.City;

import java.util.Optional;

public interface CityDao {
    Optional<City> findById(String cityName);
    void update(City city);
    void save(City city);
}
