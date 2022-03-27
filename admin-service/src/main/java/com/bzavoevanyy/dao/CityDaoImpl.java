package com.bzavoevanyy.dao;

import com.bzavoevanyy.model.City;
import com.bzavoevanyy.utils.ThrowingConsumer;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Repository
@RequiredArgsConstructor
public class CityDaoImpl implements CityDao {

    private final EntityManager entityManager;

    @Override
    public List<City> findAll() {
        List<City> cities = new ArrayList<>();
        entityManager.execute(
                "select cityName, message, updatedAt, serviceLink from City", Params.empty(),
                ThrowingConsumer.unchecked(result -> {
                    final var resultSet = result.getResultSet(0);
                    while (resultSet.next()) {
                        cities.add(City.fromResultSet(resultSet));
                    }
                }));
        return cities;
    }

    @Override
    public Optional<City> findById(String cityName) {
        AtomicReference<City> city = new AtomicReference<>();
        entityManager.execute(
                "declare $cityName as Utf8;" +
                        "select cityName, message, updatedAt, serviceLink from City where cityName = $cityName",
                Params.of("$cityName", PrimitiveValue.utf8(cityName.toUpperCase())), ThrowingConsumer.unchecked(result -> {
                    final var resultSet = result.getResultSet(0);
                    if (resultSet.next()) {
                        city.set(City.fromResultSet(resultSet));
                    }
                }));
        return Optional.ofNullable(city.get());
    }

    @Override
    public void update(City city) {
        entityManager.execute(
                "declare $cityName as Utf8;" +
                        "declare $message as Utf8;" +
                        "declare $serviceLink as Utf8;" +
                        "update City set message = $message, serviceLink = $serviceLink, updatedAt = CurrentUtcDateTime() where cityName = $cityName",
                Params.of("$cityName", PrimitiveValue.utf8(city.getCityName().toUpperCase()),
                        "$serviceLink", PrimitiveValue.utf8(city.getServiceLink()),
                        "$message", PrimitiveValue.utf8(city.getMessage())));
    }

    @Override
    public void save(City city) {
        entityManager.execute(
                "declare $serviceLink as Utf8;" +
                        "declare $cityName as Utf8;" +
                        "declare $message as Utf8;" +
                        "insert into City(cityName, message, serviceLink, updatedAt) values ($cityName, $message, $serviceLink, CurrentUtcDateTime());",
                Params.of("$serviceLink", PrimitiveValue.utf8(city.getServiceLink()),
                        "$cityName", PrimitiveValue.utf8(city.getCityName().toUpperCase()),
                        "$message", PrimitiveValue.utf8(city.getMessage()))
        );
    }

    @Override
    public void delete(String cityName) {
        entityManager.execute(
                "declare $cityName as Utf8;" +
                        "delete from City where cityName = $cityName",
                Params.of("$cityName", PrimitiveValue.utf8(cityName.toUpperCase()))
        );
    }
}
