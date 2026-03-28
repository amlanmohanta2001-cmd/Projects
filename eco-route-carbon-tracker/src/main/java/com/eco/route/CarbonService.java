package com.eco.route;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * CarbonService.java - Service Layer
 * 
 * This class contains the business logic for the Eco-Route Carbon Tracker application.
 * It orchestrates API calls (via ApiClient), performs calculations, and manages
 * in-app calculation history tracking.
 * 
 * Key responsibilities:
 * 1. Calculate CO2 emissions for a given distance and transport type
 * 2. Compare emissions between car types and bus for the same distance
 * 3. Maintain calculation history in memory for in-app display
 */
public class CarbonService {

    // Instance of ApiClient to fetch emissions data
    private final ApiClient apiClient;
    
    // In-memory list to store calculation history
    private final List<CalculationRecord> calculationHistory;

    /**
     * Inner class to represent a single calculation record.
     */
    public static class CalculationRecord {
        public final LocalDateTime timestamp;
        public final double distance;
        public final String transportType;
        public final double emissions;
        public final double busSavings;

        public CalculationRecord(LocalDateTime timestamp, double distance, String transportType, 
                                double emissions, double busSavings) {
            this.timestamp = timestamp;
            this.distance = distance;
            this.transportType = transportType;
            this.emissions = emissions;
            this.busSavings = busSavings;
        }

        @Override
        public String toString() {
            return String.format(
                "[%s] Distance: %.1f km | Type: %s | CO₂: %.2f kg | Bus Savings: %.2f kg",
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                distance,
                transportType,
                emissions,
                busSavings
            );
        }
    }

    /**
     * Constructor: Initializes the CarbonService with an ApiClient instance.
     * 
     * @param apiClient The ApiClient instance for API communication
     */
    public CarbonService(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.calculationHistory = new ArrayList<>();
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
     * Maps user-friendly transport type names to API identifiers.
     * 
     * This method converts from user-facing names (shown in the UI dropdown)
     * to the API identifiers used internally.
     * 
     * Supported Transport Types:
     * - Small Car → small_car (0.25 kg CO₂e/km)
     * - Large Car → large_car (0.31 kg CO₂e/km)
     * - Bus → bus (0.08 kg CO₂e/km per passenger)
     * - Flight → flight (0.15 kg CO₂e/km per passenger)
     * - Electric Car → electric_car (0.05 kg CO₂e/km)
     * - Electric Bike → electric_bike (0.01 kg CO₂e/km)
     * - Train → train (0.04 kg CO₂e/km per passenger)
     * 
     * @param userFriendlyType The transport type as shown in the UI
     * @return The corresponding API identifier
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
            case "electric car":
                return "electric_car";
            case "electric bike":
                return "electric_bike";
            case "train":
                return "train";
            default:
                throw new IllegalArgumentException(
                    "Unknown transport type: " + userFriendlyType + 
                    ". Supported types: Small Car, Large Car, Bus, Flight, Electric Car, Electric Bike, Train"
                );
        }
    }

    /**
     * Saves a calculation result to the in-memory history.
     * 
     * This method stores the calculation in memory for display in the app.
     * The history persists during the session and is displayed in the UI.
     * 
     * @param distance Distance traveled in kilometers
     * @param transportType The user-friendly transport type name
     * @param emissions CO2 emissions in kg CO2e
     * @param savings CO2 savings by taking bus instead (in kg CO2e), or 0 if not applicable
     */
    public void logCalculation(double distance, String transportType, double emissions, double savings) {
        CalculationRecord record = new CalculationRecord(
            LocalDateTime.now(),
            distance,
            transportType,
            emissions,
            savings
        );
        calculationHistory.add(record);
    }

    /**
     * Gets the complete calculation history.
     * 
     * @return List of all CalculationRecord objects
     */
    public List<CalculationRecord> getCalculationHistory() {
        return new ArrayList<>(calculationHistory); // Return a copy to prevent external modification
    }

    /**
     * Gets the calculation history as a formatted string for display.
     * 
     * @return Formatted string representation of all calculations
     */
    public String getHistoryAsString() {
        if (calculationHistory.isEmpty()) {
            return "No calculations performed yet.\nStart by entering a distance and selecting a transport type!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                  CALCULATION HISTORY\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        for (int i = 0; i < calculationHistory.size(); i++) {
            CalculationRecord record = calculationHistory.get(i);
            sb.append(String.format("%d. %s\n", i + 1, record.toString()));
            if (record.busSavings > 0) {
                sb.append(String.format("   → By bus, save: %.2f kg CO₂\n", record.busSavings));
            }
            sb.append("\n");
        }

        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append(String.format("Total Calculations: %d\n", calculationHistory.size()));
        return sb.toString();
    }

    /**
     * Clears the calculation history.
     */
    public void clearHistory() {
        calculationHistory.clear();
    }
}
