"""
Data Layer - CSV File Loader
Loads YouTube trending video data from Kaggle CSV files
"""

import pandas as pd
from models import Video


def load_trending_videos(csv_file_path):
    """
    Load YouTube trending videos from a CSV file.
    
    Expected CSV columns (with flexible naming):
    - video_id or videoid
    - title
    - channelTitle or channel_title or channeltitle
    - views
    - likes
    - comment_count or comments
    - publishedAt or published_at or publishedat
    
    Args:
        csv_file_path (str): Path to the CSV file
        
    Returns:
        list: List of Video objects
        
    Raises:
        FileNotFoundError: If CSV file doesn't exist
        pd.errors.ParserError: If CSV is malformed
    """
    try:
        # Read CSV file using pandas
        df = pd.read_csv(csv_file_path)
        
        print(f"[DEBUG] Columns in CSV: {list(df.columns)}")
        
        # Normalize column names to lowercase for consistency
        df.columns = [col.lower() for col in df.columns]
        
        # Handle variations in column naming
        column_aliases = {
            'video_id': ['video_id', 'videoid'],
            'title': ['title'],
            'channel_name': ['channeltitle', 'channel_title', 'channelname'],
            'views': ['views'],
            'likes': ['likes'],
            'comments': ['comment_count', 'comments', 'comment_num'],
            'publish_date': ['publishedat', 'published_at', 'publisheddate', 'publish_time', 'trending_date']
        }
        
        # Find actual column names in the CSV
        found_columns = {}
        for field_name, aliases in column_aliases.items():
            for alias in aliases:
                if alias in df.columns:
                    found_columns[field_name] = alias
                    print(f"[DEBUG] Found '{field_name}' as column: '{alias}'")
                    break
            else:
                print(f"[WARNING] Column '{field_name}' not found. Aliases tried: {aliases}")
        
        # Check if all essential columns were found
        essential_fields = ['video_id', 'title', 'channel_name', 'views', 'likes', 'comments', 'publish_date']
        missing = [field for field in essential_fields if field not in found_columns]
        
        if missing:
            raise ValueError(f"Missing required columns: {missing}. Available columns: {list(df.columns)}")
        
        # Create Video objects from DataFrame rows
        videos = []
        for index, row in df.iterrows():
            try:
                video = Video(
                    video_id=str(row[found_columns['video_id']]),
                    title=str(row[found_columns['title']]),
                    channel_name=str(row[found_columns['channel_name']]),
                    views=row[found_columns['views']],
                    likes=row[found_columns['likes']],
                    comments=row[found_columns['comments']],
                    publish_date=str(row[found_columns['publish_date']])
                )
                videos.append(video)
            except KeyError as e:
                print(f"Warning: Skipping row {index} - missing field: {e}")
                continue
        
        print(f"[OK] Successfully loaded {len(videos)} videos from {csv_file_path}")
        return videos
    
    except FileNotFoundError:
        raise FileNotFoundError(f"CSV file not found: {csv_file_path}")
    except pd.errors.ParserError as e:
        raise pd.errors.ParserError(f"Error parsing CSV file: {e}")
    except Exception as e:
        raise Exception(f"Unexpected error loading CSV: {e}")


def get_available_regions(data_folder_path):
    """
    Get list of available region datasets.
    
    Args:
        data_folder_path (str): Path to the data folder
        
    Returns:
        list: List of available CSV files (region codes)
    """
    import os
    import glob
    
    csv_files = glob.glob(os.path.join(data_folder_path, "*.csv"))
    regions = [os.path.basename(f).replace(".csv", "") for f in csv_files]
    return regions
