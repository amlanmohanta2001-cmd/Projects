# Plan: Trending YouTube Statistics in Pure Python

## Project Overview
Build a simple Python console application with a 3-layer architecture (API → Service → UI) that fetches trending videos using YouTube's official API and displays advanced statistics like engagement rates and trending rankings. Designed for easy understanding with college-appropriate concepts and architecture.

## Technical Choices
- **Data Source**: Kaggle Dataset - YouTube Trending Videos (CSV file)
- **Output Format**: Console/terminal with formatted tables
- **Statistics Level**: Advanced (engagement rate, trending score, growth metrics)
- **Data Persistence**: None - in-memory analysis only
- **Architecture**: 3-Layer separation of concerns

## Architecture Overview

### Layer 1: Data Layer (`data_layer.py`)
- Functions as a CSV file loader and data parser
- **Responsibility**: Load and parse YouTube trending videos from Kaggle dataset
- **Key Function**: `load_trending_videos(csv_file_path)` 
  - Reads CSV file using pandas
  - Parses data: video_id, title, channel_name, views, likes, comments, publish_time
  - Converts data types (strings to integers for counts)
  - Returns list of Video objects
  - Handles file errors gracefully

### Layer 2: Service Layer (`service_layer.py`)
- **Responsibility**: Business logic and data analysis
- **Key Functions**:
  - `calculate_engagement_rate(likes, views)` → percentage value
  - `calculate_trending_score(views, engagement_rate, days_online)` → combined metric
  - `rank_videos_by_engagement(videos)` → sorted list by engagement
  - `get_top_channels(videos, limit)` → channel frequency analysis
  - `format_statistics_summary(videos)` → prepare aggregated statistics

### Layer 3: UI Layer (`ui_layer.py`)
- **Responsibility**: Display and user interaction
- **Key Functions**:
  - `display_trending_videos(videos)` → formatted table output
  - `display_statistics(videos)` → summary statistics (averages, top channels)
  - `display_engagement_analysis(videos)` → engagement metrics table
  - `main_menu()` → CLI menu interface

### Data Model (`models.py`)
- **Video Class**: Simple data container with attributes
  - video_id, title, channel_name, view_count, like_count, comment_count, published_date
  - Methods to access and format data

### Entry Point (`main.py`)
- Loads configuration (API key from .env)
- Presents menu to user
- Orchestrates calls between layers
- Handles errors with user-friendly messages

## Project Structure
```
Trending_YT_Video_Statistics/
├── src/
│   ├── main.py              # Entry point and orchestrator
│   ├── data_layer.py        # CSV file loader and parser
│   ├── service_layer.py     # Business logic and calculations
│   ├── ui_layer.py          # Console display formatting
│   └── models.py            # Video data class
├── data/
│   └── youtube_trending.csv # Downloaded from Kaggle
├── requirements.txt         # Python dependencies
├── README.md               # Setup and usage guide
├── .gitignore             # Exclude data files and __pycache__
└── SETUP_INSTRUCTIONS.md  # How to download Kaggle dataset
```

## Dependencies
```
pandas>=1.3.0
python-dotenv
```

**Why These?**
- **pandas**: Read and parse CSV files efficiently
- **python-dotenv**: Load environment variables (optional, for future enhancements)

## Implementation Phases

### Phase 1: Setup
- [ ] Create project structure and files
- [ ] Create requirements.txt with dependencies
- [ ] Download Kaggle dataset CSV file
- [ ] Create initial README with setup instructions

### Phase 2: Data Layer
- [ ] Implement models.py with Video class
- [ ] Implement data_layer.py with CSV file loader
- [ ] Test: Verify CSV loading and data parsing

### Phase 3: Service Layer
- [ ] Implement service_layer.py with calculation functions
- [ ] Test: Verify engagement rate and trending score calculations
- [ ] Test: Verify sorting and aggregation functions

### Phase 4: UI Layer
- [ ] Implement ui_layer.py with display functions
- [ ] Test: Verify formatted table output
- [ ] Test: Verify statistics display

### Phase 5: Integration
- [ ] Implement main.py with menu system
- [ ] Test: Verify menu navigation
- [ ] Test: Verify error handling

### Phase 6: Documentation
- [ ] Complete README with explanation of metrics
- [ ] Add code comments explaining key concepts
- [ ] Create usage examples

## Key Statistics Explained (College-Level)

### Engagement Rate
```
Engagement Rate (%) = (Likes / Views) × 100
```
Shows what percentage of viewers engaged by liking the video. Higher = more compelling content.

### Trending Score
```
Trending Score = (Views × Engagement_Rate) / Days_Online
```
Combines views and engagement to show momentum. Newer videos may score higher if gaining traction quickly.

### Comment-to-Like Ratio
```
Comment Ratio = Comments / Likes
```
Shows discussion level relative to quick likes. Higher = more discussion-inducing content.

### Top Channels
Count how many trending videos each channel has. Shows which creators consistently produce trending content.

## Verification Checklist
- [ ] `pip install -r requirements.txt` completes successfully
- [ ] Kaggle dataset CSV file downloaded and placed in `data/` folder
- [ ] `python src/main.py` runs without errors
- [ ] Menu displays options: View Trending Videos, View Statistics, Exit
- [ ] Option 1 displays table with trending videos from CSV
- [ ] Option 2 displays engagement statistics and top channels
- [ ] Engagement rates calculated and displayed as valid percentages
- [ ] Program handles invalid menu input gracefully
- [ ] Program displays error message if CSV file not found
- [ ] Program displays error message if CSV is malformed

## Scope Decisions

### ✅ Included
- Real YouTube trending video data from Kaggle dataset
- Engagement analysis and rankings
- Channel statistics and frequency analysis
- Console output with formatted tables
- Support for multiple trending dataset files (US, IN, GB, etc.)
- Error handling for file loading issues

### ❌ Not Included (Keep Simple)
- Live API integration (uses static dataset instead)
- Historical data tracking or database storage
- Web interface or GUI beyond console
- Machine learning or advanced predictions
- Export to files (can add if needed)
- Real-time data updates (dataset is static)

## Notes for College Project
- Focus on **clean code separation**: Each layer has a single responsibility
- Use **simple naming**: Function and variable names clearly indicate purpose
- Add **comments**: Explain calculations and significant logic
- Keep **functions small**: Each function does one thing well
- Use **meaningful test data**: Show realistic trending video examples
- **Error messages**: Help user understand what went wrong and how to fix it

## Next Steps
1. User confirms plan is acceptable
2. Download Kaggle dataset: https://www.kaggle.com/datasets/datasnaek/youtube-new
3. Create all project files and structure
4. Implement and test each layer sequentially
5. Verify with real Kaggle dataset
6. Document and finalize README
