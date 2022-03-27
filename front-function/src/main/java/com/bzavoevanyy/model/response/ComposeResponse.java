package com.bzavoevanyy.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComposeResponse {
    private Response response;
    private String version;
}
