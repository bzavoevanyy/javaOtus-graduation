package com.bzavoevanyy.model;

import com.yandex.ydb.table.result.ResultSetReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private String cityName;
    private String message;
    private String serviceLink;
    private LocalDateTime updatedAt;

    public static City fromResultSet(ResultSetReader resultSet) {
        var cityName = resultSet.getColumn("cityName").getUtf8();
        var message = resultSet.getColumn("message").getUtf8();
        var serviceLink = resultSet.getColumn("serviceLink").getUtf8();
        var updatedAt = resultSet.getColumn("updatedAt").getDatetime();
        return new City(cityName, message, serviceLink, updatedAt);
    }
}
