package com.eco.route;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CarbonService.java - Service Layer
 * 
 * This class contains the business logic for the Eco-Route Carbon Tracker application.
 * It orchestrates API calls (via ApiClient), performs calculations, and handles
 * persistent storage of calculation history to a CSV file.
 * 
 * Key responsibilities:
 * 1. Calculate CO2 emissions for a given distance and transport type
 * 2. Compare emissions between car types and bus for the same distance
 * 3. Save calculation results to a CSV file for historical tracking
 */
public class CarbonService {

    // Instance of ApiClient to fetch emissions data from the Carbon Interface API
    private final ApiClient apiClient;
    
    // Path to the CSV file where calculation history is stored
    private static final String HISTORY_FILE = "calculation_history.csv";
    
    // Flag to track if the CSV header has been written (to avoid duplicates)
    private boolean csvHeaderWritten = false;

    /**
     * Constructor: Initializes the CarbonService with an ApiClient instance.
     * 
     * @param apiClient The ApiClient instance for API communication
     */
    public CarbonService(ApiClient apiClient) {
        this.apiClient = apiClient;
        // Check if CSV header already exists
        this.csvHeaderWritten = new File(HISTORY_FILE).exists();
    }

    /**
     * Calculates CO2 emissions for a given distance and transport type.
     * 
     * This method calls the ApiClient to fetch the emissions data from the
     * Carbon Interface API and returns the kg CO2e value.
     * 
     * @param distance Distance traveled in kilometers (must be > 0)
     * @param transportType Type of transport: "small_car", "large_car", "bus", "flight"
     * @return CO2 emissions in kilograms of CO2 equivalent (kg CO2e)
     * @throws Exception if API call fails or data is invalid
     */
    public double calculateEmissions(double distance, String transportType) throws Exception {
        return apiClient.fetchEmissions(distance, transportType);
    }

    /**
     * Calculates the CO2 savings by taking the bus instead of a car.
     * 
     * This method assumes the user currently drives a car. It fetches the bus emissions
     * for the same distance and calculates the difference.
     * 
     * Formula: savings = carEmissions - busEmissions
     * 
     * If the result is negative (bus emits more than the car, unlikely but possible
     * in edge cases), this method returns 0 to avoid confusing the user.
     * 
     * @param carEmissions CO2 emissions of the car in kg CO2e
     * @param distance Distance traveled in kilometers
     * @return CO2 savings in kg CO2e if taking the bus (0 or positive value)
     * @throws Exception if API call fails
     */
    public double calculateBusSavings(double carEmissions, double distance) throws Exception {
        // Fetch bus emissions for the same distance
        double busEmissions = apiClient.fetchEmissions(distance, "bus");
        
        // Calculate savings as the difference
        double savings = carEmissions - busEmissions;
        
        // Return 0 if the result is negative (bus would emit more, which is unlikely)
        return Math.max(savings, 0);
    }

    /**
     * Maps user-friendly transport type names to Carbon Interface API identifiers.
     * 
     * The API expects specific identifiers for different transport methods.
     * This method converts from user-facing names (shown in the UI dropdown)
     * to the API identifiers.
     * 
     * @param userFriendlyType The transport type as shown in the UI (e.g., "Small Car", "Large Car", "Bus", "Flight")
     * @return The corresponding API identifier (e.g., "small_car", "large_car", "bus", "flight")
     * @throws IllegalArgumentException if the transport type is not recognized
     */
    public static String mapTransportType(String userFriendlyType) {
        // Normalize input: trim whitespace and convert to lowercase for case-insensitive matching
        if (userFriendlyType == null) {
            throw new IllegalArgumentException("Transport type cannot be null");
        }

        switch (userFriendlyType.trim().toLowerCase()) {
            case "small car":
                return "small_car";
            case "large car":
                return "large_car";
            case "bus":
                return "bus";
            case "flight":
                return "flight";
            default:
                throw new IllegalArgumentException(
                    "Unknown transport type: " + userFriendlyType + 
                    ". Supported types: Small Car, Large Car, Bus, Flight"
                );
        }
    }

    /**
     * Saves a calculation result to the CSV history file.
     * 
     * This method appends a new row to the calculation_history.csv file with
     * the calculation details. The file is created automatically if it doesn't exist,
     * and a header row is written on the first write.
     * 
     * CSV Format (one row per calculation):
     * timestamp,distance_km,transport_type,co2_kg,bus_savings_kg
     * 2025-03-28T14:30:45,100.5,small_car,25.34,5.12
     * 
     * @param distance Distance traveled in kilometers
     * @param transportType The user-friendly transport type name (not the API identifier)
     * @param emissions CO2 emissions in kg CO2e
     * @param savings CO2 savings by taking bus instead (in kg CO2e), or 0 if not applicable
     * @throws IOException if file writing fails
     */
    public void logCalculationToFile(double distance, String transportType, double emissions, double savings) throws IOException {
        try (FileWriter writer = new FileWriter(HISTORY_FILE, true)) {
            // Write CSV header if this is the first write
            if (!csvHeaderWritten) {
                writer.write("timestamp,distance_km,transport_type,co2_kg,bus_savings_kg\n");
                csvHeaderWritten = true;
            }

            // Get current timestamp in ISO 8601 format (e.g., 2025-03-28T14:30:45)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Build CSV row: timestamp, distance, transport type, emissions (rounded to 2 decimals), savings (rounded to 2 decimals)
            String csvRow = String.format(
                "%s,%.1f,%s,%.2f,%.2f%n",
                timestamp,
                distance,
                transportType.toLowerCase(),
                emissions,
                savings
            );

            // Write the row to the CSV file
            writer.write(csvRow);
        }
    }

    /**
     * Gets the absolute path to the calculation history CSV file.
     * 
     * This method is useful for the UI to display the file location or
     * allow the user to view the history file.
     * 
     * @return The absolute file path to calculation_history.csv
     */
    public String getHistoryFilePath() {
        return new File(HISTORY_FILE).getAbsolutePath();
    }
}
