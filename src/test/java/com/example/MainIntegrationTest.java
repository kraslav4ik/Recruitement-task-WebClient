package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

class MainIntegrationTest {

    private static final String testOutputDir = "testOutput";
    private static HttpServer server;
    private static final int PORT = 8089;
    private static final String BASE_URL = "http://localhost:" + PORT;
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/posts", exchange -> {
            String postsJson = """
                    [
                      {"id":1,"userId":1,"title":"t1","body":"b1"},
                      {"id":2,"userId":2,"title":"t2","body":"b2"}
                    ]
                    """;
            sendJson(exchange, postsJson);
        });

        server.createContext("/comments", exchange -> {
            String commentsJson = """
                    [
                      {"id":10,"postId":1,"name":"c1","email":"e1","body":"cb1"},
                      {"id":11,"postId":2,"name":"c2","email":"e2","body":"cb2"}
                    ]
                    """;
            sendJson(exchange, commentsJson);
        });

        server.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        server.stop(0);
        cleanOutput();
    }

    @BeforeEach
    void beforeEach() throws IOException {
        cleanOutput();
    }

    private static void cleanOutput() throws IOException {
        Path outDir = Paths.get(testOutputDir);
        if (Files.exists(outDir)) {
            try (Stream<Path> files = Files.walk(outDir)) {
                files.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
        if (Files.exists(outDir)) {
            Files.delete(outDir);
        }
    }

    @Test
    void testMainIntegration() throws Exception {
        System.setProperty("fetchComments", "true");
        System.setProperty("apiUrl", BASE_URL);
        System.setProperty("outputDir", testOutputDir);
        Main.main(new String[0]);

        File outDir = new File(testOutputDir);
        assertTrue(outDir.exists());

        File post1File = new File(outDir, "1.json");
        assertTrue(post1File.exists());
        Post post1 = mapper.readValue(post1File, Post.class);
        assertEquals(1, post1.id());
        assertEquals("t1", post1.title());

        Set<Comment> comments = post1.comments();
        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        Comment comment = comments.iterator().next();
        assertEquals(10, comment.id());
        assertEquals("c1", comment.name());
    }

    private static void sendJson(HttpExchange exchange, String json) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }
}