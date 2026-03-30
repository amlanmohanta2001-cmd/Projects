# YouTube Trending Video Statistics Analyzer

A simple Python application to analyze YouTube trending videos using real data from Kaggle. Built with clean 3-layer architecture (Data Layer → Service Layer → UI Layer) - perfect for learning software design patterns.

## Features

✅ **Trending Videos Analysis** — View top videos by views and engagement  
✅ **Engagement Metrics** — Calculate engagement rates, comment ratios, and trending scores  
✅ **Channel Statistics** — Find which channels have the most trending videos  
✅ **Overall Analytics** — View aggregate statistics across all trending videos  
✅ **Easy to Understand** — College-appropriate code with clear architecture  
✅ **No External Dependencies** — Uses only pandas (common data library)  

## Quick Start

### 1. Install Dependencies

```bash
pip install -r requirements.txt
```

Make sure you have Python 3.7+ installed.

### 2. Download Dataset (Two Options)

#### Option A: Automatic Download with kagglehub (Recommended)

This is the easiest way - the script automatically downloads and sets up the data.

```bash
# First, get Kaggle credentials:
# 1. Visit: https://www.kaggle.com/settings/account
# 2. Scroll to "API" section and click "Create New API Token"
# 3. This downloads kaggle.json - place it in ~/.kaggle/

# Then run the setup script:
python setup_data.py
```

The script will:
- ✓ Authenticate with your Kaggle account
- ✓ Download the YouTube Trending Videos dataset
- ✓ Extract CSV files to the `data/` folder automatically

#### Option B: Manual Download from Kaggle

If you prefer manual download:

1. Go to: https://www.kaggle.com/datasets/datasnaek/youtube-new
2. Click the "Download" button
3. Extract the ZIP file
4. Move CSV files to `data/` folder

**Available regions in dataset:**
- `USvideos.csv` (United States)
- `INvideos.csv` (India)
- `GBvideos.csv` (Great Britain)
- `DEvideos.csv` (Germany)
- `FRvideos.csv` (France)
- And many more...

### 3. Run the Application

```bash
python src/main.py
```

You'll see a menu:
```
================================================================================
YOUTUBE TRENDING VIDEO STATISTICS ANALYZER
================================================================================

1. View Trending Videos (by views)
2. View Engagement Analysis
3. View Overall Statistics
4. View Top Channels
5. Exit

================================================================================

Enter your choice (1-5):
```

## Using kagglehub for Automatic Download

The `setup_data.py` script uses kagglehub to automatically download data from Kaggle.

### Setup Kaggle Credentials (One-time)

1. Create a Kaggle account: https://www.kaggle.com
2. Go to Settings → Account: https://www.kaggle.com/settings/account
3. Scroll down to "API" section
4. Click "Create New API Token"
5. A `kaggle.json` file will download - **don't lose it!**
6. Place it in your home directory:
   - **Windows**: `C:\Users\YourUsername\.kaggle\kaggle.json`
   - **Mac/Linux**: `~/.kaggle/kaggle.json`

### Run the Setup Script

```bash
python setup_data.py
```

Output:
```
================================================================================
YOUTUBE TRENDING VIDEO STATISTICS - DATA SETUP
================================================================================

Downloading YouTube Trending Videos dataset from Kaggle...
This may take a few minutes on first run...

⏳ Downloading from Kaggle (datasnaek/youtube-new)...
✓ Downloaded to: C:\Users\...\cache\kagglehub\datasets\datasnaek\youtube-new\versions\1

⏳ Copying CSV files to C:\...\data...
  ✓ Copied: USvideos.csv
  ✓ Copied: INvideos.csv
  ✓ Copied: GBvideos.csv
  ... (more files)

================================================================================
✓ SUCCESS! Downloaded 10 CSV files
================================================================================

You can now run the program:
  python src/main.py
```

**Note**: First download takes a few minutes. Subsequent runs use cached data instantly.

## Project Structure

```
Trending_YT_Video_Statistics/
├── src/
│   ├── main.py              # Entry point - program orchestrator
│   ├── data_layer.py        # CSV file loader
│   ├── service_layer.py     # Calculations and analysis
│   ├── ui_layer.py          # Console display formatting
│   └── models.py            # Video data class
├── data/
│   └── *.csv                # YouTube trending videos (from Kaggle)
├── requirements.txt         # Python dependencies
├── plan.md                  # Project plan and architecture
└── README.md               # This file
```

## Architecture

### 3-Layer Design Pattern

**Why three layers?** Separates concerns and makes code easy to understand and modify.

