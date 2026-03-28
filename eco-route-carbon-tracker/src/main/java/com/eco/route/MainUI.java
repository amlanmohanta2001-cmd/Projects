package com.eco.route;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * MainUI.java - UI Layer (Presentation Layer)
 * 
 * This class provides the graphical user interface (GUI) for the Eco-Route Carbon Tracker
 * application using Java Swing. It handles all user interactions and displays results.
 * 
 * The UI consists of:
 * 1. Input fields: Distance (km) and Transport Type (dropdown)
 * 2. Action button: Calculate emissions
 * 3. Output area: Display results and bus savings
 * 4. Menu: Option to view calculation history
 * 
 * This class acts as the main entry point (@main method) and orchestrates
 * communication between the UI and the CarbonService.
 */
public class MainUI extends JFrame {

    // Service layer dependency
    private final CarbonService carbonService;

    // UI Components
    private JTextField distanceField;              // Input field for distance (km)
    private JComboBox<String> transportTypeCombo; // Dropdown for transport type
    private JTextArea resultsArea;                // Output area for results
    private JButton calculateButton;              // Calculate button
    private JButton viewHistoryButton;            // View calculation history button

    /**
     * Constructor: Initializes the MainUI with the CarbonService and sets up the GUI.
     * 
     * @param carbonService The CarbonService instance for business logic
     */
    public MainUI(CarbonService carbonService) {
        this.carbonService = carbonService;

        // Configure the JFrame
        setTitle("Eco-Route: Carbon Tracker");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Initialize and arrange UI components
        initializeComponents();
    }

