package com.example;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    private static String API_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {

        boolean fetchComments = System.getProperty("fetchComments", "false").equalsIgnoreCase("true");
        ObjectMapper mapper = new ObjectMapper();
        DataFetcher dataFetcher = new DataFetcher(mapper);
        DataWriter dataWriter = new DataWriter(mapper);

        try {
            List<Post> posts = dataFetcher.fetchData(API_URL, fetchComments);
            dataWriter.writeData(posts, fetchComments);
        } catch (IOException e) {
            throw new RuntimeException("Error fetching or writing data", e);
        }
    }

}