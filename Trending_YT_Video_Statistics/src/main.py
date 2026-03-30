"""
Main Entry Point - Program Orchestrator
Manages menu system and coordinates all layers
"""

import os
import sys

# Add src directory to path for imports
# __file__ is in src/, so dirname gets us src/, and we add it to path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from data_layer import load_trending_videos
import service_layer
import ui_layer


def main():
    """
    Main program flow.
    Load data, display menu, and handle user interactions.
    """
    # Paths - go up to project root, then to data folder
    current_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    data_folder = os.path.join(current_dir, 'data')
    
    # Try to find a CSV file in the data folder
    csv_files = [f for f in os.listdir(data_folder) if f.endswith('.csv')]
    
    if not csv_files:
        ui_layer.display_error(
            f"No CSV files found in '{data_folder}' folder.\n\n"
            "SETUP OPTIONS:\n\n"
            "Option 1 - AUTOMATIC (Recommended):\n"
            "  1. Install kagglehub: pip install -r requirements.txt\n"
            "  2. Request Kaggle API credentials at: https://www.kaggle.com/settings/account\n"
            "  3. Run the setup script: python setup_data.py\n\n"
            "Option 2 - MANUAL:\n"
            "  1. Visit: https://www.kaggle.com/datasets/datasnaek/youtube-new\n"
            "  2. Click 'Download' button\n"
            "  3. Extract CSV file(s) to the 'data' folder\n\n"
            "Then run this program again."
        )
        return
    
    # Use the first CSV file found
    csv_file_path = os.path.join(data_folder, csv_files[0])
    region_name = csv_files[0].replace('.csv', '').upper()
    
    # Load data
    ui_layer.display_loading()
    try:
        videos = load_trending_videos(csv_file_path)
    except Exception as e:
        ui_layer.display_error(f"Failed to load data: {str(e)}")
        return
    
    if not videos:
        ui_layer.display_error("No videos loaded from the CSV file.")
        return
    
    # Main menu loop
    while True:
        ui_layer.display_main_menu()
        choice = ui_layer.get_user_choice()
        
        if choice == '1':
            # View Trending Videos (by views)
            ui_layer.display_trending_videos(videos, count=20)
        
        elif choice == '2':
            # View Engagement Analysis
            ui_layer.display_engagement_analysis(videos, top_n=15)
        
        elif choice == '3':
            # View Overall Statistics
            stats = service_layer.generate_statistics_summary(videos)
            ui_layer.display_statistics(stats)
            
            # Also show top channels
            top_channels = service_layer.get_top_channels(videos, limit=10)
            ui_layer.display_top_channels(top_channels)
        
        elif choice == '4':
            # View Top Channels
            top_channels = service_layer.get_top_channels(videos, limit=15)
            ui_layer.display_top_channels(top_channels, limit=15)
        
        elif choice == '5':
            # Exit
            ui_layer.display_goodbye()
            break
        
        else:
            ui_layer.display_error("Invalid choice. Please enter 1-5.")
            continue


if __name__ == "__main__":
    main()
