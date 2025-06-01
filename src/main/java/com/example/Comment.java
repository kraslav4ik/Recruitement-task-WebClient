package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Comment(long id, @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) long postId, String name,
        String email, String body) {
}
