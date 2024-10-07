package com.question.answer1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class Answer1Application {

	public static void main(String[] args) {
		SpringApplication.run(Answer1Application.class, args);

		URLFetcherService fetcherService = new URLFetcherService();
        List<String> urls = List.of(
                "https://api.example.com/step1",
                "https://api.example.com/step2",
                "https://api.invalid.url"
        );

        List<String> responses = fetcherService.fetchURLs(urls);
        System.out.println("Responses: " + responses);
	}
}

@Service
class URLFetcherService {
    private static final Logger LOGGER = Logger.getLogger(URLFetcherService.class.getName());

    public List<String> fetchURLs(List<String> urls) {
        List<String> responses = new ArrayList<>();
        String previousResponse = null;

        for (String url : urls) {
            try {
                String responseData = fetchDataFromURL(url, previousResponse);
                responses.add(responseData);
                previousResponse = responseData;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to fetch data from URL: " + url, e);
                responses.add(null);
                previousResponse = null;
            }
        }
        return responses;
    }

    private String fetchDataFromURL(String url, String previousResponse) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET();

        if (previousResponse != null) {
            requestBuilder.header("Previous-Response", previousResponse);
        }

        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Failed with HTTP error code: " + response.statusCode());
        }

        return response.body();
    }
}
