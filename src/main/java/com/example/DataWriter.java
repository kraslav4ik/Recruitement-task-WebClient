package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

class DataWriter {
    private final ObjectMapper mapper;

    public DataWriter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void writeData(Collection<Post> posts, boolean addCommentsToJson) throws IOException {
        File outDir = new File("output");
        if (!outDir.exists()) {
            Files.createDirectories(outDir.toPath());
        }
        ObjectMapper mapperToUse = mapper;
        if (!addCommentsToJson) {
            mapperToUse = mapper.copy();
            mapperToUse.addMixIn(Post.class, PostIgnoreCommentsMixIn.class);
        }
        for (Post post : posts) {
            File outFile = new File(outDir, Long.toString(post.id()) + ".json");
            mapperToUse.writerWithDefaultPrettyPrinter().writeValue(outFile, post);
        }

    }

    abstract class PostIgnoreCommentsMixIn {
        @JsonIgnore
        abstract Set<Comment> comments();
    }
}