### Architecture Diagram & Execution Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        APPLICATION EXECUTION FLOW                       │
└─────────────────────────────────────────────────────────────────────────┘

STEP 1: USER RUNS PROGRAM
┌──────────────────────────┐
│   python src/main.py     │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────────────────────────────────────┐
│  LAYER 1: DATA LAYER (data_layer.py)                     │
│  ─────────────────────────────────────────────────────   │
│  1. load_trending_videos(csv_file_path)                  │
│     └─> Reads CSV file using pandas                      │
│     └─> Parses rows and creates Video objects            │
│     └─> Returns: List[Video]                             │
│                                                          │
│  Dependencies:                                           │
│  └─> models.py (Video class)                             │
│  └─> pandas (CSV reading)                                │
└────────────┬─────────────────────────────────────────────┘
             │ Returns: videos = [Video, Video, ...]
             │
             ▼
┌──────────────────────────────────────────────────────────┐
│  LAYER 3: UI LAYER (ui_layer.py)                         │
│  ─────────────────────────────────────────────────────   │
│  1. display_main_menu()  ◄─── Shows menu options        │
│  2. get_user_choice()    ◄─── Gets user input (1-5)     │
└────────────┬─────────────────────────────────────────────┘
             │
             ▼ User selects option (1, 2, 3, 4, or 5)
             │
    ┌────────┴────────┬────────────┬─────────────┐
    │                 │            │             │
    ▼                 ▼            ▼             ▼
 OPTION 1         OPTION 2     OPTION 3     OPTION 4
 View Videos   Engagement    Statistics    Top Channels
    │                 │            │             │
    ▼                 ▼            ▼             ▼
┌──────────────────────────────────────────────────────────┐
│  LAYER 2: SERVICE LAYER (service_layer.py)               │
│  ─────────────────────────────────────────────────────   │
│                                                          │
│  OPTION 1: View Trending Videos                          │
│  └─> No service functions needed                         │
│  └─> Directly pass videos to UI                          │
│                                                          │
│  OPTION 2: Engagement Analysis                           │
│  └─> calculate_engagement_rate(likes, views)             │
│  └─> get_comment_to_like_ratio()                         │
│  └─> rank_videos_by_engagement(videos)                   │
│     Returns: sorted_videos                               │
│                                                          │
│  OPTION 3: Overall Statistics                            │
│  └─> generate_statistics_summary(videos)                 │
│     ├─> calculate_average_engagement_rate()              │
│     ├─> calculate_average_views()                        │
│     ├─> calculate_average_likes()                        │
│     └─> get_top_channels(videos, limit=10)               │
│     Returns: stats_dict                                  │
│                                                          │
│  OPTION 4: Top Channels                                  │
│  └─> get_top_channels(videos, limit=15)                  │
│     Returns: [(channel_name, count), ...]                │
│                                                          │
└────────────┬─────────────────────────────────────────────┘
             │ Returns calculated/sorted data
             │
             ▼
┌──────────────────────────────────────────────────────────┐
│  LAYER 3: UI LAYER (ui_layer.py)                         │
│  ─────────────────────────────────────────────────────   │
│  Display Results:                                        │
│                                                          │
│  OPTION 1:                                               │
│  └─> display_trending_videos(videos, count=20)           │
│     Creates formatted table with tabulate()              │
│                                                          │
│  OPTION 2:                                               │
│  └─> display_engagement_analysis(videos, top_n=15)       │
│     Creates ranked table by engagement %                 │
│                                                          │
│  OPTION 3:                                               │
│  └─> display_statistics(stats)                           │
│  └─> display_top_channels(top_channels)                  │
│     Creates summary statistics table                     │
│                                                          │
│  OPTION 4:                                               │
│  └─> display_top_channels(top_channels, limit=15)        │
│     Creates channel frequency table                      │
│                                                          │
│  OPTION 5:                                               │
│  └─> display_goodbye()  ◄─── Exit program               │
│                                                          │
└────────────┬─────────────────────────────────────────────┘
             │
             ▼ Loop back to menu (unless Exit selected)
        [STEP REPEAT]
