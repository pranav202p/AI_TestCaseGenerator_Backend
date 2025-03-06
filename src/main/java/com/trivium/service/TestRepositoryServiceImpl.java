package com.trivium.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestRepositoryServiceImpl implements TestRepositoryService {

    private final RestTemplate restTemplate;

    @Autowired
    public TestRepositoryServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String processRepository(String repoUrl) {
        try {
            // Convert GitHub repo URL to API URL
            String apiUrl = convertToGitHubApiUrl(repoUrl);

            // Set up headers for GitHub API (optional: add your GitHub token for higher rate limits)
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.github.v3+json");
            // If you have a GitHub token, uncomment the next line and add your token
            // headers.set("Authorization", "token YOUR_GITHUB_TOKEN");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Fetch repository content from GitHub
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // Return the fetched repository data
            return response.getBody();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return a more detailed error message
            return "Failed to fetch repository: " + e.getMessage() + " (URL: " + repoUrl + ")";
        }
    }

    private String convertToGitHubApiUrl(String repoUrl) {
        // Validate the URL format
        if (!repoUrl.startsWith("https://github.com/")) {
            throw new IllegalArgumentException("Invalid GitHub repository URL. Must start with 'https://github.com/'");
        }

        // Extract username and repository name
        String path = repoUrl.substring("https://github.com/".length());
        if (!path.contains("/")) {
            throw new IllegalArgumentException("Invalid GitHub repository URL. Must be in format 'https://github.com/username/repository'");
        }

        // Convert "https://github.com/user/repo" -> "https://api.github.com/repos/user/repo/contents"
        return "https://api.github.com/repos/" + path + "/contents";
    }
}