    /**
     * Initializes and arranges all Swing UI components.
     * 
     * This method creates:
     * 1. Input panel with distance field and transport type dropdown
     * 2. Button panel with Calculate and View History buttons
     * 3. Results text area to display outputs
     * 4. Main content pane with proper layout
     */
    private void initializeComponents() {
        // Create main content pane with BorderLayout
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== INPUT PANEL =====
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));

        // Distance input
        JLabel distanceLabel = new JLabel("Distance (km):");
        distanceField = new JTextField();
        distanceField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(distanceLabel);
        inputPanel.add(distanceField);

        // Transport type input
        JLabel transportLabel = new JLabel("Transport Type:");
        String[] transportTypes = {"Small Car", "Large Car", "Bus", "Flight", "Electric Car", "Electric Bike", "Train"};
        transportTypeCombo = new JComboBox<>(transportTypes);
        transportTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(transportLabel);
        inputPanel.add(transportTypeCombo);

        // Empty cells to fill the 3x2 grid
        inputPanel.add(new JLabel());
        inputPanel.add(new JLabel());

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Calculate button
        calculateButton = new JButton("Calculate Emissions");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setPreferredSize(new Dimension(180, 40));
        calculateButton.addActionListener(this::onCalculateButtonClicked);
        buttonPanel.add(calculateButton);

        // View History button
        viewHistoryButton = new JButton("View History");
        viewHistoryButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewHistoryButton.setPreferredSize(new Dimension(140, 40));
        viewHistoryButton.addActionListener(this::onViewHistoryButtonClicked);
        buttonPanel.add(viewHistoryButton);

        // Clear History button
        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearHistoryButton.setPreferredSize(new Dimension(140, 40));
        clearHistoryButton.addActionListener(ev -> {
            carbonService.clearHistory();
            resultsArea.setText("History cleared. Start a new calculation!");
            showInfoDialog("Success", "Calculation history has been cleared.");
        });
        buttonPanel.add(clearHistoryButton);

        // ===== RESULTS AREA =====
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        resultsArea = new JTextArea();
        resultsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setText("Enter distance, select transport type, and click 'Calculate Emissions' to get started.");

        // Add scroll bar to results area
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== ADD PANELS TO CONTENT PANE =====
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(resultsPanel, BorderLayout.SOUTH);

        // Set the content pane and display the frame
        setContentPane(contentPane);
        setVisible(true);
    }

    /**
     * Action listener for the "Calculate Emissions" button.
     * 
     * This method:
     * 1. Retrieves and validates user input (distance and transport type)
     * 2. Calls the CarbonService to fetch emissions data from the API
     * 3. If the transport type is a car variant, calculates bus savings
     * 4. Logs the calculation to the CSV history file
     * 5. Displays formatted results in the results area
     * 6. Shows error dialogs if anything goes wrong
     * 
     * @param e The action event triggered by button click
     */
    private void onCalculateButtonClicked(ActionEvent e) {
        try {
            // Step 1: Retrieve and validate input
            double distance = validateDistance(distanceField.getText());
            String transportType = (String) transportTypeCombo.getSelectedItem();

            // Step 2: Map user-friendly name to API identifier
            String apiTransportType = CarbonService.mapTransportType(transportType);

            // Step 3: Calculate emissions using the service layer
            double emissions = carbonService.calculateEmissions(distance, apiTransportType);

            // Step 4: Calculate bus savings if transport type is a car variant
            double busSavings = 0;
            String savingsMessage = "";
            if (transportType.toLowerCase().contains("car")) {
                busSavings = carbonService.calculateBusSavings(emissions, distance);
                savingsMessage = String.format(
                    "\n\n🌍 Environmental Impact:\n" +
                    "By taking the bus instead, you could save %.2f kg of CO₂!",
                    busSavings
                );
            }

            // Step 5: Log calculation to in-memory history
            carbonService.logCalculation(distance, transportType, emissions, busSavings);

            // Step 6: Display formatted results with updated history
            String results = String.format(
                "📊 CALCULATION RESULTS\n" +
                "═══════════════════════════════════════\n" +
                "Distance: %.1f km\n" +
                "Transport Type: %s\n" +
                "CO₂ Emissions: %.2f kg CO₂e\n" +
                "%s\n" +
                "\n✓ Calculation saved to history.\n\n" +
                "────────────────────────────────────────\n" +
                "%s",
                distance,
                transportType,
                emissions,
                savingsMessage,
                carbonService.getHistoryAsString()
            );

            resultsArea.setText(results);

        } catch (NumberFormatException ex) {
            // Handle invalid distance input (non-numeric)
            showErrorDialog("Invalid Input", "Please enter a valid numeric distance in kilometers.");
        } catch (IllegalArgumentException ex) {
            // Handle validation errors (e.g., distance <= 0)
            showErrorDialog("Invalid Input", ex.getMessage());
        } catch (Exception ex) {
            // Handle API errors or other unexpected errors
            showErrorDialog(
                "Calculation Error",
                "An error occurred while calculating emissions:\n" + ex.getMessage()
            );
        }
    }

    /**
     * Action listener for the "View History" button.
     * 
     * This method displays the complete calculation history in the results area.
     * Includes options to clear history if needed.
     * 
     * @param e The action event triggered by button click
     */
    private void onViewHistoryButtonClicked(ActionEvent e) {
        // Get formatted history string from the service
        String historyText = carbonService.getHistoryAsString();

        // Display history in the results area
        resultsArea.setText(historyText);

        // Show confirmation message
        showInfoDialog("History", "Displaying calculation history.\nClick 'Calculate Emissions' to return to calculation mode.");
    }

    /**
     * Validates and parses the distance input.
     * 
     * This method checks that:
     * 1. The input is not empty
     * 2. The input is a valid numeric value
     * 3. The distance is greater than 0
     * 
     * @param distanceStr The distance input as a string
     * @return The parsed distance as a double
     * @throws NumberFormatException if the input is not a valid number
     * @throws IllegalArgumentException if the distance is <= 0
     */
    private double validateDistance(String distanceStr) throws NumberFormatException, IllegalArgumentException {
        if (distanceStr == null || distanceStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Distance cannot be empty. Please enter a value.");
        }

        double distance = Double.parseDouble(distanceStr.trim());

        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0 km.");
        }

        return distance;
    }

    /**
     * Displays an error dialog to the user.
     * 
     * @param title The dialog title
     * @param message The error message
     */
    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information dialog to the user.
     * 
     * @param title The dialog title
     * @param message The information message
     */
    private void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method: Entry point for the application.
     * 
     * This method:
     * 1. Initializes the ApiClient (loads API key from .env)
     * 2. Initializes the CarbonService with the ApiClient
     * 3. Creates and displays the MainUI frame
     * 4. Shows an error dialog if the API key is missing
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize the API client (this loads the .env file)
                ApiClient apiClient = new ApiClient();

                // Initialize the service layer
                CarbonService carbonService = new CarbonService(apiClient);

                // Create and display the main GUI
                MainUI mainUI = new MainUI(carbonService);

                // Show a welcome message (optional)
                JOptionPane.showMessageDialog(
                    mainUI,
                    "Welcome to Eco-Route: Carbon Tracker!\n\n" +
                    "This application calculates CO₂ emissions for different transportation modes\n" +
                    "and helps you understand the environmental impact of your travel choices.",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (RuntimeException ex) {
                // Handle API key loading errors
                JOptionPane.showMessageDialog(
                    null,
                    "Error: " + ex.getMessage() + "\n\n" +
                    "Please ensure your .env file exists in the project root and contains:\n" +
                    "carbon_interface_api_key=YOUR_API_KEY_HERE",
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
}
