package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class DataFetcher {

    private final ObjectMapper mapper;

    public DataFetcher(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<Post> fetchData(String apiUrl, boolean fetchComments) throws IOException {

        List<Post> posts = Request.get(apiUrl + "/posts")
                .connectTimeout(Timeout.ofSeconds(10))
                .responseTimeout(Timeout.ofSeconds(300))
                .addHeader("Accept", "application/json")
                .execute()
                .handleResponse(new CustomResponseHandler<List<Post>>(new TypeReference<List<Post>>() {
                }));
        if (!fetchComments) {
            return posts;
        }
        Map<Long, List<Comment>> comments = Request.get(apiUrl + "/comments")
                .connectTimeout(Timeout.ofSeconds(10))
                .responseTimeout(Timeout.ofSeconds(300))
                .addHeader("Accept", "application/json")
                .execute()
                .handleResponse(new CustomResponseHandler<List<Comment>>(new TypeReference<List<Comment>>() {
                }))
                .stream()
                .collect(Collectors.groupingBy(Comment::postId));

        posts.forEach(post -> post.comments().addAll(comments.get(post.id())));
        return posts;
    }

    class CustomResponseHandler<T> implements HttpClientResponseHandler<T> {

        private final TypeReference<T> typeRef;

        public CustomResponseHandler(TypeReference<T> typeRef) {
            this.typeRef = typeRef;
        }

        @Override
        public T handleResponse(ClassicHttpResponse response) throws IOException, ParseException {
            int status = response.getCode();
            if (status >= 300) {
                throw new HttpResponseException(status, response.getReasonPhrase());
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new RuntimeException("No response body");
            }

            String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            try {
                return mapper.readValue(json, typeRef);
            } catch (Exception e) {
                throw new RuntimeException("Can't parse JSON", e);
            }
        }
    }
}
