package com.example;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

public record Post(long id, long userId, String title, String body,
        @JsonInclude(JsonInclude.Include.NON_NULL) Set<Comment> comments) {

    public Post {
        if (comments == null) {
            comments = new HashSet<>();
        }
    }
}
