package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Comment(long id, @JsonIgnore long postId, String name, String email, String body) {
}