```

---

### File Invocation Order (Chronological)

| # | Step | File/Function | Action | Input | Output |
|---|------|---|---|---|---|
| 1 | Import | `main.py` | Imports layers | None | modules loaded |
| 2 | Import | `main.py` | Imports `data_layer` | None | load_trending_videos ready |
| 3 | Import | `main.py` | Imports `service_layer` | None | analysis functions ready |
| 4 | Import | `main.py` | Imports `ui_layer` | None | display functions ready |
| 5 | Load | `data_layer.load_trending_videos()` | Reads CSV | file path | List[Video] |
| 6 | Create | `models.Video.__init__()` | Creates Video objects | row data | Video objects |
| 7 | Display | `ui_layer.display_loading()` | Shows loading message | None | "Loading..." |
| 8 | Display | `ui_layer.display_main_menu()` | Shows menu options | None | Menu display |
| 9 | Input | `ui_layer.get_user_choice()` | Gets user input | None | choice (1-5) |
| 10a | Option 1 | `ui_layer.display_trending_videos()` | Shows top videos | videos | Formatted table |
| 10b | Option 2 | `service_layer.rank_videos_by_engagement()` | Ranks videos | videos | sorted_videos |
| 10b | Option 2 | `ui_layer.display_engagement_analysis()` | Shows rankings | videos | Engagement table |
| 10c | Option 3 | `service_layer.generate_statistics_summary()` | Calculates stats | videos | stats_dict |
| 10c | Option 3 | `ui_layer.display_statistics()` | Shows summary | stats | Stats table |
| 10c | Option 3 | `service_layer.get_top_channels()` | Counts channels | videos | channel_counts |
| 10c | Option 3 | `ui_layer.display_top_channels()` | Shows channels | channels | Channels table |
| 10d | Option 4 | `service_layer.get_top_channels()` | Counts channels | videos | channel_counts |
| 10d | Option 4 | `ui_layer.display_top_channels()` | Shows channels | channels | Channels table |
| 10e | Option 5 | `ui_layer.display_goodbye()` | Exit message | None | Goodbye message |
| 11 | Loop | Back to Step 8 | Menu loop | None | Next choice or exit |

---

### Data Flow Through Layers

```
CSV File (data/USvideos.csv)
    │
    ▼
[DATA LAYER]
data_layer.load_trending_videos()
    │
    ├─> pandas.read_csv()
    │
    ├─> for each row:
    │   └─> models.Video(row_data)
    │
    └─> returns: List[Video]
         [
           Video(title="...", views=1000000, ...),
           Video(title="...", views=2000000, ...),
           ...
         ]
    │
    ▼
[SERVICE LAYER] ◄─── Only called when user selects an option
    │
    ├─> process videos
    ├─> calculate metrics
    ├─> sort/rank results
    │
    └─> returns: Processed data
         {
           'total_videos': 50,
           'avg_views': 500000,
           'top_channels': [('Channel A', 5), ...]
         }
    │
    ▼
[UI LAYER]
    │
    ├─> format data for display
    ├─> create tables with tabulate
    ├─> print to console
    │
    └─> Display to user
```

---

### Key Function Dependencies

```
main.py (Orchestrator)
│
├─> data_layer.py
│   └─> models.Video (Create Video objects)
│
├─> service_layer.py
│   └─> Uses Video objects for calculations
│
└─> ui_layer.py
    └─> Displays results using tabulate
