# Eco-Route: Carbon Tracker

A Java desktop application that calculates CO₂ emissions for different transportation modes and suggests greener alternatives using the **Carbon Interface API**.

## Features

✅ **Calculate CO₂ Emissions** — Get real-time emissions data for:
- Small Car
- Large Car
- Bus
- Flight

✅ **Compare Transport Modes** — Automatically calculates CO₂ savings when switching from car to bus.

✅ **Track History** — All calculations are saved to a CSV file for reference and analysis.

✅ **Professional API Integration** — Uses the Carbon Interface API with secure API key management via `.env` file.

✅ **Educational Code** — Clear, well-commented code suitable for explaining to your professor.

---

## Technology Stack

- **Language**: Java 17+
- **GUI Framework**: Java Swing
- **Build Tool**: Maven
- **JSON Parsing**: Gson
- **Environment Variables**: Dotenv-java
- **HTTP Client**: Java's built-in `java.net.http.HttpClient`

---

## 3-Layer Architecture

```
┌─────────────────────────────────────┐
│   UI Layer (MainUI.java)            │  ← User Interface (Swing)
├─────────────────────────────────────┤
│   Service Layer (CarbonService.java)│  ← Business Logic
├─────────────────────────────────────┤
│   Data Layer (ApiClient.java)       │  ← API Communication
└─────────────────────────────────────┘
```

---

## Prerequisites

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Maven** (optional, for building from command line)
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **Carbon Interface API Key**
   - Sign up for free at: https://carboninterface.com
   - API documentation: https://www.carboninterface.com/docs

---

## Setup Instructions

### Step 1: Get Your API Key

