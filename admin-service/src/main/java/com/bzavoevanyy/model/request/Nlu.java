package com.bzavoevanyy.model.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Nlu {
    private List<Entity> entities = new ArrayList<>();
}
