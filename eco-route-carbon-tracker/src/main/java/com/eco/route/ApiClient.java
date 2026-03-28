package com.eco.route;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * ApiClient.java - Data Layer
 * 
 * This class is responsible for all HTTP communication with the Carbon Interface API.
 * It handles API key loading from the .env file, building API requests, sending them,
 * and parsing the JSON responses.
 * 
 * The Carbon Interface API (https://carboninterface.com) estimates carbon emissions
 * for different transportation modes based on distance traveled.
 */
public class ApiClient {

    // API endpoint for fetching carbon emission estimates
    private static final String API_URL = "https://www.carboninterface.com/api/v1/estimates";
    
    // API key loaded from .env file
    private final String apiKey;
    
    // HTTP client for making requests (reusable, thread-safe)
    private final HttpClient httpClient;

    /**
     * Constructor: Initializes the ApiClient by loading the API key from the .env file.
     * 
     * The .env file should contain a line like:
     * carbon_interface_api_key=YOUR_API_KEY_HERE
     * 
     * Throws RuntimeException if the API key is not found in the .env file.
     */
    public ApiClient() {
        // Load environment variables from .env file in project root
        Dotenv dotenv = Dotenv.load();
        
        // Retrieve the API key; throw exception if missing
        this.apiKey = dotenv.get("carbon_interface_api_key");
        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            throw new RuntimeException(
                "Error: API key not found. Please set 'carbon_interface_api_key' in the .env file."
            );
        }
        
        // Create a shared HTTP client for all requests
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Fetches CO2 emissions estimate from the Carbon Interface API.
     * 
     * This method builds a POST request with the given distance and transport type,
     * sends it to the Carbon Interface API, and parses the JSON response to extract
     * the kg_CO2e (kilograms of CO2 equivalent) value.
     * 
     * @param distance Distance traveled in kilometers (must be > 0)
     * @param transportType Type of transport: "small_car", "large_car", "bus", "flight"
     * @return CO2 emissions in kilograms of CO2 equivalent (kg CO2e)
     * @throws Exception if API request fails, network error occurs, or response is invalid
     */
    public double fetchEmissions(double distance, String transportType) throws Exception {
        // Validate inputs
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0 km");
        }
        if (transportType == null || transportType.trim().isEmpty()) {
            throw new IllegalArgumentException("Transport type cannot be empty");
        }

        // Build the request body in form-encoded format
        // The Carbon Interface API expects: type, distance_value, distance_unit, transportation_method
        String requestBody = buildRequestBody(distance, transportType);

        // Create the HTTP POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and receive the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Check if the response status is not 201 (Created) - the API returns 201 on success
        if (response.statusCode() != 201 && response.statusCode() != 200) {
            throw new Exception(
                "API Error (Status " + response.statusCode() + "): " + response.body()
            );
        }

        // Parse the JSON response and extract the CO2 value
        return parseEmissionsFromResponse(response.body());
    }

    /**
     * Builds the request body in form-encoded format for the Carbon Interface API.
     * 
     * The API expects parameters like: type=estimate&distance_value=100&distance_unit=km&transportation_method=small_car
     * 
     * @param distance Distance in kilometers
     * @param transportType Transport type (e.g., "small_car", "large_car", "bus", "flight")
     * @return URL-encoded form body string
     */
    private String buildRequestBody(double distance, String transportType) {
        try {
            // Use URLEncoder to safely encode each parameter
            String encoded = "type=" + URLEncoder.encode("estimate", StandardCharsets.UTF_8) +
                    "&distance_value=" + distance +
                    "&distance_unit=" + URLEncoder.encode("km", StandardCharsets.UTF_8) +
                    "&transportation_method=" + URLEncoder.encode(transportType, StandardCharsets.UTF_8);
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build request body: " + e.getMessage(), e);
        }
    }

    /**
     * Parses the JSON response from the Carbon Interface API to extract the CO2 value.
     * 
     * The API returns a JSON object like:
     * {
     *   "data": {
     *     "id": "...",
     *     "type": "estimate",
     *     "attributes": {
     *       "carbon_kg": 12.34,
     *       "carbon_lb": 27.21,
     *       "carbon_mt": 0.01,
     *       ...
     *     }
     *   }
     * }
     * 
     * This method extracts the "carbon_kg" value.
     * 
     * @param responseBody JSON response body as a string
     * @return CO2 value in kilograms (kg CO2e)
     * @throws Exception if JSON is malformed or expected fields are missing
     */
    private double parseEmissionsFromResponse(String responseBody) throws Exception {
        try {
            // Parse the JSON response
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            
            // Navigate through the nested JSON structure: data -> attributes -> carbon_kg
            JsonObject data = jsonResponse.getAsJsonObject("data");
            JsonObject attributes = data.getAsJsonObject("attributes");
            double carbonKg = attributes.get("carbon_kg").getAsDouble();
            
            return carbonKg;
        } catch (Exception e) {
            throw new Exception(
                "Failed to parse API response: " + e.getMessage() + "\nResponse: " + responseBody, e
            );
        }
    }
}
