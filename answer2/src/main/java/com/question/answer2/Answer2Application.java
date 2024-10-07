package com.question.answer2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@SpringBootApplication
public class Answer2Application implements CommandLineRunner {

	private final URLFetcherService fetcherService;

    public Answer2Application(URLFetcherService fetcherService) {
        this.fetcherService = fetcherService;
    }

	public static void main(String[] args) {
		SpringApplication.run(Answer2Application.class, args);
	}

	@Override
    public void run(String... args) {
        List<String> urls = List.of(
                "https://api.example.com/data1",
                "https://api.example.com/data2",
                "https://api.invalid.url"
        );

        List<String> responses = fetcherService.fetchURLsConcurrently(urls);
        System.out.println("Responses: " + responses);
    }
}

@Service
class URLFetcherService {
    private static final Logger LOGGER = Logger.getLogger(URLFetcherService.class.getName());
    private final HttpClient client = HttpClient.newHttpClient();

    public List<String> fetchURLsConcurrently(List<String> urls) {
        List<CompletableFuture<String>> futures = urls.stream()
                .map(this::fetchDataFromURLAsync)
                .collect(Collectors.toList());

        return futures.stream()
                .map(this::getResult)
                .collect(Collectors.toList());
    }

    private CompletableFuture<String> fetchDataFromURLAsync(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new Exception("Failed with HTTP error code: " + response.statusCode());
                }

                return response.body();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to fetch data from URL: " + url, e);
                return null;
            }
        });
    }

    private String getResult(CompletableFuture<String> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving result from CompletableFuture", e);
            return null;
        }
    }
}