```

---

#### Layer 1: Data Layer (`data_layer.py`)
- **Responsibility**: Load and parse data
- **Main Function**: `load_trending_videos(csv_file_path)`
  - Reads CSV using pandas
  - Creates Video objects
  - Handles errors gracefully
- **Why**: Easy to swap CSV for API later without changing other code

#### Layer 2: Service Layer (`service_layer.py`)
- **Responsibility**: Business logic and calculations
- **Main Functions**:
  - `calculate_engagement_rate()` — likes/views × 100
  - `rank_videos_by_engagement()` — sort videos by engagement
  - `get_top_channels()` — find most-trending channels
  - `generate_statistics_summary()` — aggregate statistics
- **Why**: All calculations in one place, easy to test

#### Layer 3: UI Layer (`ui_layer.py`)
- **Responsibility**: Display and user interaction
- **Main Functions**:
  - `display_trending_videos()` — show formatted table
  - `display_statistics()` — show summary stats
  - `display_main_menu()` — show menu options
- **Why**: Easy to change output format (console → web → GUI)

### Data Model (`models.py`)

Simple `Video` class representing one YouTube video:
```python
Video(
    video_id,        # Unique identifier
    title,          # Video title
    channel_name,   # Channel name
    views,          # View count
    likes,          # Like count
    comments,       # Comment count
    publish_date    # Publication date
)
```

Methods:
- `get_engagement_rate()` — Returns engagement % 
- `get_comment_to_like_ratio()` — Returns comment/like ratio

## Key Metrics Explained

### Engagement Rate
```
Engagement Rate (%) = (Likes / Views) × 100
```
Shows what percentage of viewers liked the video. **Higher = more engaging content**.

Example: 
- 1,000,000 views, 50,000 likes → 5% engagement rate
- 1,000,000 views, 10,000 likes → 1% engagement rate → Less engaging

### Comment-to-Like Ratio
```
Ratio = Comments / Likes
```
Shows how much people discuss vs. quick-like. **Higher = more discussion**.

Example:
- 50,000 likes, 25,000 comments → 0.5 ratio → High discussion
- 50,000 likes, 1,000 comments → 0.02 ratio → Low discussion

### Top Channels
Counts how many trending videos each channel has. **Higher count = consistently makes trending content**.

## Verification Checklist

Run through these to verify everything works:

- [ ] Install: `pip install -r requirements.txt` (no errors)
- [ ] Download: CSV files in `data/` folder
- [ ] Run: `python src/main.py` (starts successfully)
- [ ] Menu: All 5 options display correctly
- [ ] Option 1: Shows 20 trending videos in a table
- [ ] Option 2: Shows top 15 videos by engagement rate
- [ ] Option 3: Shows statistics summary + top channels
- [ ] Option 4: Shows more detailed channel statistics
- [ ] Engagement: All engagement rates shown as valid % (e.g., 5.23%)
- [ ] Errors: Program handles missing CSV gracefully

## How to Modify (Examples)

### Show different number of videos
In `src/main.py`, change line in menu choice 1:
```python
ui_layer.display_trending_videos(videos, count=50)  # Show 50 instead of 20
```

### Add a new ranking (by likes)
In `src/service_layer.py`, add function:
```python
def rank_videos_by_likes(videos):
    return sorted(videos, key=lambda v: v.likes, reverse=True)
```

### Change table formatting
In `src/ui_layer.py`, modify the `tabulate()` call:
```python
print(tabulate(table_data, headers=headers, tablefmt="fancy_grid"))
```

See [tabulate docs](https://pypi.org/project/tabulate/) for more formats.

## Learning Outcomes

This project teaches:
- **Software Architecture**: 3-layer separation of concerns
- **Data Handling**: Loading and parsing CSV files with pandas
- **Problem Solving**: Ranking, filtering, and aggregating data
- **Clean Code**: Clear naming, simple functions, helpful comments
- **Testing**: Easy to add unit tests for each layer

## Troubleshooting

### Setup Issues

#### "ModuleNotFoundError: No module named 'kagglehub'"
**Solution**: Install dependencies with `pip install -r requirements.txt`

#### "ERROR: Authentication error while downloading dataset"
**Solution**: 
1. Create Kaggle account: https://www.kaggle.com/register
2. Download API token: https://www.kaggle.com/settings/account (API section)
3. Place `kaggle.json` in `~/.kaggle/` directory
4. Run setup again: `python setup_data.py`

#### "ERROR: No CSV files found in downloaded dataset"
**Solution**:
- The Kaggle dataset might be updated. Download manually:
  - https://www.kaggle.com/datasets/datasnaek/youtube-new
  - Extract and place CSV files in `data/` folder

### Runtime Issues

#### "No CSV files found in 'data' folder"
**Solution**: Run `python setup_data.py` first, or manually download from Kaggle

#### "ModuleNotFoundError: No module named 'pandas'"
**Solution**: Run `pip install -r requirements.txt`

#### "ParserError: Error tokenizing data"
**Solution**: Make sure CSV file is not corrupted. Download fresh from Kaggle.

### Column errors ("Missing required columns...")
**Solution**: Verify CSV has columns: `video_id`, `title`, `channelTitle`, `views`, `likes`, `comment_count`, `publishedAt`

## Future Enhancements

Easy to add (if you want to extend):
- 📊 Export statistics to JSON/Excel
- 📈 Visualizations using matplotlib
- 🔍 Filter by date range
- 🌍 Switch between region datasets
- 💾 Save/load analysis results
- 🧪 Unit tests for service layer

## Dependencies

- **pandas** (v1.3.0+) — Read and parse CSV files
- **python-dotenv** (v0.19.0+) — Load environment variables (optional, for future use)
- **tabulate** (v0.8.9+) — Format tables for display
- **kagglehub** (v0.2.0+) — Automatically download Kaggle datasets

All are listed in `requirements.txt`. Install with:
```bash
pip install -r requirements.txt
```

## Authors & License

Created as a college project to demonstrate clean software architecture.

**Enjoy analyzing YouTube trends! 🎥📊**
