package com.example;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    private static String API_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        String apiUrl = System.getProperty("apiUrl", API_URL);
        String outputDir = System.getProperty("outputDir", "output");
        boolean fetchComments = System.getProperty("fetchComments", "false").equalsIgnoreCase("true");
        ObjectMapper mapper = new ObjectMapper();
        DataFetcher dataFetcher = new DataFetcher(mapper);
        DataWriter dataWriter = new DataWriter(mapper);

        try {
            List<Post> posts = dataFetcher.fetchData(apiUrl, fetchComments);
            dataWriter.writeData(posts, fetchComments, outputDir);
        } catch (IOException e) {
            throw new RuntimeException("Error fetching or writing data", e);
        }
    }

}