1. Visit [Carbon Interface](https://carboninterface.com)
2. Sign up for a free account
3. Navigate to your API settings and copy your API key (looks like: `abcd1234efgh5678ijkl9012`)

### Step 2: Create `.env` File

1. In the project root directory (`eco-route-carbon-tracker/`), open `.env` file (it's already created)
2. Replace `YOUR_API_KEY_HERE` with your actual API key:
   ```
   carbon_interface_api_key=abcd1234efgh5678ijkl9012
   ```
3. Save the file

### Step 3: Build the Project

#### Option A: Using Maven (Recommended)
```bash
# Navigate to project root
cd eco-route-carbon-tracker

# Build the project
mvn clean package

# Run the application
java -jar target/eco-route-carbon-tracker-1.0.0-shaded.jar
```

#### Option B: Using VS Code
1. Install the "Extension Pack for Java" extension in VS Code
2. Open the project folder in VS Code
3. Click **Run → Run Without Debugging** (or Ctrl+F5)
4. Select **MainUI** as the main class to run

#### Option C: Using IntelliJ IDEA
1. Open the project as a Maven project
2. Wait for Maven dependencies to download
3. Right-click on `MainUI.java` → **Run 'MainUI.main()'**

---

## Usage

### Running the Application

**From Command Line:**
```bash
java -jar target/eco-route-carbon-tracker-1.0.0-shaded.jar
```

**From IDEs:**
- VS Code: Press Ctrl+F5 or click **Run → Run Without Debugging**
- IntelliJ IDEA: Right-click `MainUI.java` → **Run 'MainUI.main()'**

### Using the Application

1. **Enter Distance**: Input the distance traveled in kilometers (e.g., 100)
2. **Select Transport Type**: Choose from:
   - Small Car
   - Large Car
   - Bus
   - Flight
3. **Click "Calculate Emissions"**: The app fetches real emissions data via the API
4. **View Results**: See CO₂ emissions and (for car types) bus savings
5. **Check History**: Click "View History" to open the `calculation_history.csv` file

### Example Output

```
📊 CALCULATION RESULTS
═══════════════════════════════════════
Distance: 100.0 km
Transport Type: Small Car
CO₂ Emissions: 25.34 kg CO₂e

🌍 Environmental Impact:
By taking the bus instead, you could save 18.50 kg of CO₂!

✓ Calculation saved to history file.
```

---

## File Structure

```
eco-route-carbon-tracker/
├── pom.xml                              # Maven configuration
├── .env                                 # API key (keep secret!)
├── README.md                            # This file
├── calculation_history.csv              # Auto-generated, stores all calculations
└── src/
    └── main/
        └── java/
            └── com/eco/route/
                ├── MainUI.java          # UI Layer (Swing GUI)
                ├── CarbonService.java   # Service Layer (Business Logic)
                └── ApiClient.java       # Data Layer (API Communication)
```

---

## Code Explanation for Your Professor

### 1. **MainUI.java** — The Gateway
- Extends `JFrame` to create a Swing-based desktop application
- Handles user input validation and UI events
- Calls `CarbonService` to orchestrate business logic
- Displays results and error messages using dialog boxes
- Saves calculations to a CSV file for persistence

**Key Methods:**
- `onCalculateButtonClicked()` - Validates input, calls the service, displays results
- `validateDistance()` - Ensures distance is a positive number
- `main()` - Entry point; initializes API client and service layer

### 2. **CarbonService.java** — The Business Logic
- Central hub for all business logic and calculations
- Calls `ApiClient` to fetch emissions data
- Performs calculations (e.g., bus savings = car emissions - bus emissions)
- Handles CSV file logging and history persistence
- Maps user-friendly transport names to API identifiers

**Key Methods:**
- `calculateEmissions()` - Fetches emissions from the API
- `calculateBusSavings()` - Compares car vs. bus emissions
- `logCalculationToFile()` - Appends results to CSV
- `mapTransportType()` - Converts "Small Car" → "small_car" for the API

### 3. **ApiClient.java** — The Data Layer
- Pure integration with the Carbon Interface REST API
- Handles HTTP communication using Java's built-in `HttpClient`
- Manages API key loading from the `.env` file (using Dotenv-java)
- Parses JSON responses using Gson
- Provides robust error handling

**Key Methods:**
- `fetchEmissions()` - Sends POST request to API, returns CO₂ value
- `buildRequestBody()` - Constructs form-encoded request payload
- `parseEmissionsFromResponse()` - Extracts CO₂ data from JSON

---

## How the Three Layers Work Together

```
User Input (MainUI)
        ↓
  Validates Input
        ↓
  Calls CarbonService.calculateEmissions()
        ↓
  CarbonService calls ApiClient.fetchEmissions()
        ↓
  ApiClient sends HTTP POST to Carbon Interface API
        ↓
  API returns JSON with CO₂ data
        ↓
  ApiClient parses JSON, returns double value
        ↓
  CarbonService performs calculations (e.g., bus savings)
        ↓
  CarbonService logs to CSV
        ↓
  Returns result to MainUI
        ↓
  MainUI displays formatted result to user
```

---

## API Integration Details

### Request Format
```
POST /api/v1/estimates
Content-Type: application/x-www-form-urlencoded

type=estimate&distance_value=100&distance_unit=km&transportation_method=small_car
```

### Response Format
```json
{
  "data": {
    "id": "...",
    "type": "estimate",
    "attributes": {
      "carbon_kg": 25.34,
      "carbon_lb": 55.85,
      "carbon_mt": 0.025,
      ...
    }
  }
}
```

Our code extracts the `carbon_kg` value and returns it.

---

## Troubleshooting

### Error: "API key not found"
- **Cause**: The `.env` file is missing or doesn't contain `carbon_interface_api_key`
- **Solution**: Create `.env` in the project root with your API key:
  ```
  carbon_interface_api_key=YOUR_KEY_HERE
  ```

### Error: "Connection refused" or Network Error
- **Cause**: API is unreachable or your internet is down
- **Solution**: Check your internet connection; verify the Carbon Interface API is online

### CSV File Not Creating
- **Cause**: No write permissions in the project directory
- **Solution**: Ensure you have write permissions; try running from a different directory

### Build Fails with Maven
- **Cause**: Maven or Java not installed correctly
- **Solution**:
  ```bash
  # Verify Java
  java -version
  
  # Verify Maven
  mvn -version
  
  # Check if JAVA_HOME is set correctly (Windows)
  echo %JAVA_HOME%
  ```

---

## Future Enhancements

1. **Database Integration** — Replace CSV with a relational database (SQLite, MySQL)
2. **Data Visualization** — Add charts to compare emissions by transport type
3. **Historical Analysis** — Show trends over time (daily/weekly CO₂ tracking)
4. **Route Management** — Save favorite routes and auto-calculate for them
5. **Carbon Offset Information** — Suggest ways to offset calculated emissions
6. **Multi-user Support** — Store data per user with authentication

---

## License

This project is created for educational purposes. Use freely for learning and demonstration.

---

## Contact & Support

For questions about this code:
1. Review the **detailed comments in each `.java` file**
2. Consult [Carbon Interface API Docs](https://www.carboninterface.com/docs)
3. Check [Maven Documentation](https://maven.apache.org/)
4. Refer to [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)

---

## Summary for Your Professor

**Project Goal**: Build a tool that calculates CO₂ emissions and suggests greener alternatives.

**Architecture**: Clean 3-layer design:
- **UI Layer**: User-friendly Swing GUI
- **Service Layer**: Business logic and orchestration
- **Data Layer**: Transparent API integration

**Education Value**:
- Demonstrates API integration best practices
- Shows proper separation of concerns
- Includes comprehensive error handling
- Features persistent data storage
- Uses industry-standard libraries (Gson, Maven)

**Key Takeaways**:
1. REST API integration with Java
2. JSON parsing with Gson
3. GUI development with Swing
4. File I/O and CSV handling
5. Design patterns (3-layer, dependency injection)
6. Environment variable management (.env)

Good luck with your presentation! 🌍
