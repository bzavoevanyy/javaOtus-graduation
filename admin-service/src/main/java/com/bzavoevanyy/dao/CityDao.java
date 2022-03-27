package com.bzavoevanyy.dao;

import com.bzavoevanyy.model.City;

import java.util.List;
import java.util.Optional;

public interface CityDao {
    List<City> findAll();
    Optional<City> findById(String cityName);
    void update(City city);
    void save(City city);
    void delete(String cityName);
}
